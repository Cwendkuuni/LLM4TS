import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


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
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        String username = "wrongUser";
        String password = "wrongPassword";
        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testProxyConnectionLock() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection proxyConnection = dataSource.getConnectionProxy(connection);

        // Test locking mechanism
        Runnable runnable = () -> {
            try {
                dataSource.getConnectionProxy(connection);
            } catch (Exception e) {
                assertTrue(e instanceof ConflictException);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            Thread.sleep(200); // Wait for the thread to acquire the lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(thread.isAlive());
        proxyConnection.close();

        try {
            Thread.sleep(200); // Wait for the thread to release the lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(thread.isAlive());

    }

}