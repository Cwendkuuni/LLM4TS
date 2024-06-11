package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    private List<ConnectionInitializer> initializerList;

    private final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(eventPublisher);
        initializerList = new LinkedList<>();
        dataSource.setInitializerList(initializerList);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);
        when(connection.getAutoCommit()).thenReturn(null);

        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(false);
        when(connection.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernameOrPassword() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);
        when(connection.getUsername()).thenReturn("invalid_username");
        when(connection.getPassword()).thenReturn("invalid_password");
        when(connection.getAutoCommit()).thenReturn(null);

        dataSource.getConnection("username", "password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        dataSource.setConnection(connection);
        dataSource.close();
        dataSource.resetConnection();

        verify(connection).close();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
        assertNull(dataSource.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionLocked() throws SQLException, InterruptedException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        dataSource.setConnection(connection);
        dataSource.lock.lock();

        dataSource.getConnection();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);
        assertTrue(initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        dataSource.setConnection(connection);
        dataSource.close();

        verify(connection).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLock() throws InterruptedException {
        ReentrantLock lock = mock(ReentrantLock.class);
        assertFalse(dataSource.tryLock().isHeldByCurrentThread());
        dataSource.lock.lock();
        assertTrue(dataSource.tryLock().isHeldByCurrentThread());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        Connection result = dataSource.getConnectionProxy(connection);
        assertEquals(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionThrowsException() throws SQLException {
        when(newConnectionFromDriver(anyString(), anyString())).thenThrow(new SQLException());

        dataSource.innerCreateConnection();
    }

    @After
    public void tearDown() {
        Mockito.reset(connection, eventPublisher, connectionInitializer);
        dataSource.setConnection(null);
        dataSource.lock = null;
    }
}