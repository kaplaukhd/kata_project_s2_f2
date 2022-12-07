package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS kata_f2_s1.user" + "(id int not null auto_increment," + " name VARCHAR(50), " + "lastname VARCHAR(50), " + "age int, " + "PRIMARY KEY (id));");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("Drop table if exists kata_f2_s1.user;");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = null;
        try {
            connection = Util.connection();
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO kata_f2_s1.user(name, lastname, age) VALUES(?,?,?)");
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Can't save user " + name);
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.close();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM kata_f2_s1.user WHERE id;");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("User with id: " + id + "could not deleted");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, name, lastName, age from kata_f2_s1.user");
            while (resultSet.next()) {
                User tempUser = new User();
                tempUser.setId((long) resultSet.getInt("id"));
                tempUser.setName(resultSet.getString("name"));
                tempUser.setLastName(resultSet.getString("lastName"));
                tempUser.setAge((byte) resultSet.getInt("age"));
                userList.add(tempUser);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.connection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("TRUNCATE TABLE kata_f2_s1.user");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}