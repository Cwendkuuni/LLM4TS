import static org.junit.Assert.*;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        // Test typical use case
        // Initialize SingleConnectionDataSource
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            // Get connection
            dataSource.getConnection();
            // Assert connection is not null
            assertNotNull(dataSource.getConnection());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        // Test edge case
        // Initialize SingleConnectionDataSource with autoReconnect set to true
        SingleConnectionDataSource dataSourceAutoReconnect = new SingleConnectionDataSource(true);
        try {
            // Get connection
            dataSourceAutoReconnect.getConnection();
            // Assert connection is not null
            assertNotNull(dataSourceAutoReconnect.getConnection());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        // Test error scenario
        // Initialize SingleConnectionDataSource with uninitialized connection
        SingleConnectionDataSource dataSourceUninitialized = new SingleConnectionDataSource();
        try {
            // Get connection
            dataSourceUninitialized.getConnection();
            // Connection should throw SQLException
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Connection was closed or not valid"));
        } catch (Exception e) {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        // Test typical use case
        // Initialize SingleConnectionDataSource
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            // Get connection with username and password
            dataSource.getConnection("user", "pass");
            // Connection should throw SQLException
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Invalid username or password"));
        } catch (Exception e) {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        // Test typical use case
        // Initialize SingleConnectionDataSource
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            // Reset connection
            dataSource.resetConnection();
            // Assert connection is reset
            assertNull(dataSource.getConnection());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAddInitializer() {
        // Test typical use case
        // Initialize SingleConnectionDataSource
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        // Add connection initializer
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // Mock initializer
            }
        };
        dataSource.addInitializer(initializer);

        // Assert initializerList contains the added initializer
        assertEquals(1, dataSource.getInitializerList().size());
        assertEquals(initializer, dataSource.getInitializerList().get(0));
    }

    @Test
    public void testClose() {
        // Test typical use case
        // Initialize SingleConnectionDataSource
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        // Close connection
        dataSource.close();

        // Assert connection is closed
        assertNull(dataSource.getConnection());
    }

}