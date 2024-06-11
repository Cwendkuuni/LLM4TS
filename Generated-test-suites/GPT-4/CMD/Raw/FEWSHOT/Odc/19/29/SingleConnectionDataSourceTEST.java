
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("username");
        dataSource.setPassword("password");

        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
        assertTrue(Proxy.isProxyClass(connection.getClass()));
        // Note: The actual assert conditions may vary based on your specific scenario,
        // the above are the most common ones
    }

    @Test
    public void testGetConnectionWithWrongCredentials() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("username");
        dataSource.setPassword("password");

        assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUsername", "wrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("username");
        dataSource.setPassword("password");

        Connection connection = dataSource.getConnection();
        assertNotNull(connection); //Ensure connection is established
        
        dataSource.resetConnection();
        assertNull(dataSource.connection); //Ensure connection is reset
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        Connection connection = dataSource.getConnection();

        assertNotNull(connection); //Ensure connection is established
        
        dataSource.close();
        assertTrue(dataSource.connection.isClosed()); // Ensure Connection closed
    }
    
    @Test
    public void testPrepareConnection() throws SQLException {
        Connection mockConnection = new MockConnection();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        
        dataSource.prepareConnection(mockConnection);
        assertTrue(mockConnection.getAutoCommit());
    }
    
    @Test(expected = SQLException.class) 
    public void testConnectionInitializerException() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        dataSource.addInitializer(connection -> { throw new Exception("Sample Exception"); }); 

        try {
            dataSource.initConnection(new MockConnection());
        } catch (SQLException e) {
            assertEquals("java.lang.Exception: Sample Exception", e.getMessage());
            throw e;
        }
    }
    
}
