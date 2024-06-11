package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        // Make sure to properly set the connection details before running the tests. 
        // This will normally come from your mock setup or application settings 
    }

    @Test
    public void testGetConnection() {
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUser() throws SQLException {
        Connection conn = dataSource.getConnection("invalid_username", "invalid_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
    }

    @Test
    public void testClose() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);

        dataSource.close();
        assertTrue("Connection should be closed", conn.isClosed());
    }
  
    // You can add more tests for other methods or possible edge cases
  
}