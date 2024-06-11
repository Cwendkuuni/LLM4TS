import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Assert;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

  @Test
  public void testAutoCommitMethods() {
    Boolean autoCommit = true;
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setAutoCommit(autoCommit);
    Assert.assertEquals(autoCommit, dataSource.getAutoCommit());
  }

  @Test
  public void testGetAutoReconnect() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    Assert.assertFalse(dataSource.getAutoReconnect());
    dataSource = new SingleConnectionDataSource(true);
    Assert.assertTrue(dataSource.getAutoReconnect());
  }

  @Test(expected = SQLException.class)
  public void testGetConnection() throws SQLException {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    Connection conn = dataSource.getConnection();
    Assert.assertNotNull(conn);
    conn.close();
  }

  @Test(expected = SQLException.class)
  public void testGetConnectionWithCredentials() throws SQLException {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    Connection conn = dataSource.getConnection("myUsername", "myPassword");
  }

  @Test(expected = SQLException.class)
  public void testResetConnection() throws SQLException {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.resetConnection();
  }

  @Test
  public void testSetTimeoutMillis() {
    long timeOutMillis = 5000L;
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setTimeoutMillis(timeoutMillis);
  }
}