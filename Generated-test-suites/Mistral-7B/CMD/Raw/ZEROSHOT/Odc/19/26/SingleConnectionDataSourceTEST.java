package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

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

    private SingleConnectionDataSource singleConnectionDataSource;

    private static final long TIME_OUT_MILLIS = 10 * 1000;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
        Connection returnedConnection = singleConnectionDataSource.getConnection();
        assertEquals(connection, returnedConnection);
        verify(singleConnectionDataSource).prepareConnection(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        singleConnectionDataSource.setUsername("invalid_username");
        singleConnectionDataSource.setPassword("invalid_password");
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
        singleConnectionDataSource.resetConnection();
        verify(singleConnectionDataSource).close();
        verify(singleConnectionDataSource).onConnectionReset(connection);
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        verify(singleConnectionDataSource).initializerList.add(connectionInitializer);
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.close();
        verify(connection).close();
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        Lock lock = new ReentrantLock();
        assertTrue(singleConnectionDataSource.tryLock().get());
        assertFalse(singleConnectionDataSource.tryLock().get());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setLock(new ReentrantLock());
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.getConnection();
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        singleConnectionDataSource.setConnection(connection);
        Connection returnedConnection = singleConnectionDataSource.getConnectionProxy(connection);
        assertEquals(connection, returnedConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
        singleConnectionDataSource.initConnection(connection);
        verify(singleConnectionDataSource).initConnection(connection);
        for (ConnectionInitializer initializer : singleConnectionDataSource.initializerList) {
            verify(initializer).init(connection);
        }
        singleConnectionDataSource.onConnectionCreated(connection);
        verify(singleConnectionDataSource).onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() {
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        when(singleConnectionDataSource.getConnection()).thenReturn(connection);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
    }
}