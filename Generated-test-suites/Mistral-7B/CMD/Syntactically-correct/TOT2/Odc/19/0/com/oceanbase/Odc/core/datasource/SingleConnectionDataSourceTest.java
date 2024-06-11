import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ReentrantLock;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private EventPublisher eventPublisher;
 private Connection connection;

 @Before
 public void setup() throws Exception {
 dataSource = new SingleConnectionDataSource();
 eventPublisher = new EventPublisher() {};
 dataSource.setEventPublisher(eventPublisher);
 connection = dataSource.getConnection();
 }

 @Test
 public void testGetConnection() throws SQLException {
 assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 SingleConnectionDataSource invalidDataSource = new SingleConnectionDataSource();
 invalidDataSource.setUsername("invalidUsername");
 invalidDataSource.setPassword("invalidPassword");
 try {
 invalidDataSource.getConnection();
 fail("Expected SQLException to be thrown");
 } catch (SQLException e) {
 assertTrue(e.getMessage().contains("Invalid username or password"));
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithNullAutoCommit() throws SQLException {
 SingleConnectionDataSource nullAutoCommitDataSource = new SingleConnectionDataSource();
 nullAutoCommitDataSource.setAutoCommit(null);
 try {
 nullAutoCommitDataSource.getConnection();
 fail("Expected SQLException to be thrown");
 } catch (SQLException e) {
 assertTrue(e.getMessage().contains("Auto commit value is null."));
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() throws SQLException {
 ConnectionInitializer initializer = mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializer);
 Connection connection = dataSource.getConnection();
 verify(initializer).init(connection);
 }

 @Test(expected = ConflictException.class)
 public void testLockConnectionWithTimeout() throws SQLException {
 ReentrantLock lock = new ReentrantLock();
 SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
 singleConnectionDataSource.setLock(lock);
 singleConnectionDataSource.setTimeOutMillis(1);
 Boolean acquireLock = singleConnectionDataSource.tryLock();
 assertFalse(acquireLock);
 singleConnectionDataSource.getConnection();
 singleConnectionDataSource.close();
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 ConnectionResetEvent event = new ConnectionResetEvent(connection);
 eventPublisher.publishEvent(event);
 assertTrue(eventPublisher.hasPublished(ConnectionResetEvent.class));
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
 singleConnectionDataSource.connection = null;
 singleConnectionDataSource.lock = null;
 Connection innerConnection = singleConnectionDataSource.innerCreateConnection();
 assertNotNull(innerConnection);
 singleConnectionDataSource.setConnection(innerConnection);
 assertSame(innerConnection, singleConnectionDataSource.getConnection());
 }

}