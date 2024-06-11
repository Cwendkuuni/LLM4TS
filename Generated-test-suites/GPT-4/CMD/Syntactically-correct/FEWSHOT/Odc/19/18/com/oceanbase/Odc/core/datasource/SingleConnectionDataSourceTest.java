package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;

import org.junit.Test;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection_firstTime() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        assertTrue(conn != null);
    }

    @Test
    public void testGetConnection_secondTime() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection firstConn = dataSource.getConnection();
        Connection secondConn = dataSource.getConnection();
        assertEquals(firstConn, secondConn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_differentCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        dataSource.getConnection("another_user", "another_pass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection firstConn = dataSource.getConnection();
        dataSource.resetConnection();
        Connection secondConn = dataSource.getConnection();
        assertFalse(firstConn.equals(secondConn));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testGetConnection_locked() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        conn.close();
    }

    @Test
    public void testAddInitializer() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Connection conn = dataSource.getConnection();
        Mockito.verify(initializer).init(conn);
    }

    @Test(expected = SQLException.class)
    public void testAddInitializer_failedInit() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        Mockito.doThrow(new SQLException()).when(initializer).init(Mockito.any());
        dataSource.addInitializer(initializer);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_closed() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.getConnection();
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        dataSource.close();
        assertTrue(conn.isClosed());
    }

}