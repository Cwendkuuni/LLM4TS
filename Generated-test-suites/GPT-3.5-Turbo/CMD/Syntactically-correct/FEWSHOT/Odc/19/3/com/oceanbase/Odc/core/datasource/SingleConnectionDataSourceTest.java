package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

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
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        dataSource.close();
        // No exceptions should be thrown
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testTryLock() {
        boolean locked = dataSource.tryLock();
        assertTrue(locked);
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        assertNotNull(dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
        // No exceptions should be thrown
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setEventPublisher(mock(EventPublisher.class));
        dataSource.onConnectionReset(mock(Connection.class));
        // No exceptions should be thrown
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        dataSource.innerCreateConnection();
        // Testing private class by invoking public methods from SingleConnectionDataSource
    }
}