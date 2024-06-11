package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        dataSource = null;
    }

    @Test
    public void testConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getAutoCommit());
        assertFalse(dataSource.isAutoReconnect());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
        assertNull(dataSource.getInitializerList());
        assertNull(dataSource.getTimeOutMillis());
    }

    @Test
    public void testConstructorWithAutoReconnect() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertTrue(dataSource.isAutoReconnect());
        assertNull(dataSource.getAutoCommit());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
        assertNull(dataSource.getInitializerList());
        assertNull(dataSource.getTimeOutMillis());
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isValid(anyLong())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());
        assertSame(connection, getConnectionProxy(connection));
        verify(connection).isValid(eq(getLoginTimeout()));
    }

    @Test
    public void testGetConnectionInvalidCredentials() throws SQLException {
        when(connection.isValid(anyLong())).thenReturn(false);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionNullConnection() throws SQLException {
        dataSource.setConnection(null);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertFalse(connectionProxy.isClosed());
    }

    @Test
    public void testGetConnectionProxyClosedConnection() {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNull(connectionProxy);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connection);
        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());

        try (Connection newConnection = newConnectionFromDriver(dataSource.getUsername(), dataSource.getPassword())) {
            dataSource.onConnectionReset(newConnection);
            verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
        }
    }

    @Test
    public void testAddInitializer() {
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        initializerList.add(connectionInitializer);

        dataSource.addInitializer(connectionInitializer);

        assertEquals(initializerList, dataSource.getInitializerList());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testCloseTwice() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.close();
        dataSource.close();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(connection.isValid(anyLong())).thenReturn(true);

        Connection innerConnection = dataSource.innerCreateConnection();
        assertSame(innerConnection, dataSource.getConnection());
        assertSame(innerConnection, getConnectionProxy(innerConnection));
        verify(dataSource).onConnectionCreated(innerConnection);
    }

    @Test
    public void testTryLock() {
        ReentrantLock lock = new ReentrantLock();

        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLockTimeout() {
        ReentrantLock lock = new ReentrantLock();

        assertFalse(dataSource.tryLock(50, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        dataSource.setLock(mock(Lock.class));
        when(dataSource.tryLock()).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupiedLonger() throws SQLException {
        dataSource.setLock(mock(Lock.class));
        when(dataSource.tryLock(1, TimeUnit.SECONDS)).thenReturn(false);

        dataSource.getConnection();
    }

    @Test
    public void testInnerCreateConnectionNullConnection() throws SQLException {
        dataSource.setConnection(null);

        assertThrows(SQLException.class, () -> dataSource.innerCreateConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = Boolean.TRUE;

        dataSource.prepareConnection(connection);
        assertTrue(connection.isAutoCommit());
        assertEquals(autoCommit, connection.getAutoCommit());
    }

    @Test
    public void testPrepareConnectionFalseAutoCommit() throws SQLException {
        Boolean autoCommit = Boolean.FALSE;

        dataSource.prepareConnection(connection);
        assertFalse(connection.isAutoCommit());
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testPrepareConnectionTrueAutoCommit() throws SQLException {
        dataSource.prepareConnection(connection);
        assertTrue(connection.isAutoCommit());
        assertTrue(connection.getAutoCommit());
    }

}