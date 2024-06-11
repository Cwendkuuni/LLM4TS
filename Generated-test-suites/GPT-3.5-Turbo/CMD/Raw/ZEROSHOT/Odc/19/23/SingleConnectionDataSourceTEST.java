import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertTrue(singleConnectionDataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();
        Connection proxyConnection = singleConnectionDataSource.getConnectionProxy(connection, lock);
        assertNotNull(proxyConnection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(anyBoolean());
    }

    @Test
    public void testTryLock() {
        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        when(initializer.init(connection)).thenReturn(true);
        singleConnectionDataSource.addInitializer(initializer);
        singleConnectionDataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

}