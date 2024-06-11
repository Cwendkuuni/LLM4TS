import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        // Typical use case
        assertNotNull(dataSource.getConnection());

        // Edge case
        dataSource.setConnection(mock(Connection.class));
        assertNotNull(dataSource.getConnection());

        // Error scenario
        dataSource.setConnection(null);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        // Typical use case
        assertEquals(dataSource.getConnection(), dataSource.getConnection("user", "pass"));

        // Error scenario
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
    }

    @Test
    public void testResetConnection() {
        // Typical use case
        dataSource.setConnection(mock(Connection.class));
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());

        // Error scenario
        dataSource.setConnection(null);
        assertThrows(SQLException.class, () -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        // Typical use case
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        // Typical use case
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() {
        // Typical use case
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        // Typical use case
        assertTrue(dataSource.tryLock());
    }

}