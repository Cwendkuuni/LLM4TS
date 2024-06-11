package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.common.event.MockEventPublisher;

/**
 * Test class for {@link SingleConnectionDataSource}
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private ConnectionInitializer connectionInitializerMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() {
        this.initializerList = new LinkedList<>();
        this.connectionMock = Mockito.mock(Connection.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);

        this.dataSource = new SingleConnectionDataSource() {{
            this.eventPublisher = eventPublisherMock;
            this.initializerList = initializerList;
        }};
    }

    @Test
    public void testGetConnection_ReturnsExpectedConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();

        assertEquals(connectionMock, connection);
        verify(connectionMock, times(1)).isValid(anyLong());
        verify(connectionMock, never()).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ThrowsExceptionWhenConnectionIsClosedOrInvalid() throws SQLException {
        Connection connection = dataSource.getConnection();
        reset(connectionMock);

        when(connectionMock.isValid(anyLong())).thenReturn(false);

        Connection connection2 = dataSource.getConnection();

        fail();

        verify(connectionMock, times(1)).isValid(anyLong());
        verify(connectionMock, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ThrowsExceptionWhenInvalidUsernameOrPassword() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);

        dataSource.setUsername("test");
        dataSource.setPassword("test");

        try {
            Connection connection = dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }

        verifyNoMoreInteractions(connectionMock);
    }

    @Test
    public void testResetConnection_ResetsConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        dataSource.resetConnection();

        verify(connectionMock, times(1)).close();
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
        assertNull(dataSource.getConnection());
        assertEquals(null, dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testAddInitializer_AddsConnectionInitializer() {
        dataSource.addInitializer(connectionInitializerMock);

        assertEquals(connectionInitializerMock, dataSource.getInitializerList().get(0));
    }

    @Test
    public void testClose_ClosesConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();

        dataSource.close();

        verify(connectionMock, times(1)).close();
    }

    @Test
    public void testPrepareConnection_SetsAutoCommit() throws SQLException {
        Connection connection = dataSource.getConnection();

        dataSource.prepareConnection(connection);

        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testTryLock_LocksConnectionIfAvailable() {
        when(lock.tryLock(anyLong(), anyLong(), anyInt())).thenReturn(true);

        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLock_ThrowsExceptionWhenConnectionIsAlreadyLocked() {
        when(lock.tryLock(anyLong(), anyLong(), anyInt())).thenReturn(false);

        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy_ReturnsConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertEquals(dataSource.getConnectionProxy(connection), connection);
    }

    @Test
    public void testInitConnection_InitializesConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.initConnection(connection);

        verify(connectionMock, times(size(dataSource.getInitializerList()))).init(connection);
        verify(connectionMock, times(1)).commit();
    }

    @Test
    public void testOnConnectionCreated_CallsOnConnectionCreated() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.onConnectionCreated(connection);

        verify(connectionMock, times(1)).close();
        verify(connectionMock, times(1)).commit();
        verify(connectionMock, times(1)).setAutoCommit(true);
    }

    @Test
    public void testOnConnectionReset_PublishEventWhenEventPublisherIsSet() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        MockEventPublisher eventPublisher = new MockEventPublisher();
        dataSource.setEventPublisher(eventPublisher);

        dataSource.onConnectionReset(connection);

        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection_CreatesConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();

        verify(connectionMock, times(1)).createStatement();
        verify(connectionMock, times(1)).setAutoCommit(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnection_ThrowsExceptionWhenConnectionIsNotNull() throws SQLException {
        Connection connection = dataSource.getConnection();

        dataSource.innerCreateConnection();

        fail();
    }

    @Test
    public void testCloseIgnoreInvocationHandler_SuppressesCloseCallOnConnection() {
        Connection connection = dataSource.getConnection();

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());

        handler.invoke(handler, closeMethod(), new Object[0]);

        assertEquals(connection, dataSource.getConnection());
    }

    private Method closeMethod() {
        try {
            return connectionMock.getClass().getMethod("close");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void teardown() {
        Mockito.reset(connectionMock, eventPublisherMock, connectionInitializerMock);
    }
}