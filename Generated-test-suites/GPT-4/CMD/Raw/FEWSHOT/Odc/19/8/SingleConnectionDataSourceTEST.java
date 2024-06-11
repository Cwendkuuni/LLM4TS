package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() {
        dataSource = null;
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_InvalidConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertTrue(Proxy.isProxyClass(connection.getClass()));
        connection.close();

        // It should be invalid after being closed
        assertFalse(connection.isValid(0));
    }

    @Test
    public void testGetConnection_ValidConnection() throws SQLException {
        dataSource.setAutoReconnect(true);

        Connection connection1 = dataSource.getConnection();
        assertFalse(connection1.isClosed()); 

        // Close connection
        connection1.close();

        // Should reopen a closed connection
        assertFalse(connection1.isClosed()); 

        Connection connection2 = dataSource.getConnection();
        assertFalse(connection2.isClosed()); 

        connection2.close();
        
        assertFalse(connection2.isClosed());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertFalse(connection.isClosed()); 

        dataSource.close();

        assertTrue(connection.isClosed()); 
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        dataSource.setAutoReconnect(true);
        assertFalse(connection1.isClosed()); 

        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();

        // Connection1 should be closed and connection2 is a new connection
        // thus, connection1 != connection2
        assertTrue(connection1.isClosed());
        assertFalse(connection2.isClosed());
        assertNotEquals(connection1, connection2);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());
    }

    @Test
    public void testLockResource() throws InterruptedException {
        dataSource.getConnection();
        ReentrantLock lock = new ReentrantLock();
        assertFalse(lock.tryLock(1000, java.util.concurrent.TimeUnit.MILLISECONDS));
    }
}