import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;

class SingleConnectionDataSourceTest {

 @BeforeAll
 static void setup() {
 // Initialize test environment here, e.g., setting up database schema
 }

 @AfterAll
 static void teardown() {
 // Clean up test environment here, e.g., dropping database schema
 }

 @BeforeEach
 void init() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException, InterruptedException {
 // Initialize connection here, e.g., setting autoCommit
 connection.setAutoCommit(true);
 }
 });
 testDataSource = dataSource;
 }

 @AfterEach
 void cleanup() {
 testDataSource.close();
 testDataSource = null;
 }

 @Test
 void testGetConnection() throws SQLException {
 Connection connection = testDataSource.getConnection();
 assertNotNull(connection, "Connection should not be null");
 assertTrue(connection.isValid(10), "Connection should be valid");
 }

 @Test
 void testGetConnectionWithInvalidUsernameAndPassword() {
 assertThrows(SQLException.class, () -> testDataSource.getConnection("invalidUsername", "invalidPassword"));
 }

 @Test
 void testResetConnection() throws SQLException {
 testDataSource.resetConnection();
 assertNull(testDataSource.connection, "Connection should be null after reset");
 }

 @Test
 void testAddInitializer() {
 testDataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException, InterruptedException {
 // Initialize connection here, e.g., setting autoCommit
 connection.setAutoCommit(false);
 }
 });
 Connection connection = testDataSource.getConnection();
 assertFalse(connection.getAutoCommit(), "Connection should not have autoCommit by default");
 }

 @Test
 void testClose() {
 testDataSource.close();
 assertNull(testDataSource.connection, "Connection should be closed");
 }

 @Test
 void testConnectionResetEvent() throws InterruptedException {
 EventPublisher eventPublisher = new EventPublisher() {
 @Override
 public void publishEvent(Object event) {
 // Check if event is of type ConnectionResetEvent
 assertTrue(event instanceof ConnectionResetEvent);
 }
 };
 testDataSource.setEventPublisher(eventPublisher);
 testDataSource.resetConnection();
 verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 void testExceptionHandling() {
 TestAnnotationExceptionHandler exceptionHandler = new TestAnnotationExceptionHandler();
 testDataSource.setExceptionHandler(exceptionHandler);
 testDataSource.addInitializer(() -> {
 throw new SQLException("Test exception");
 });
 assertThrows(SQLException.class, testDataSource::getConnection);
 }

 @Test
 void testTimeOutException() throws InterruptedException {
 testDataSource.setTimeOutMillis(1);
 try {
 testDataSource.getConnection();
 fail("Expected TimeOutException");
 } catch (SQLException sqlException) {
 assertTrue(sqlException.getMessage().contains("waited 1 millis"));
 }
 }

 @Test
 void testLockException() throws InterruptedException {
 testDataSource.setTimeOutMillis(1);
 testDataSource.addInitializer(() -> {
 testDataSource.lock.lock();
 throw new InterruptedException();
 });
 try {
 testDataSource.getConnection();
 fail("Expected LockException");
 } catch (SQLException sqlException) {
 assertTrue(sqlException.getMessage().contains("Connection is occupied"));
 }
}

 private SingleConnectionDataSource testDataSource;
}