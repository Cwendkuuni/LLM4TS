import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        // Typical use case
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to get connection");
        }

        // Edge case
        dataSource.setConnection(null);
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to get connection");
        }

        // Error scenario
        dataSource.setAutoReconnect(false);
        try {
            dataSource.getConnection();
            fail("Should have thrown SQLException");
        } catch (SQLException e) {
            assertEquals("Connection was closed or not valid", e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Typical use case
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "user";
        String password = "password";
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        try {
            Connection connection = dataSource.getConnection(username, password);
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to get connection with credentials");
        }

        // Error scenario
        try {
            dataSource.getConnection("invalid", "password");
            fail("Should have thrown SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        // Typical use case
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.resetConnection();
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Failed to reset connection");
        }
    }

    @Test
    public void testClose() {
        // Typical use case
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.getConnection();
            dataSource.close();
            assertTrue(dataSource.getConnection().isClosed());
        } catch (SQLException e) {
            fail("Failed to close connection");
        }
    }
}