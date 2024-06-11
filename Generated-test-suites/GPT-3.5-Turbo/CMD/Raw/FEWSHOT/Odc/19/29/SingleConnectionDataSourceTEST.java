package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        connection = mock(Connection.class);
        lock = new ReentrantLock();
    }

    @Test
    public void testCreateSingleConnectionDataSource() {
        assertNotNull(dataSource);
        assertTrue(dataSource.isAutoReconnect());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        dataSource.setConnection(connection);
        assertEquals(connection, dataSource.getConnection());
        verify(connection, times(1)).isValid(anyInt());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionThrowsSQLException() throws SQLException {
        Connection closedConnection = mock(Connection.class);
        when(closedConnection.isValid(anyInt())).thenReturn(false);
        dataSource.setConnection(closedConnection);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertNull(dataSource.getConnection("user", "pass"));
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        assertNotNull(dataSource.getConnection("user", "pass"));
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connection);
        assertNotNull(dataSource.getConnection());
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
        dataSource.addInitializer(null);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(connection);
        assertNotNull(dataSource.getConnection());
        dataSource.close();
        assertNull(dataSource.getConnection());
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(connection.getAutoCommit()).thenReturn(true);
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
        verify(lock, times(1)).tryLock(anyLong(), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
        dataSource.setConnection(connection);
        assertThrows(IllegalStateException.class, () -> dataSource.innerCreateConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        Lock mockLock = mock(Lock.class);
        Connection mockConnection = mock(Connection.class);
        CloseIgnoreInvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(mockConnection, mockLock);
        
        invocationHandler.invoke(mockConnection, Connection.class.getMethod("close"), null);
        verify(mockLock, times(1)).unlock();
        
        assertFalse((boolean) invocationHandler.invoke(mockConnection, Object.class.getMethod("equals", Object.class), new Object[] {mockConnection}));
        assertEquals(System.identityHashCode(mockConnection), invocationHandler.invoke(mockConnection, Object.class.getMethod("hashCode"), null));
        assertNull(invocationHandler.invoke(mockConnection, Connection.class.getMethod("isClosed"), null));
    }
}