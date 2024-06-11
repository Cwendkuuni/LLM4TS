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
    public void testResetConnection() {
        try {
            dataSource.resetConnection();
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to reset connection: " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithInvalidUsernamePassword() {
        try {
            dataSource.getConnection("invalidUser", "invalidPassword");
            fail("Expected SQLException for invalid username/password");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        try {
            assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail("Failed to close connection: " + e.getMessage());
        }
    }

    @Test
    public void testAutoReconnect() {
        dataSource.setAutoReconnect(true);
        try {
            dataSource.resetConnection();
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to auto reconnect: " + e.getMessage());
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = (con) -> {};
        dataSource.addInitializer(mockInitializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

}