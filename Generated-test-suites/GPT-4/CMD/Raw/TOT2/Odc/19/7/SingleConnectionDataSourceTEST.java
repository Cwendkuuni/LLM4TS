package com.oceanbase.odc.core.datasource;

import org.junit.Before;

import static org.junit.Assert.*;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
    }

    @Test
    public void testGetConnection() {
        try {
            Connection conn = dataSource.getConnection();
            assertNotNull(conn);
            assertTrue(conn.getAutoCommit());
        } catch (Exception e) {
            fail("Exception occurred getting connection");
        }
    }

    @Test
    public void testGetConnectionByUsernamePassword() {
        try {
            // Assume current user: admin and password: admin as correct ones
            String userName = "admin";
            String password = "admin";
            
            Connection conn = dataSource.getConnection(userName, password);
            assertNotNull(conn);

            userName = "admin";
            password = "wrongPassword";
            
            conn = dataSource.getConnection(userName, password);
            fail("Should throw exception when password is wrong");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }
    
    // Similar test methods can be written for resetConnection(),
    // addInitializer() and close() methods.

}