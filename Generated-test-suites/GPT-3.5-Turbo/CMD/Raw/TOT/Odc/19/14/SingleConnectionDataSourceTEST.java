import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
  
  private SingleConnectionDataSource dataSource;
  
  @Before
  public void setUp() {
    dataSource = new SingleConnectionDataSource();
    dataSource.setAutoCommit(false);
  }
  
  @Test
  public void testGetConnection() throws SQLException {
    Connection connection = dataSource.getConnection();
    assertNotNull(connection);
    
    // Check if autoCommit is set to false
    assertFalse(connection.getAutoCommit());
    
    // Check if connection proxy is created
    assertTrue(Proxy.isProxyClass(connection.getClass()));
  }
  
  @Test
  public void testGetConnectionWithUsernameAndPassword() throws SQLException {
    String username = "user";
    String password = "pass";
    
    Connection connection = dataSource.getConnection(username, password);
    assertNotNull(connection);
    
    // Check if autoCommit is set to false
    assertFalse(connection.getAutoCommit());
    
    // Check if connection proxy is created
    assertTrue(Proxy.isProxyClass(connection.getClass()));
  }
  
  @Test(expected = SQLException.class)
  public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
    String username = "invalid";
    String password = "invalid";
    
    dataSource.getConnection(username, password);
  }
  
  // Add more tests for addInitializer, resetConnection, close methods
  
}