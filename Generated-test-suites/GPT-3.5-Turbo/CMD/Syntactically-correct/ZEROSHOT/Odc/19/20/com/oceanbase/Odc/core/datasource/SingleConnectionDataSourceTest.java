import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.*;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(mock(EventPublisher.class));
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Lock mockLock = mock(ReentrantLock.class);
        Connection mockConnection = mock(Connection.class);
        when(mockLock.tryLock(anyLong(), any())).thenReturn(true);
        when(dataSource.tryLock()).thenReturn(true);
        dataSource.setConnection(mockConnection);
        dataSource.setLock(mockLock);

        dataSource.resetConnection();
        verify(mockConnection, times(1)).close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);
        List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertEquals(mockInitializer, initializerList.get(0));

    }

    @Test
    public void testClose() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        dataSource.setConnection(mockConnection);

        dataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        dataSource.setAutoCommit(true);

        dataSource.prepareConnection(mockConnection);

        verify(mockConnection, times(1)).setAutoCommit(true);

    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);

        dataSource.initConnection(mockConnection);

        verify(mockInitializer, times(1)).init(mockConnection);
    }

    @Test
    public void testOnConnectionReset() {
        Connection mockConnection = mock(Connection.class);
        ConnectionResetEvent mockEvent = mock(ConnectionResetEvent.class);
        EventPublisher mockPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(mockPublisher);

        when(mockPublisher.publishEvent(mockEvent)).then(invocation -> {
            throw new RuntimeException("Event publishing failed");
        });

        dataSource.onConnectionReset(mockConnection);
        verify(mockPublisher, times(1)).publishEvent(mockEvent);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Lock mockLock = mock(Lock.class);
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setTimeOutMillis(1000);

        when(dataSource.newConnectionFromDriver("username", "password")).thenReturn(mockConnection);
        when(dataSource.tryLock()).thenReturn(true);
        when(mockConnection.isValid(1000)).thenReturn(true);

        Connection connection = dataSource.innerCreateConnection();

        assertNotNull(connection);
        assertEquals(mockConnection, dataSource.getConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        Connection mockConnection = mock(Connection.class);
        Lock mockLock = mock(Lock.class);
        CloseIgnoreInvocationHandler handler =
                new CloseIgnoreInvocationHandler(mockConnection, mockLock);

        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, handler);

        Method equalsMethod = Object.class.getMethod("equals", Object.class);
        assertTrue((boolean) equalsMethod.invoke(proxy, proxy));

        Method hashCodeMethod = Object.class.getMethod("hashCode");
        assertEquals(System.identityHashCode(proxy), hashCodeMethod.invoke(proxy));

        Method unwrapMethod = Connection.class.getMethod("unwrap", Class.class);
        assertEquals(proxy, unwrapMethod.invoke(proxy, Connection.class));

        Method isWrapperForMethod = Connection.class.getMethod("isWrapperFor", Class.class);
        assertTrue((boolean) isWrapperForMethod.invoke(proxy, Connection.class));

        Method closeMethod = Connection.class.getMethod("close");
        assertNull(closeMethod.invoke(proxy));

        Method isClosedMethod = Connection.class.getMethod("isClosed");
        assertFalse((boolean)isClosedMethod.invoke(proxy));
    }
}