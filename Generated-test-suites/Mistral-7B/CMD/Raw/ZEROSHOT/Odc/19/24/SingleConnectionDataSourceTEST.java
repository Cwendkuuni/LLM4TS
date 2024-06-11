package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.ConnectionEventPublisherMock;

/**
 * JUnit test file for SingleConnectionDataSource class
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ConnectionEventPublisherMock eventPublisherMock;

    @Before
    public void setUp() {
        this.eventPublisherMock = new ConnectionEventPublisherMock();
        this.eventPublisher = this.eventPublisherMock;
        this.dataSource = new SingleConnectionDataSource(true) {
            @Override
            public EventPublisher getEventPublisher() {
                return this.eventPublisher;
            }
        };
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        try {
            this.connection = DriverManager.getConnection("jdbc:mock:oceanbase:testdb", "testuser", "testpassword");
        } catch (SQLException e) {
            fail("Failed to create mock connection");
        }
        reset(this.dataSource);
    }

    @After
    public void tearDown() throws Exception {
        this.dataSource.close();
        closeQuietly(this.connection);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        Connection resultConnection = this.dataSource.getConnection();
        assertEquals(this.connection, resultConnection);
        verify(this.dataSource, times(1)).innerCreateConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        this.dataSource.getConnection("invaliduser", "invalidpassword");
        fail("Should have thrown SQLException");
        verify(this.dataSource, atLeastOnce()).getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection resultConnection = this.dataSource.getConnectionProxy(this.connection);
        assertEquals(this.connection, resultConnection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhenLocked() throws SQLException {
        this.dataSource.setLock(Mockito.mock(Lock.class));
        when(this.dataSource.getLock().tryLock(anyLong(), anyLong())).thenReturn(false);
        this.dataSource.getConnection();
        fail("Should have thrown ConflictException");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection resetConnection = mock(Connection.class);
        when(this.dataSource.innerCreateConnection()).thenReturn(resetConnection);
        this.dataSource.resetConnection();
        verify(this.dataSource).innerCreateConnection();
        verify(this.eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(mock(ConnectionInitializer.class));
        verify(this.dataSource, times(1)).addInitializer(any(ConnectionInitializer.class));
    }

    @Test
    public void testClose() throws SQLException {
        this.dataSource.close();
        verify(this.connection).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(this.connection.isAutoCommit()).thenReturn(false);
        this.dataSource.prepareConnection(this.connection);
        verify(this.connection, times(1)).setAutoCommit(true);

        when(this.connection.isAutoCommit()).thenReturn(true);
        this.dataSource.prepareConnection(this.connection);
        verify(this.connection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testTryLock() throws InterruptedException, SQLException {
        this.dataSource.setLock(Mockito.mock(Lock.class));
        when(this.dataSource.getLock().tryLock(anyLong(), anyLong())).thenReturn(true);
        assertTrue(this.dataSource.tryLock());

        when(this.dataSource.getLock().tryLock(anyLong(), anyLong())).thenReturn(false);
        assertFalse(this.dataSource.tryLock());
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionWhenFailed() throws SQLException {
        when(this.dataSource.newConnectionFromDriver(anyString(), anyString())).thenThrow(new SQLException());
        this.dataSource.getConnection();
        fail("Should have thrown SQLException");
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        this.dataSource = spy(this.dataSource);
        Connection connection = mock(Connection.class);
        this.dataSource.onConnectionCreated(connection);
        verify(this.dataSource).getConnectionProxy(connection);
        verify(this.dataSource).initConnection(connection);
    }
}

class ConnectionEventPublisherMock implements EventPublisher {

    @Override
    public void publishEvent(Object event) {
        // empty implementation
    }
}