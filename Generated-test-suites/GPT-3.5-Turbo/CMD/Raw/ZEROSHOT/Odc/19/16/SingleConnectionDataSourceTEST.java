package com.oceanbase.odc.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.prepareConnection(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testOnConnectionCreated() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
        verify(connection, times(1)).isClosed();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
    }
}