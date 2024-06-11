import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.staticords.DoReturn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private List<ConnectionInitializer> initializerList;
 private EventPublisher eventPublisher;
 private Lock lock;

 @Before
 public void setUp() throws Exception {
 initializerList = new LinkedList<>();
 dataSource = new SingleConnectionDataSource() {
 @Override
 protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
 connection = DriverManager.getConnection("jdbc:mock:database", username, password);
 return connection;
 }
 };
 dataSource.setAutoReconnect(true);
 dataSource.setTimeOutMillis(5000);
 eventPublisher = Mockito.mock(EventPublisher.class);
 dataSource.setEventPublisher(eventPublisher);
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null && connection.isClosed() == false) {
 connection.close();
 }
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertSame(connection, dataSource.getConnection());
 }

 @Test
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
 }

 @Test
 public void testGetConnectionWithSelfCredentials() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertSame(dataSource.getUsername(), connection.getUsername());
 assertSame(dataSource.getPassword(), connection.getPassword());
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() throws SQLException {
 ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializer);
 DoReturn(initializer).when(initializer).getName();
 dataSource.getConnection();
 verify(initializer).init(connection);
 }

 @Test(expected = ConflictException.class)
 public void testLockTimeout() throws SQLException {
 dataSource.setTimeOutMillis(50);
 dataSource.getConnection();
 dataSource.getConnection();
 }

 @Test
 public void testConnectionReset() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertEquals(connection, dataSource.getConnection());
 dataSource.onConnectionReset(connection);
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testPublishEvent() throws SQLException {
 Connection connection = dataSource.getConnection();
 dataSource.onConnectionReset(connection);
 Mockito.verify(eventPublisher).publishEvent(Mockito.isA(ConnectionResetEvent.class));
 }
}