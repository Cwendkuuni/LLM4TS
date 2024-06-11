
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource scDataSource;

    @Before
    public void setUp() throws Exception {
        scDataSource = new SingleConnectionDataSource();
        scDataSource.setUsername("testuser");
        scDataSource.setPassword("testpassword");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = scDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernamePassword() throws SQLException {
        scDataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = scDataSource.getConnection();
        assertNotNull(connection);
        
        scDataSource.resetConnection();
        
        connection = scDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = scDataSource.getConnection();
        assertNotNull(connection);
        
        scDataSource.close();
        
        assertTrue(connection.isClosed());
    }
}
