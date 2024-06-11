package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testGetConnection() {
        try {
            when(connection.isValid(10)).thenReturn(true);
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("SQLException thrown when getting connection");
        }
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        try {
            when(connection.isValid(10)).thenReturn(true);
            assertNotNull(dataSource.getConnection("username", "password"));
        } catch (SQLException e) {
            fail("SQLException thrown when getting connection with username and password");
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.setConnection(connection);
            dataSource.resetConnection();
            assertTrue(dataSource.getConnection() != connection);
        } catch (SQLException e) {
            fail("SQLException thrown when reset connection");
        }
    }

    @Test
    public void testClose() {
        try {
            dataSource.setConnection(connection);
            dataSource.close();
            assertTrue(dataSource.getConnection() == null);
        } catch (Exception e) {
            fail("Exception thrown when closing connection");
        }
    }
}