package com.oceanbase.odc.core.datasource.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test for SingleConnectionDataSource class
 */
class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private EventPublisher eventPublisher;
    private Connection mockConnection;
    private List<ConnectionInitializer> initializerList;
    private ConnectionInitializer mockInitializer;

    @BeforeEach
    void setup() {
        this.singleConnectionDataSource = new SingleConnectionDataSource();
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.mockConnection = Mockito.mock(Connection.class);
        this.initializerList = new LinkedList<>();
        this.mockInitializer = Mockito.mock(ConnectionInitializer.class);
        when(this.mockConnection.isValid(anyInt())).thenReturn(true);
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
    }

    @Test
    void testGetConnection() throws SQLException {
        singleConnectionDataSource.addInitializer(mockInitializer);
        when(mockInitializer.init(eq(mockConnection))).thenReturn(null);
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(mockInitializer).init(eq(mockConnection));
        verify(mockConnection, times(1)).isValid(anyInt());
    }

    @Test
    void testGetConnectionWithInvalidCredentials() throws SQLException {
        when(mockConnection.isValid(anyInt())).thenReturn(false);
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection());
    }

    @Test
    void testResetConnection() throws SQLException {
        singleConnectionDataSource.close();
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    void testAddInitializer() {
        singleConnectionDataSource.addInitializer(mockInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

    @Test
    void testClose() {
        singleConnectionDataSource.close();
        verify(mockConnection).close();
    }

    @Test
    void testInnerCreateConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(anyInt());
    }

    @Test
    void testGetConnectionProxy() throws SQLException {
        when(this.lock.tryLock(eq(anyLong()), eq(eq(TimeUnit.MILLISECONDS())))).thenReturn(true);
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(this.mockConnection);
        assertNotNull(connectionProxy);
        verify(this.lock, times(1)).unlock();
    }

    @Test
    void testConnectionOccupiedException() {
        when(this.lock.tryLock(eq(anyLong()), eq(eq(TimeUnit.MILLISECONDS())))).thenReturn(false);
        assertThrows(ConflictException.class, () -> singleConnectionDataSource.getConnectionProxy(this.mockConnection));
    }

    @Test
    void testInvalidUserNameOrPasswordException() throws SQLException {
        when(this.mockConnection.isClosed()).thenReturn(false);
        when(this.mockConnection.isValid(anyInt())).thenReturn(false);
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection(anyString(), anyString()));
    }

}