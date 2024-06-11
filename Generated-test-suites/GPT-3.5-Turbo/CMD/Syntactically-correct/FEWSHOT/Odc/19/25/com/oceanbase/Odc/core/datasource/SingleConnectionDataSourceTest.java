package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testConstructor() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        assertNotNull(ds);
        assertFalse(ds.getAutoReconnect());
        assertNull(ds.getAutoCommit());

        SingleConnectionDataSource ds2 = new SingleConnectionDataSource(true);
        assertNotNull(ds2);
        assertTrue(ds2.getAutoReconnect());
        assertNull(ds2.getAutoCommit());
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        Connection connection = ds.getConnection();
        assertNotNull(connection);

        ds.setConnection(mock(Connection.class));
        Connection connection2 = ds.getConnection();
        assertNotNull(connection2);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.resetConnection();
        // Verify that log.info was called
        // Verify that close method was called on connection
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        ds.addInitializer(initializer);
        assertEquals(1, ds.getInitializerList().size());
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setConnection(mock(Connection.class));
        ds.close();
        // Verify that connection.close() was called
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        ds.setConnection(connection);
        ds.prepareConnection(connection);
        // Verify that autoCommit was set on connection
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        assertTrue(ds.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        ds.setConnection(connection);
        assertNotNull(ds.getConnectionProxy(connection));
    }

    // Add more test cases as needed for other methods in SingleConnectionDataSource

}