import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
  
  private SingleConnectionDataSource dataSource;
  
  @Before
  public void setUp() {
    dataSource = new SingleConnectionDataSource();
  }
  
  @After
  public void tearDown() {
    dataSource = null;
  }
  
  @Test
  public void testGetConnection() throws SQLException {
    Connection connection = dataSource.getConnection();
    assertTrue(connection.isValid(1));
  }
  
  @Test
  public void testGetConnectionWithUsernameAndPassword() throws SQLException {
    Connection connection = dataSource.getConnection("username", "password");
    assertTrue(connection.isValid(1));
  }
  
  @Test
  public void testResetConnection() throws SQLException {
    dataSource.resetConnection();
    assertNull(dataSource.getConnection());
  }
  
  @Test
  public void testAddInitializer() {
    ConnectionInitializer initializer = mock(ConnectionInitializer.class);
    dataSource.addInitializer(initializer);
    assertEquals(1, dataSource.getInitializerList().size());
  }
  
  @Test
  public void testClose() {
    dataSource.close();
    verify(dataSource.getConnection(), times(1)).close();
  }
  
  @Test
  public void testPrepareConnection() throws SQLException {
    Connection connection = dataSource.getConnection();
    dataSource.prepareConnection(connection);
    assertFalse(connection.getAutoCommit());
  }
  
}