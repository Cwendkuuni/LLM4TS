
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock private Connection connection;
    @Mock private ConnectionInitializer connectionInitializer;
    @Mock private EventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws Exception {
        singleConnectionDataSource.setConnection(connection);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        assertNotNull(singleConnectionDataSource.getConnection());
        assertTrue(singleConnectionDataSource.getConnection().isWrapperFor(Connection.class));
        verify(connection, times(2)).isClosed();
        verify(connection, times(2)).isValid(anyInt());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedAndInvalid() throws Exception {
        singleConnectionDataSource.setConnection(connection);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws Exception {
        singleConnectionDataSource.getConnection("admin", "admin123");
    }

    @Test
    public void testResetConnection() throws Exception {
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertTrue(singleConnectionDataSource.getInitializerList().contains(connectionInitializer));
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.close();
        verify(connection, times(1)).close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testPrepareConnectionWhenAutoCommitNonNull() throws Exception {
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.setConnection(connection);

        when(connection.getAutoCommit()).thenReturn(false);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionWhenConnectionIsNotNull() throws Exception {
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.innerCreateConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = 
        new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());

        assertEquals(connection, closeIgnoreInvocationHandler.target);
        assertTrue((Boolean) closeIgnoreInvocationHandler.invoke(connection, Connection.class.getMethod("isWrapperFor", Class.class), new Class[]{Connection.class}));
    }

    @After
    public void tearDown() {
        singleConnectionDataSource.close();
    }
}

