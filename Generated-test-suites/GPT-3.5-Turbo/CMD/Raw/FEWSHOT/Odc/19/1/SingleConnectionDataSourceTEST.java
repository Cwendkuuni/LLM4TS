package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
        
        dataSource.addInitializer(mock(ConnectionInitializer.class));
        Connection initializedConnection = dataSource.getConnection();
        assertNotNull(initializedConnection);
        assertTrue(initializedConnection.isValid(0));
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
        
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_username", "invalid_password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.getConnection(); // Initialize connection
        
        dataSource.resetConnection();
        
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection(); // Initialize connection
        
        dataSource.close();
        
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        
        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.innerCreateConnection();
        
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Lock lock = mock(Lock.class);
        dataSource.setLock(lock);
        
        when(lock.tryLock(dataSource.getTimeOutMillis(), dataSource.getTimeUnit())).thenReturn(true);
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();
        dataSource.setLock(lock);
        
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mock(Connection.class), mock(Lock.class));
        
        assertNotNull(handler);
    }
}