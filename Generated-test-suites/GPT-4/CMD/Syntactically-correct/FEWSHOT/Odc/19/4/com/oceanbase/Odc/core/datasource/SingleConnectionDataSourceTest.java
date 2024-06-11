package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;


public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
            assertTrue(Proxy.isProxyClass(connection.getClass()));

            dataSource.close();
            assertTrue(dataSource.getConnection().isClosed());

        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithUsernamePassword() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection("username", "password");
            assertNotNull(connection);
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.resetConnection();
            fail("Exception should've been thrown");
        } catch (SQLException e) {
            assertEquals("Connection is not null", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);

            dataSource.close();
            assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPrepareConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assertEquals(false, connection.getAutoCommit());

            dataSource.setAutoCommit(true);
            dataSource.prepareConnection(connection);
            assertTrue(connection.getAutoCommit());

        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}