
package com.oceanbase.odc.core.datasource;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(connection -> {
            // do nothing for test
        });
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }
  
    // Add more test cases for other methods if needed

}
