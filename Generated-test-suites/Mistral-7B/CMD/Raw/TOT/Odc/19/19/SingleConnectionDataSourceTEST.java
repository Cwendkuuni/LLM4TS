package com.oceanbase.odc.core.datasource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.base.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private EventPublisher eventPublisher;
    private SingleConnectionDataSource dataSource;
    private List<ConnectionInitializer> initializerList = Collections.singletonList(connectionInitializer);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setAutoReconnect(true);
        dataSource.setInitializerList(initializerList);
    }

    @Test
    public void testGetConnection_ValidCredentials_ReturnsNonNullConnection() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection actual = dataSource.getConnection();
        assertNotNull(actual);
        assertEquals(dataSource.getConnection(), actual);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnection_InvalidCredentials_ThrowsSQLException() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnection_ConnectionReset_ResetsConnectionAndPublishEvent() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);
        when(connection.close()).thenThrow(SQLException.class);

        dataSource.getConnection();
        verify(eventPublisher).publishEvent(argThat(e -> e instanceof ConnectionResetEvent));
    }

    @Test
    public void testGetConnectionProxy_ClosesConnection_ReturnsProxyConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection actual = dataSource.getConnectionProxy(connection);
        assertNotNull(actual);
        assertEquals(connection, actual);

        dataSource.lock.unlock();
        verify(connection).close();
    }

    @Test
    public void testGetConnection_ConnectionAlreadyOpen_ReturnsSameConnection() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        assertEquals(connection1, connection2);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection_ThrowsSQLException_DoesNotResetConnection() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(true);

        assertThrows(SQLException.class, () -> dataSource.resetConnection());
        assertEquals(null, dataSource.connection);
    }

    @Test
    public void testResetConnection_ConnectionIsNotClosed_ResetsConnectionAndPublishEvent() throws SQLException {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);

        dataSource.resetConnection();
        verify(eventPublisher).publishEvent(argThat(e -> e instanceof ConnectionResetEvent));
        assertNull(dataSource.connection);
    }

    @Test
    public void testInnerCreateConnection_ReturnsConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection actual = dataSource.innerCreateConnection();
        assertNotNull(actual);
        assertEquals(newConnectionFromDriver(dataSource.getUsername(), dataSource.getPassword()), actual);
    }

    @Test
    public void testOnConnectionCreated_LogsInfoMessage() throws SQLException {
        Connection connection = newConnectionFromDriver(dataSource.getUsername(), dataSource.getPassword());

        dataSource.onConnectionCreated(connection);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger).info(logCaptor.capture());
        assertTrue(logCaptor.getValue().contains("Connection is created"));
    }

    @Test
    public void testOnConnectionReset_LogsInfoMessageAndPublishesEvent() throws SQLException {
        Connection connection = newConnectionFromDriver(dataSource.getUsername(), dataSource.getPassword());

        dataSource.onConnectionReset(connection);

        ArgumentCaptor<ConnectionResetEvent> eventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(connection, eventCaptor.getValue().getConnection());
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger).info(logCaptor.capture());
        assertTrue(logCaptor.getValue().contains("The connection will be reset soon"));
    }
}