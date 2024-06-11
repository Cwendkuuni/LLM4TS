package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

/**
 * Test for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @date 2023-03-29 13:30
 */
@Slf4j
public class SingleConnectionDataSourceTest {

    private static final String DUMMY_USERNAME = "testUser";
    private static final String DUMMY_PASSWORD = "testPassword";
    private static final String DUMMY_DRIVER_CLASS = "org.h2.Driver";

    private static final String DUMMY_EVENT_PUBLISHER_CLASS = "com.oceanbase.odc.common.event.DummyEventPublisher";

    private Connection connection;
    private EventPublisher eventPublisher;
    private SingleConnectionDataSource singleConnectionDataSource;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() throws Exception {
        Class<?> driverClass = Class.forName(DUMMY_DRIVER_CLASS);
        DriverManager.registerDriver((java.sql.Driver) driverClass.newInstance());

        eventPublisher = mock(EventPublisher.class);
        singleConnectionDataSource = new SingleConnectionDataSource() {{
            setEventPublisher(eventPublisher);
        }};
        singleConnectionDataSource.setAutoReconnect(true);

        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        initializerList = new LinkedList<>();
        singleConnectionDataSource.addInitializer(connectionInitializer);

        connection = mock(Connection.class);
    }

    @After
    public void tearDown() throws Exception {
        DriverManager.getRegisteredDrivers().stream().forEach(driver -> DriverManager.deregisterDriver(driver));
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        Connection actualConnection = singleConnectionDataSource.getConnection();
        assertNotNull(actualConnection);
        assertEquals(connection, actualConnection);

        verify(connection).isValid(singleConnectionDataSource.getLoginTimeout());
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.setUsername(DUMMY_USERNAME);
        singleConnectionDataSource.setPassword(DUMMY_PASSWORD);

        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        singleConnectionDataSource.resetConnection();

        verify(singleConnectionDataSource).close();
        verify(singleConnectionDataSource).initConnection(connection);
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(connectionInitializer);

        ArgumentCaptor<ConnectionInitializer> connectionInitializerArgumentCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        verify(singleConnectionDataSource).addInitializer(connectionInitializerArgumentCaptor.capture());
        ConnectionInitializer capturedConnectionInitializer = connectionInitializerArgumentCaptor.getValue();

        assertTrue(singleConnectionDataSource.getInitializerList().contains(capturedConnectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);

        singleConnectionDataSource.close();

        verify(connection, never()).close();
    }

    @Test
    public void testCloseTwice() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);

        singleConnectionDataSource.close();
        singleConnectionDataSource.close();

        verify(connection, never()).close();
    }

    @Test
    public void testInitConnection() throws SQLException {
        when(connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(false);

        singleConnectionDataSource.initConnection(connection);

        verify(singleConnectionDataSource).getConnectionProxy(connection);

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(singleConnectionDataSource).prepareConnection(connectionArgumentCaptor.capture());
        Connection capturedConnection = connectionArgumentCaptor.getValue();
        assertTrue(Objects.equals(singleConnectionDataSource.getAutoCommit(), capturedConnection.getAutoCommit()));
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        singleConnectionDataSource.setUsername(DUMMY_USERNAME);
        singleConnectionDataSource.setPassword(DUMMY_PASSWORD);

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(singleConnectionDataSource, never()).onConnectionCreated(connectionArgumentCaptor.capture());

        Connection connection = mock(Connection.class);
        singleConnectionDataSource.onConnectionCreated(connection);

        verify(singleConnectionDataSource).onConnectionCreated(connectionArgumentCaptor.capture());
        Connection capturedConnection = connectionArgumentCaptor.getValue();
        assertEquals(DUMMY_USERNAME, capturedConnection.getUsername());
        assertEquals(DUMMY_PASSWORD, capturedConnection.getPassword());
        assertTrue(singleConnectionDataSource.getConnection() == capturedConnection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(singleConnectionDataSource.tryLock()).thenReturn(true);

        Connection actualConnection = singleConnectionDataSource.getConnectionProxy(connection);
        assertNotNull(actualConnection);
        assertEquals(connection, actualConnection);

        verify(singleConnectionDataSource).lock();

        singleConnectionDataSource.setAutoCommit(true);

        Connection actualConnectionWithAutoCommit = singleConnectionDataSource.getConnectionProxy(connection);
        assertNotNull(actualConnectionWithAutoCommit);
        assertEquals(connection, actualConnectionWithAutoCommit);
        assertTrue(Objects.equals(singleConnectionDataSource.getAutoCommit(), actualConnectionWithAutoCommit.getAutoCommit()));
    }

    @Test
    public void testGetConnectionProxyWithExpiredConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(false);

        Connection actualConnection = singleConnectionDataSource.getConnectionProxy(connection);

        verify(singleConnectionDataSource).resetConnection();

        Connection resetConnection = singleConnectionDataSource.getConnectionProxy(connection);
        assertNotNull(resetConnection);
        assertEquals(connection, resetConnection);
    }

    @Test
    public void testGetConnectionProxyWithExpiredConnectionAndAutoReconnectDisabled() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSourceWithAutoReconnectDisabled = new SingleConnectionDataSource() {{
            setAutoReconnect(false);
        }};

        when(connection.isValid(anyInt())).thenReturn(false);

        SQLException exception = assertThrows(SQLException.class, () -> singleConnectionDataSourceWithAutoReconnectDisabled.getConnectionProxy(connection));

        assertEquals("Connection was closed or not valid", exception.getMessage());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(true);

        when(singleConnectionDataSource.newConnectionFromDriver(singleConnectionDataSource.getUsername(), singleConnectionDataSource.getPassword())).thenReturn(connection);

        Connection actualConnection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(actualConnection);
        assertEquals(connection, actualConnection);

        verify(singleConnectionDataSource).initConnection(connection);
        verify(singleConnectionDataSource).lock();
        verify(singleConnectionDataSource).onConnectionCreated(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnectionWithExpiredConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(false);

        when(singleConnectionDataSource.newConnectionFromDriver(singleConnectionDataSource.getUsername(), singleConnectionDataSource.getPassword())).thenReturn(connection);

        singleConnectionDataSource.resetConnection();

        Connection actualConnection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(actualConnection);
        assertEquals(connection, actualConnection);

        verify(singleConnectionDataSource).initConnection(connection);
        verify(singleConnectionDataSource).lock();
        verify(singleConnectionDataSource).onConnectionCreated(connection);
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnectionWithExpiredConnectionAndAutoReconnectDisabled() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSourceWithAutoReconnectDisabled = new SingleConnectionDataSource() {{
            setAutoReconnect(false);
        }};

        Connection connection = mock(Connection.class);
        when(connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(false);

        when(singleConnectionDataSourceWithAutoReconnectDisabled.newConnectionFromDriver(singleConnectionDataSourceWithAutoReconnectDisabled.getUsername(), singleConnectionDataSourceWithAutoReconnectDisabled.getPassword())).thenReturn(connection);

        assertThrows(SQLException.class, () -> singleConnectionDataSourceWithAutoReconnectDisabled.innerCreateConnection());
    }

    @Test
    public void testNewConnectionFromDriver() throws SQLException {
        Class<?> driverClass = Class.forName(DUMMY_DRIVER_CLASS);

        Connection actualConnection = singleConnectionDataSource.newConnectionFromDriver(singleConnectionDataSource.getUsername(), singleConnectionDataSource.getPassword());

        assertNotNull(actualConnection);
        assertTrue(driverClass.isInstance(actualConnection.getDriver()));
        assertTrue(driverClass.isAssignableFrom(actualConnection.getClass()));
    }

    @Test
    public void testNewConnectionFromDriverWithInvalidCredentials() throws SQLException {
        Class<?> driverClass = Class.forName(DUMMY_DRIVER_CLASS);

        assertThrows(SQLException.class, () -> singleConnectionDataSource.newConnectionFromDriver("invalidUser", "invalidPassword"));
    }

    @Test
    public void testIsWrapperForConnectionProxy() {
        Connection connectionProxy = getConnectionProxy(connection);

        assertTrue(singleConnectionDataSource.isWrapperFor(connectionProxy));
    }

    @Test
    public void testIsWrapperForConnection() {
        assertFalse(singleConnectionDataSource.isWrapperFor(connection));
    }

    @Test
    public void testEqualsConnectionProxy() {
        Connection connectionProxy1 = getConnectionProxy(connection);
        Connection connectionProxy2 = getConnectionProxy(connection);

        assertTrue(Objects.equals(connectionProxy1, connectionProxy2));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(Objects.equals(singleConnectionDataSource, null));
    }

    @Test
    public void testEqualsSameInstance() {
        assertTrue(Objects.equals(singleConnectionDataSource, singleConnectionDataSource));
    }

    @Test
    public void testEqualsDifferentInstance() {
        SingleConnectionDataSource anotherSingleConnectionDataSource = new SingleConnectionDataSource();

        assertFalse(Objects.equals(singleConnectionDataSource, anotherSingleConnectionDataSource));
    }

    @Test
    public void testHashCode() {
        int hashcode1 = singleConnectionDataSource.hashCode();
        SingleConnectionDataSource anotherSingleConnectionDataSource = new SingleConnectionDataSource();

        assertNotEquals(hashcode1, anotherSingleConnectionDataSource.hashCode());
    }

    @Test
    public void testHashCodeSameInstance() {
        int hashcode1 = singleConnectionDataSource.hashCode();
        singleConnectionDataSource = spy(singleConnectionDataSource);

        int hashcode2 = singleConnectionDataSource.hashCode();

        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        String expectedToString = "SingleConnectionDataSource{autoCommit=null, autoReconnect=true, eventPublisher=null, connection=null, initializerList=[]}";

        String actualToString = singleConnectionDataSource.toString();

        assertEquals(expectedToString, actualToString);
    }

    private Connection getConnectionProxy(Connection connection) {
        SingleConnectionDataSource singleConnectionDataSourceSpy = spy(singleConnectionDataSource);
        CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = new CloseIgnoreInvocationHandler(connection, singleConnectionDataSourceSpy.getLock());
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] {Connection.class}, closeIgnoreInvocationHandler);
    }
}