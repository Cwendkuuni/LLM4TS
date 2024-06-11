package com.oceanbase.odc.core.datasource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2021-11-09 18:32
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private Connection connection;
    private ConnectionInitializer connectionInitializerMock;
    private SingleConnectionDataSource dataSource;

    @BeforeEach
    public void setup() throws SQLException {
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        dataSource = new SingleConnectionDataSource(() -> connectionInitializerMock, eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Test successful getConnection call
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(null);
        Connection result = dataSource.getConnection();
        assertEquals(result, dataSource.getConnection()); // Check connection reuse
    }

    @Test
    public void testGetConnectionInvalidCredentials() throws SQLException {
        // Test getting connection with invalid credentials
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(connection);
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(connection);
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        // Test adding a ConnectionInitializer
        List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
        dataSource.addInitializer(connectionInitializerMock);
        assertEquals(1, initializerList.size());
    }

    @Test
    public void testClose() throws SQLException {
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(connection);
        dataSource.close();
        verify(connection).close();
    }

    @Test
    public void testAutoCommit() throws SQLException {
        // Test setting and getting autoCommit
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(connection);
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.isAutoCommit());
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.isAutoCommit());
    }

    @Test
    public void testEventPublisher() throws SQLException {
        // Test event publishing
        when(connectionInitializerMock.init(any(Connection.class))).thenReturn(connection);
        ConnectionResetEvent event = new ConnectionResetEvent(connection);
        doNothing().when(eventPublisher).publishEvent(event);
        dataSource.resetConnection();
        verify(eventPublisher, times(1)).publishEvent(event);
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        Connection result = dataSource.getConnectionProxy(connection);
        assertEquals(connection, result);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> connection, eventPublisher);
        Connection result = dataSource.innerCreateConnection();
        assertEquals(connection, result);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> connection, eventPublisher);
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        when(connectionInitializerMock.init(connection)).thenReturn(null);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> connectionInitializerMock, eventPublisher);
        dataSource.initConnection(connection);
        verify(connectionInitializerMock, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        dataSource.onConnectionCreated(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionCreatedEvent.class));
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testTryLockException() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testGetConnectionProxyInvalidConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(() -> {}, eventPublisher);
        assertThrows(SQLException.class, () -> dataSource.getConnectionProxy(connection));
    }

}