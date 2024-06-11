import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        try {
            Connection connection = dataSource.getConnection("username", "password");
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to get connection with valid credentials");
        }
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        try {
            dataSource.getConnection("invalidUsername", "invalidPassword");
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.resetConnection();
            assertTrue(true);
        } catch (SQLException e) {
            fail("Failed to reset connection");
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = (Connection conn) -> {System.out.println("Initializing connection");};
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testCloseConnection() {
        dataSource.getConnection(); // Get connection to ensure it's not null
        dataSource.close(); // Close connection
        assertNull(dataSource.getConnection()); // Connection should be null after closing
    }

    @Test
    public void testConnectionProxy() {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testInitConnection() {
        try {
            Connection connection = dataSource.getConnection();
            dataSource.initConnection(connection);
            assertTrue(true);
        } catch (SQLException e) {
            fail("Failed to init connection");
        }
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(dataSource.getConnection());
        assertTrue(true);
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.onConnectionReset(dataSource.getConnection());
        assertTrue(true);
    }

    @Test
    public void testInnerCreateConnection() {
        try {
            Connection connection = dataSource.innerCreateConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to create connection");
        }
    }
}