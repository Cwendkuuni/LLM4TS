package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "testUser";
        String password = "testPassword";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "invalidUser";
        String password = "invalidPassword";
        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotNull(newConnection);
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        boolean result = dataSource.tryLock();
        assertEquals(true, result);
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }
}