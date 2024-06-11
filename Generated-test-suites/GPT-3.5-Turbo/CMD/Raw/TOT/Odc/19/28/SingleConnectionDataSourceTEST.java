import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;

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
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernamePassword() {
        // Valid username and password
        assertThrows(SQLException.class, () -> dataSource.getConnection("testUser", "test"));
        // Invalid username and password
        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testTryLock() {
        Lock lock = mock(Lock.class);
        when(lock.tryLock(anyLong(), any())).thenReturn(Boolean.TRUE);
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        assertNotNull(dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher publisher = mock(EventPublisher.class);
        ConnectionResetEvent resetEvent = mock(ConnectionResetEvent.class);
        dataSource.setEventPublisher(publisher);
        dataSource.onConnectionReset(resetEvent.getConnection());
        verify(publisher, times(1)).publishEvent(resetEvent);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

}