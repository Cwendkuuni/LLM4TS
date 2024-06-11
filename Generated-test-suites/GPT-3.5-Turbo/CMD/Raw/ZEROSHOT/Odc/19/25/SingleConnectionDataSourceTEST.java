package com.oceanbase.odc.core.datasource;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertThrows(SQLException.class, () -> dataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.resetConnection();
        assertThrows(SQLException.class, connection::getAutoCommit);
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        verify(initializer, times(1)).init(any(Connection.class));
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, connection::getAutoCommit);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Connection connection = dataSource.getConnection();
        verify(initializer, times(1)).init(connection);
    }

}