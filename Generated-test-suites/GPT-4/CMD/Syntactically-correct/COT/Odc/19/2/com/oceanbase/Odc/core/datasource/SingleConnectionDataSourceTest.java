package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;
import org.junit.Test;
import mockit.*;

// Main test class
public class SingleConnectionDataSourceTest {

    // Test the SingleConnectionDataSource constructor
    @Test
    public void testSingleConnectionDataSourceConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertNotNull(dataSource.getAutoReconnect());
    }
    
    // ...More tests for other public methods...

    // Test getConnection with null username
    @Test(expected = SQLException.class)
    public void testGetConnectionNullUsername() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.getConnection(null, "password");
    }

    // Test getConnection with null password
    @Test(expected = SQLException.class)
    public void testGetConnectionNullPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.getConnection("username", null);
    }
    
    // Test close connection already closed
    @Test(expected = SQLException.class)
    public void testCloseClosedConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.close();
        dataSource.close();
    }
    
    // Test reset closed connection
    @Test(expected = SQLException.class)
    public void testResetClosedConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.close();
        dataSource.resetConnection();
    }
}