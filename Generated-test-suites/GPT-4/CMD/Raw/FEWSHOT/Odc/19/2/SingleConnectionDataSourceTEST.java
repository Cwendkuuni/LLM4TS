
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }
    
    @Test
    public void testAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getAutoCommit());
    }

    @Test
    public void testTimeOutMillis() {
        long defaultTimeOutMillis = 10 * 1000;
        assertEquals(defaultTimeOutMillis, dataSource.getTimeOutMillis());
        long newTimeOutMillis = 20 * 1000;
        dataSource.setTimeOutMillis(newTimeOutMillis);
        assertEquals(newTimeOutMillis, dataSource.getTimeOutMillis());
    }

    @Test
    public void testGetConnection() {
        try {
            assertNull(dataSource.getConnection());
            Connection connection = dataSource.getConnection("username", "password");
            assertNotNull(connection);
            assertEquals(connection, dataSource.getConnection());

        } catch (SQLException e) {
            fail("An unexpected SQLException was thrown.");
        }
    }

    @Test
    public void testConnectionReset() {
        try {
            dataSource.resetConnection();
            assertNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("An unexpected SQLException was thrown.");
        }
    }
    
    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAutoReconnect() {
        SingleConnectionDataSource singleConnectionWithAutoReconnect = new SingleConnectionDataSource(true);
        assertTrue(singleConnectionWithAutoReconnect.getAutoReconnect());
        assertFalse(dataSource.getAutoReconnect());
    }
    
    @After
    public void tearDown() {
        dataSource.close();
    }
}
