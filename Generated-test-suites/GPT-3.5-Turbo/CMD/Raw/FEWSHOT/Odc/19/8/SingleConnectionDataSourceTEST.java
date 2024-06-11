package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Lock mockLock = mock(ReentrantLock.class);
        Connection mockConnection = mock(Connection.class);
        EventPublisher mockEventPublisher = mock(EventPublisher.class);

        dataSource.setLock(mockLock);
        dataSource.setConnection(mockConnection);
        dataSource.setEventPublisher(mockEventPublisher);

        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(mockInitializer));
    }

    @Test
    public void testCloseConnection() {
        Connection mockConnection = mock(Connection.class);
        dataSource.setConnection(mockConnection);

        dataSource.close();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        dataSource.setAutoCommit(true);

        dataSource.prepareConnection(mockConnection);

        verify(mockConnection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy() {
        Lock mockLock = mock(ReentrantLock.class);
        Connection mockConnection = mock(Connection.class);

        dataSource.setLock(mockLock);
        dataSource.getConnectionProxy(mockConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        Connection mockConnection = mock(Connection.class);

        dataSource.addInitializer(mockInitializer);

        dataSource.initConnection(mockConnection);

        verify(mockInitializer, times(1)).init(mockConnection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection mockConnection = mock(Connection.class);

        dataSource.onConnectionCreated(mockConnection);

        // No exceptions expected
    }

    @Test
    public void testOnConnectionReset() {
        Connection mockConnection = mock(Connection.class);
        EventPublisher mockEventPublisher = mock(EventPublisher.class);

        dataSource.setEventPublisher(mockEventPublisher);
        dataSource.onConnectionReset(mockConnection);

        verify(mockEventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        dataSource.setConnection(null);
        dataSource.setLock(null);

        when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(mockConnection);

        Connection connection = dataSource.innerCreateConnection();

        assertNotNull(connection);
        assertNotNull(dataSource.getConnection());
        assertNotNull(dataSource.getLock());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        Connection mockConnection = mock(Connection.class);
        Lock mockLock = mock(ReentrantLock.class);

        SingleConnectionDataSource.CloseIgnoreInvocationHandler invocationHandler = 
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mockConnection, mockLock);

        Object result = invocationHandler.invoke(mockConnection, Connection.class.getMethod("isClosed"), null);

        assertFalse((Boolean) result);
    }
    
}