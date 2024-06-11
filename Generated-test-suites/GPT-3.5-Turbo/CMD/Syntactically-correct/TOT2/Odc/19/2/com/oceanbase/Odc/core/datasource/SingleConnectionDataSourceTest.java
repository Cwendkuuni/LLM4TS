import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @BeforeClass
    public static void setup() {
        // Setup any necessary configurations or resources here
    }

    @Test
    public void testGetConnection() {
        try {
            dataSource = new SingleConnectionDataSource();
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Error retrieving connection: " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithCredentials() {
        try {
            dataSource = new SingleConnectionDataSource();
            assertNotNull(dataSource.getConnection("username", "password"));
        } catch (SQLException e) {
            fail("Error retrieving connection with credentials: " + e.getMessage());
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("invalid_user", "invalid_password");
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource = new SingleConnectionDataSource();
            dataSource.resetConnection();
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Error resetting connection: " + e.getMessage());
        }
    }

    @Test
    public void testConnectionClose() {
        try {
            dataSource = new SingleConnectionDataSource();
            dataSource.close();
            assertNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Error closing connection: " + e.getMessage());
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testLockTimeout() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setTimeOutMillis(1); // Set timeout to 1 millisecond for testing
        try {
            dataSource.getConnection();
            fail("Lock timeout not thrown");
        } catch (ConflictException e) {
            assertEquals("Connection is occupied, waited 1 millis", e.getMessage());
        } catch (SQLException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    private static class TestConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) throws SQLException {
            // Do nothing for testing purposes
        }
    }

}