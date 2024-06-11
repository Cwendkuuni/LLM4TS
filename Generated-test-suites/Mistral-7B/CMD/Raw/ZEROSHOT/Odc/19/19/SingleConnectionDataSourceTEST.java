package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-07 16:20
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private List<ConnectionInitializer> initializers;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connectionMock = mock(Connection.class);
        initializers = new ArrayList<>();
        eventPublisherMock = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        when(connectionMock.isValid(anyLong())).thenReturn(true);

        Connection result = dataSource.getConnection();
        assertSame(connectionMock, result);

        verify(connectionMock).isValid(anyLong());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        when(connectionMock.isValid(anyLong())).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullConnection() throws SQLException {
        dataSource.connection = null;
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        dataSource.resetConnection();

        verify(dataSource).close();
        verify(dataSource).connection = null;
        verify(dataSource).lock = null;
        verify(connectionMock).close();

        dataSource.getConnection();
        assertEquals(connectionMock, dataSource.connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializerMock);

        ArgumentCaptor<ConnectionInitializer> initializerCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        verify(dataSource).initializerList.add(initializerCaptor.capture());
        assertEquals(initializerMock, initializerCaptor.getValue());
    }

    @Test
    public void testClose() throws SQLException {
        doNothing().when(connectionMock).close();

        dataSource.close();

        verify(connectionMock).close();
    }

    @Test
    public void testInitConnection() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);

        dataSource.initConnection(connectionMock);

        verify(dataSource).prepareConnection(connectionMock);
        verify(dataSource).initConnection(connectionMock);
        verify(dataSource).onConnectionCreated(connectionMock);
    }

    @Test
    public void testTryLock() {
        Lock lock = new ReentrantLock();

        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithTimeout() throws SQLException {
        Lock lock = new ReentrantLock();
        dataSource.lock = lock;
        dataSource.connection = connectionMock;
        when(dataSource.tryLock()).thenReturn(false);

        dataSource.getConnectionProxy(connectionMock);
    }

    @Test
    public void testOnConnectionReset() {
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);

        dataSource.onConnectionReset(connectionMock);

        verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }
}