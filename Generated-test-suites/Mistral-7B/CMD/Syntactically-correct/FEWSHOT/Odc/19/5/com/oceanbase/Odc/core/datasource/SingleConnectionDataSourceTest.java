package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private Lock lockMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        connectionMock = mock(Connection.class);
        eventPublisherMock = mock(EventPublisher.class);
        lockMock = Mockito.mock(Lock.class);
        initializerList = new ArrayList<>();

        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connectionMock;
            }
        };
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(eventPublisherMock);
        dataSource.setLock(lockMock);
        dataSource.setInitializerList(initializerList);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();
        assertSame(connectionMock, connection);
        verify(connectionMock, times(1)).isValid(Mockito.anyInt());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(false);

        dataSource.setUsername("username");
        dataSource.setPassword("password");

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();

        verify(connectionMock, times(1)).close();
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());

        Connection newConnection = innerCreateConnection();
        assertSame(newConnection, dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializerMock);

        assertTrue(dataSource.getInitializerList().contains(initializerMock));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullInitializer() {
        dataSource.addInitializer(null);
    }

    @Test
    public void testClose() throws Exception {
        dataSource.close();

        verify(connectionMock, times(1)).close();
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLockTimeout() {
        assertFalse(dataSource.tryLock(1));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = innerCreateConnection();

        Connection proxiedConnection = dataSource.getConnectionProxy(connection);

        assertSame(connection, proxiedConnection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(connectionMock.getAutoCommit()).thenReturn(Boolean.FALSE);

        dataSource.prepareConnection(connectionMock);

        assertTrue(connectionMock.isAutoCommit());
    }

    @Test
    public void testPrepareConnectionNoChange() throws SQLException {
        when(connectionMock.getAutoCommit()).thenReturn(Boolean.TRUE);

        dataSource.prepareConnection(connectionMock);

        assertTrue(connectionMock.isAutoCommit());
    }

    @Test
    public void testPrepareConnectionNullAutoCommit() throws SQLException {
        dataSource.setAutoCommit(null);

        try {
            dataSource.prepareConnection(connectionMock);
        } catch (SQLException e) {
            assertEquals("autoCommit is null", e.getMessage());
        }
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializerMock);

        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);

        dataSource.initConnection(connection);

        verify(initializerMock, times(1)).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testInitConnectionInvalidConnection() throws SQLException {
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(false);

        dataSource.initConnection(connectionMock);
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);

        dataSource.onConnectionReset(connection);

        verify(eventPublisherMock, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    private Connection innerCreateConnection() throws SQLException {
        Connection connection = newConnectionFromDriver(getUsername(), getPassword());

        dataSource.initConnection(connection);
        dataSource.onConnectionCreated(connection);

        return connection;
    }
}