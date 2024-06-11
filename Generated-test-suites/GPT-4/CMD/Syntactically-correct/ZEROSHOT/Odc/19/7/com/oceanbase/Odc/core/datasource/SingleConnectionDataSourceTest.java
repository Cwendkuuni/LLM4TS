package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.sql.Connection;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        assertNotNull(connection1);

        Connection connection2 = dataSource.getConnection();
        assertNotSame(connection1, connection2);
    }

    @Test
    public void testGetConnectionWithCredentials() {
        try {
            dataSource.getConnection("user", "pass");
            fail("Expected SQLException not thrown");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.getConnection();
            dataSource.resetConnection();
            assertNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            dataSource.getConnection();
            dataSource.close();
            assertNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        dataSource = null;
    }
}
