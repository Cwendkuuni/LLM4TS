import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

  private SingleConnectionDataSource dataSource;

  @Before
  public void setUp() {
    dataSource = new SingleConnectionDataSource(true);
  }

  @After
  public void tearDown() {
    dataSource = null;
  }

  @Test
  public void testGetConnection() throws SQLException {
    assertNotNull(dataSource.getConnection());
  }

  @Test
  public void testGetConnectionWithCredentials() {
    SQLException exception = assertThrows(SQLException.class, () -> {
      dataSource.getConnection("username", "password");
    });
    assertEquals("Invalid username or password", exception.getMessage());
  }

  @Test
  public void testResetConnection() throws SQLException {
    dataSource.getConnection(); // Ensure connection is initialized
    dataSource.resetConnection();
    assertNull(dataSource.getConnection());
  }

  @Test
  public void testAddInitializer() {
    dataSource.addInitializer(new ConnectionInitializer() {
      @Override
      public void init(Connection connection) {
        // Custom initialization logic
      }
    });
    assertFalse(dataSource.getInitializerList().isEmpty());
  }

  @Test
  public void testClose() throws SQLException {
    dataSource.getConnection(); // Ensure connection is initialized
    dataSource.close();
    assertNull(dataSource.getConnection());
  }

  @Test
  public void testPrepareConnection() throws SQLException {
    dataSource.setAutoCommit(true); // Set auto commit to true
    dataSource.prepareConnection(dataSource.getConnection());
    assertTrue(dataSource.getConnection().getAutoCommit());
  }

  @Test
  public void testTryLock() {
    assertTrue(dataSource.tryLock());
  }

  @Test
  public void testGetConnectionProxy() {
    assertNotNull(dataSource.getConnectionProxy(dataSource.getConnection()));
  }

  @Test
  public void testInitConnection() throws SQLException {
    dataSource.getConnection(); // Ensure connection is initialized
    dataSource.initConnection(dataSource.getConnection());
    // Custom initialization logic verification
  }

  @Test
  public void testOnConnectionCreated() {
    dataSource.onConnectionCreated(dataSource.getConnection());
    // Verify connection creation
  }

  @Test
  public void testOnConnectionReset() throws SQLException {
    dataSource.getConnection(); // Ensure connection is initialized
    dataSource.resetConnection(); // Reset connection
    // Verify connection reset logic
  }

  @Test
  public void testInnerCreateConnection() throws SQLException {
    dataSource.innerCreateConnection();
    assertNotNull(dataSource.getConnection());
  }

}