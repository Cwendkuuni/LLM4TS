package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test");
        dataSource.setPassword("password");
        dataSource.setAutoCommit(true);
        dataSource.setAutoReconnect(false);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(dataSource.getConnection(), connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPwd() throws SQLException {
        Connection connection = dataSource.getConnection("test", "password");
        assertNotNull(connection);
        assertEquals(dataSource.getConnection(), connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotNull(newConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernamePwd() throws SQLException {
        dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.prepareConnection(connection);
        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

}