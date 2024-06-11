package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testAutoCommitSetting() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        Lock lock = mock(Lock.class);
        when(lock.tryLock(anyLong(), any())).thenReturn(true);
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }

    @Test(expected = SQLException.class)
    public void testInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalid", "invalid");
    }

    @Test(expected = RuntimeException.class)
    public void testTryLockTimeout() {
        dataSource.setTimeOutMillis(0);
        dataSource.tryLock();
    }
}