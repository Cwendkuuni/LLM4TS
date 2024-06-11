package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertNotNull("Connection should not be null", conn);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        Connection conn = dataSource.getConnection(username, password);
        assertNotNull("Connection should not be null", conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        Connection conn = dataSource.getConnection("invalidUser", "invalidPassword");
    }
    

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
    }
    
    @Test
    public void testAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue("AutoCommit should be true", dataSource.getAutoCommit());
    }
    
    @Test
    public void testClose() {
        dataSource.close();
        assertNull("Connection should be null after close", dataSource.getConnection());
    }
    
    @After
    public void tearDown() {
        dataSource = null;
    }
}
