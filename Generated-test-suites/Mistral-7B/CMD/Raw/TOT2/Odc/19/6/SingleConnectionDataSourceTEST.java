import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;

 @Before
 public void setUp() throws Exception {
 dataSource = new SingleConnectionDataSource();
 dataSource.setAutoCommit(false);
 dataSource.setAutoReconnect(true);
 dataSource.setTimeOutMillis(5000);
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection conn) throws SQLException {
 // initialization logic
 }
 });
 connection = dataSource.getConnection();
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection with valid credentials
 Connection connection1 = dataSource.getConnection();
 assertNotNull(connection1);

 // Test getting a connection with invalid credentials
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));

 // Test getting a connection after it has been closed
 dataSource.close();
 assertThrows(SQLException.class, () -> dataSource.getConnection());
 dataSource.resetConnection();
 }

 @Test
 public void testResetConnection() throws SQLException {
 // Test resetting a connection that is already closed
 dataSource.close();
 dataSource.resetConnection();
 Connection connection2 = dataSource.getConnection();
 assertNotNull(connection2);

 // Test resetting a connection that is still open
 connection = dataSource.getConnection();
 dataSource.resetConnection();
 Connection connection3 = dataSource.getConnection();
 assertNotNull(connection3);
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupied() throws SQLException, InterruptedException {
 // Test acquiring a connection when another thread is holding it
 SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();
 SingleConnectionDataSource dataSource3 = new SingleConnectionDataSource();
 dataSource3.setTimeOutMillis(1);
 Lock lock = new ReentrantLock();
 InvocationHandler handler = Proxy.getInvocationHandler(dataSource3.getConnectionProxy(connection));
 Proxy.getProxyClass(Connection.class).getMethod("close").invoke(handler); // simulate closing the connection

 dataSource2.setTimeOutMillis(5000);
 Connection connection4 = dataSource2.getConnection();
 fail("Expected ConflictException, but got " + connection4.getClass().getName());
 }

 @Test
 public void testClose() throws SQLException {
 // Test closing a connection
 Connection connection5 = dataSource.getConnection();
 assertNotNull(connection5);
 dataSource.close();
 assertFalse(dataSource.isOpen());
 }
}