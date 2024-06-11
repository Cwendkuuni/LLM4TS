import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.OceanbaseSingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ReentrantLock;

public class SingleConnectionDataSourceTest {

 private OceanbaseSingleConnectionDataSource dataSource;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 dataSource = new OceanbaseSingleConnectionDataSource();
 dataSource.setAutoReconnect(true);
 dataSource.setEventPublisher(new EventPublisher() {
 @Override
 public void publishEvent(Object event) {
 // do nothing
 }
 });
 eventPublisher = dataSource.getEventPublisher();
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 dataSource.setUsername("invalidUsername");
 dataSource.setPassword("invalidPassword");
 try {
 dataSource.getConnection();
 fail("Expected SQLException to be thrown");
 } catch (SQLException e) {
 // expected exception
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
 dataSource.addInitializer(() -> {});
 List<ConnectionInitializer> newInitializerList = dataSource.getInitializerList();
 assertEquals(initializerList.size() + 1, newInitializerList.size());
 }

 @Test
 public void testClose() throws SQLException {
 Connection connection = dataSource.getConnection();
 try {
 dataSource.close();
 assertNull(connection);
 } finally {
 dataSource.resetConnection();
 }
 }

 @Test
 public void testTryLock() {
 OceanbaseSingleConnectionDataSource dataSource1 = new OceanbaseSingleConnectionDataSource();
 OceanbaseSingleConnectionDataSource dataSource2 = new OceanbaseSingleConnectionDataSource();
 ReentrantLock lock = new ReentrantLock();
 assertFalse(dataSource1.tryLock(10, TimeUnit.MILLISECONDS));
 assertFalse(dataSource2.tryLock(10, TimeUnit.MILLISECONDS));
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionOccupied() throws SQLException {
 OceanbaseSingleConnectionDataSource dataSource1 = new OceanbaseSingleConnectionDataSource();
 OceanbaseSingleConnectionDataSource dataSource2 = new OceanbaseSingleConnectionDataSource();
 dataSource1.getConnection();
 try {
 dataSource2.getConnection();
 fail("Expected ConflictException to be thrown");
 } catch (ConflictException e) {
 // expected exception
 }
 }

 @Test
 public void testPublishEvent() {
 Connection connection = dataSource.getConnection();
 dataSource.onConnectionReset(connection);
 assertNotNull(eventPublisher);
 assertTrue(eventPublisher instanceof EventPublisher);
 try {
 eventPublisher.publishEvent(new ConnectionResetEvent(connection));
 } catch (Exception e) {
 fail("Expected no exception to be thrown");
 }
 }
}