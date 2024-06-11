package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private ConnectionInitializer connectionInitializerMock;

    @Before
    public void setUp() {
        this.singleConnectionDataSource = new SingleConnectionDataSource();
        this.connectionMock = Mockito.mock(Connection.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.singleConnectionDataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        Connection actualConnection = this.singleConnectionDataSource.getConnection();
        assertEquals(this.connectionMock, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        this.singleConnectionDataSource.setUsername("invalid_username");
        this.singleConnectionDataSource.setPassword("invalid_password");
        this.singleConnectionDataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.singleConnectionDataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        doNothing().when(this.singleConnectionDataSource).onConnectionReset(this.connectionMock);
        this.singleConnectionDataSource.resetConnection();
        verify(this.singleConnectionDataSource, times(1)).onConnectionReset(this.connectionMock);
    }

    @Test
    public void testAddInitializer() {
        this.singleConnectionDataSource.addInitializer(this.connectionInitializerMock);
        verify(this.singleConnectionDataSource, times(1)).addInitializer(this.connectionInitializerMock);
    }

    @Test
    public void testClose() throws Exception {
        doNothing().when(this.connectionMock).close();
        this.singleConnectionDataSource.close();
        verify(this.connectionMock, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        this.singleConnectionDataSource.setAutoCommit(Boolean.TRUE);
        when(this.connectionMock.getAutoCommit()).thenReturn(Boolean.FALSE);
        this.singleConnectionDataSource.prepareConnection(this.connectionMock);
        verify(this.connectionMock, times(1)).setAutoCommit(Boolean.TRUE);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        this.singleConnectionDataSource.setLock(new ReentrantLock());
        this.singleConnectionDataSource.lock();
        when(this.singleConnectionDataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        try {
            this.singleConnectionDataSource.getConnection();
        } finally {
            this.singleConnectionDataSource.unlock();
        }
        this.singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(this.singleConnectionDataSource.tryLock()).thenReturn(true);
        Connection actualConnection = this.singleConnectionDataSource.getConnectionProxy(this.connectionMock);
        assertEquals(this.connectionMock, actualConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        this.singleConnectionDataSource.addInitializer(this.connectionInitializerMock);
        when(this.connectionMock.isValid(anyLong())).thenReturn(true);
        this.singleConnectionDataSource.initConnection(this.connectionMock);
        verify(this.connectionInitializerMock, times(1)).init(this.connectionMock);
    }

    @Test
    public void testOnConnectionCreated() {
        this.singleConnectionDataSource.onConnectionCreated(this.connectionMock);
        verify(this.singleConnectionDataSource, times(1)).onConnectionCreated(this.connectionMock);
    }

    @Test
    public void testOnConnectionReset() {
        when(this.eventPublisherMock.publishEvent(any(ConnectionResetEvent.class)))
                .thenReturn(new Object());
        this.singleConnectionDataSource.onConnectionReset(this.connectionMock);
        verify(this.eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection actualConnection = this.singleConnectionDataSource.innerCreateConnection();
        assertNotNull(actualConnection);
    }
}