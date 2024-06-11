package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherMock;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionOccupiedException;

public class SingleConnectionDataSourceTest {
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private SingleConnectionDataSource singleConnectionDataSource;
    private List<ConnectionInitializer> initializerList;
    private ConnectionInitializer initializerMock;

    @Before
    public void setUp() throws SQLException {
        this.connectionMock = Mockito.mock(Connection.class);
        this.eventPublisherMock = new EventPublisherMock();
        this.singleConnectionDataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return this.connectionMock;
            }
        };
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisherMock);
        this.initializerList = new ArrayList<>();
        this.initializerMock = Mockito.mock(ConnectionInitializer.class);
        this.singleConnectionDataSource.addInitializer(this.initializerMock);
        when(this.connectionMock.isValid(anyInt())).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(false);
        when(this.connectionMock.getAutoCommit()).thenReturn(null);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.connectionMock.getAutoCommit()).thenReturn(true);
        Connection connection = this.singleConnectionDataSource.getConnection();
        assertEquals(this.connectionMock, connection);
        verify(this.connectionMock).isValid(anyInt());
        verifyZeroInteractions(this.initializerMock);
        verifyNoMoreInteractions(this.connectionMock);
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(this.connectionMock.isValid(anyInt())).thenReturn(false);
        this.singleConnectionDataSource.getConnection();
        verify(this.connectionMock).isValid(anyInt());
        verify(this.connectionMock).close();
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullConnection() throws SQLException {
        this.singleConnectionDataSource.connection = null;
        this.singleConnectionDataSource.getConnection();
        verify(this.connectionMock).isValid(anyInt());
        verify(this.connectionMock).close();
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWithDefaultAutoCommit() throws SQLException {
        Connection connection = this.singleConnectionDataSource.getConnection();
        assertEquals(this.connectionMock, connection);
        verify(this.connectionMock).isValid(anyInt());
        verifyZeroInteractions(this.initializerMock);
        verifyNoMoreInteractions(this.connectionMock);
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWithCustomAutoCommit() throws SQLException {
        this.singleConnectionDataSource.setAutoCommit(true);
        Connection connection = this.singleConnectionDataSource.getConnection();
        assertEquals(this.connectionMock, connection);
        verify(this.connectionMock).isValid(anyInt());
        verify(this.connectionMock).setAutoCommit(true);
        verifyZeroInteractions(this.initializerMock);
        verifyNoMoreInteractions(this.connectionMock);
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = this.singleConnectionDataSource.getConnection();
        this.singleConnectionDataSource.resetConnection();
        verify(this.connectionMock).isValid(anyInt());
        verify(this.connectionMock).close();
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
        assertNull(this.singleConnectionDataSource.connection);
        assertNull(this.singleConnectionDataSource.lock);
    }

    @Test(expected = ConflictException.class)
    public void testResetConnectionWhileConnectionIsOccupied() throws SQLException {
        this.singleConnectionDataSource.resetConnection();
        throw new InterruptedException();
        // Connection will be reset in the test method, but the thread will be interrupted before that
        // This will cause the test to fail with a ConflictException, as the connection is occupied
    }

    @Test
    public void testAddInitializer() {
        this.singleConnectionDataSource.addInitializer(this.initializerMock);
        verify(this.initializerMock).init(this.connectionMock);
        verifyZeroInteractions(this.eventPublisherMock);
    }

    @Test
    public void testClose() throws SQLException {
        this.singleConnectionDataSource.close();
        verify(this.connectionMock).close();
        verifyZeroInteractions(this.initializerMock);
        verifyZeroInteractions(this.eventPublisherMock);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.singleConnectionDataSource.initConnection(connection);
        verify(this.initializerMock).init(connection);
        verify(this.connectionMock).prepareCall("initialize");
        verifyNoMoreInteractions(this.connectionMock);
        verifyZeroInteractions(this.eventPublisherMock);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.singleConnectionDataSource.onConnectionCreated(connection);
        verifyZeroInteractions(this.initializerMock);
        verifyZeroInteractions(this.eventPublisherMock);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.singleConnectionDataSource.onConnectionReset(connection);
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() throws Exception {
        this.singleConnectionDataSource.close();
    }
}