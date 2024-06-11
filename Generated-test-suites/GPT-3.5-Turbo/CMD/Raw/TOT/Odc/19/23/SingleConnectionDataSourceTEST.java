import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Typical use case
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        // Edge case
        Connection connection2 = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection2);

        // Error scenario
        dataSource.setUsername("wrongUser");
        dataSource.setPassword("wrongPassword");
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testResetConnection() throws SQLException {
        // Typical use case
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());

        // Edge case
        dataSource.setAutoReconnect(false);
        assertThrows(SQLException.class, () -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        // Typical use case
        ConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());

        // Edge case
        dataSource.addInitializer(null);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        // Typical use case
        dataSource.close();

        // Edge case
        dataSource.setConnection(mock(Connection.class));
        dataSource.close();
    }

    private class TestConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) throws SQLException {
            // Initialize connection for testing purposes
        }
    }
}