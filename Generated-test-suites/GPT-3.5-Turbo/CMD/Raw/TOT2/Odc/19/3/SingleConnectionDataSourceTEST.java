import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosedAndNotAutoReconnect() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(0)).thenReturn(false);
        dataSource.setConnection(connection);
        
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        
        assertThrows(SQLException.class, () -> dataSource.getConnection("anotherUser", "anotherPass"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        dataSource.resetConnection();
        
        verify(connection, times(1)).close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        
        dataSource.close();
        
        verify(connection, times(1)).close();
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        Lock lock = mock(Lock.class);
        dataSource.setLock(lock);
        
        Connection proxy = dataSource.getConnectionProxy(connection);
        
        assertNotNull(proxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        
        dataSource.initConnection(connection);
        
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.onConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testOnConnectionReset() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        
        dataSource.onConnectionReset(mock(Connection.class));
        
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        
        assertThrows(IllegalStateException.class, dataSource::innerCreateConnection);
    }

}