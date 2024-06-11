package com.oceanbase.odc.core.datasource;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnectionWithNullConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull("Connection should not be null", dataSource.getConnection());
    }
  
    @Test(expected = SQLException.class)
    public void testGetConnectionWithDifferentCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("testUser", "wrongPassword");
    }
  
    // Writing more test cases would require an actual database setup as well as possibly using a library like Mockito to mock objects and do proper unit testing
}