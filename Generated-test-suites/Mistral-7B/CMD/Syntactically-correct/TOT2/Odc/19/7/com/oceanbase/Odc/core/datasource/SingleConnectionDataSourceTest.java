import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.common.event.TestEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 dataSource = new SingleConnectionDataSource();
 connection = Mockito.mock(Connection.class);
 eventPublisher = new TestEventPublisher();
 dataSource.setEventPublisher(eventPublisher);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Happy path
 Connection result = dataSource.getConnection();
 assert (result != null);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithoutInitializeConnection() throws SQLException {
 dataSource.resetConnection();
 Connection result = dataSource.getConnection();
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsername() throws SQLException {
 dataSource.resetConnection();
 dataSource.setUsername("InvalidUsername");
 Connection result = dataSource.getConnection("InvalidPassword", "InvalidPassword");
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidPassword() throws SQLException {
 dataSource.resetConnection();
 dataSource.setUsername("ValidUsername");
 Connection result = dataSource.getConnection("InvalidPassword", "InvalidPassword");
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithTimeout() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(1);
 Connection result = dataSource.getConnection();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 statement = connection.createStatement();
 }
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInitializer() {
 dataSource.addInitializer(null);
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 Connection result = dataSource.getConnection();
 assert (result != null);
 }

 @Test
 public void testClose() throws SQLException {
 dataSource.close();
 Connection result = dataSource.getConnection();
 assert (result == null);
 }

 @Test
 public void testPublishEventOnConnectionReset() throws SQLException {
 Mockito.when(connection.isValid()).thenReturn(false);
 dataSource.setConnection(connection);
 dataSource.resetConnection();
 Mockito.verify(eventPublisher).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 dataSource.setAutoCommit(false);
 prepareConnection(connection);
 assert (!connection.getAutoCommit());
 }

 @Test
 public void testPrepareConnectionWithAlreadyPreparedConnection() throws SQLException {
 Connection preparedConnection = Mockito.mock(Connection.class);
 Mockito.when(preparedConnection.getAutoCommit()).thenReturn(false);
 dataSource.setConnection(preparedConnection);
 prepareConnection(connection);
 assert (connection.getAutoCommit() == preparedConnection.getAutoCommit());
 }

 private Statement statement;

 @Test
 public void testInitializeStatement() throws SQLException {
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 statement = connection.createStatement();
 }
 });
 dataSource.getConnection();
 assert (statement != null);
 }

 @Test(expected = SQLException.class)
 public void testInitializeStatementWithException() throws SQLException {
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 throw new SQLException("Test Exception");
 }
 });
 dataSource.getConnection();
 }

 @Test
 public void testOnConnectionCreated() {
 dataSource.onConnectionCreated(connection);
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 Connection resetConnection = Mockito.mock(Connection.class);
 dataSource.setConnection(resetConnection);
 dataSource.resetConnection();
 dataSource.onConnectionReset(resetConnection);
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection innerConnection = Mockito.mock(Connection.class);
 Mockito.when(innerConnection.isValid()).thenReturn(true);
 dataSource.setConnection(innerConnection);
 dataSource.innerCreateConnection();
 assert (dataSource.getConnection() == innerConnection);
 }

 @Test(expected = SQLException.class)
 public void testInnerCreateConnectionWithException() throws SQLException {
 Connection innerConnection = Mockito.mock(Connection.class);
 Mockito.when(innerConnection.isValid()).thenThrow(new SQLException("Test Exception"));
 dataSource.setConnection(innerConnection);
 dataSource.innerCreateConnection();
 }

 @Test
 public void testGetConnectionProxy() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 Lock lock = new ReentrantLock();
 Connection result = dataSource.getConnectionProxy(connection);
 assert (result != null);
 assert (result.getClass().equals(Connection.class));
 }

 @Test
 public void testTryLock() throws InterruptedException {
 Lock lock = new ReentrantLock();
 assert (lock.tryLock(10, TimeUnit.MILLISECONDS));
 assert (lock.isHeldByCurrentThread());
 }

 @Test
 public void testTryLockWithTimeout() throws InterruptedException {
 Lock lock = new ReentrantLock();
 assert (!lock.tryLock(1, TimeUnit.MILLISECONDS));
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 Connection connection = Mockito.mock(Connection.class);
 Lock lock = new ReentrantLock();
 CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
 Connection result = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
 new Class[] {Connection.class}, handler);
 assert (result != null);
 }

 @After
 public void tearDown() {
 dataSource = null;
 connection = null;
 eventPublisher = null;
 }
}