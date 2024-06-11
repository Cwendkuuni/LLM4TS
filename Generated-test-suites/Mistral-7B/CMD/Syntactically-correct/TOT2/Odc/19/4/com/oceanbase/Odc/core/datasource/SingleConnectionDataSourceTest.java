import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource singleConnectionDataSource;
 private Connection connectionMock;
 private List<ConnectionInitializer> initializerList = new LinkedList<>();

 @Before
 public void setUp() {
 singleConnectionDataSource = new SingleConnectionDataSource();
 connectionMock = Mockito.mock(Connection.class);
 singleConnectionDataSource.addInitializer(connectionMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection with default settings
 Connection result = singleConnectionDataSource.getConnection();
 assertNotNull(result);
 // Test getting a connection with custom settings
 singleConnectionDataSource.setAutoCommit(false);
 Connection customResult = singleConnectionDataSource.getConnection();
 assertNotNull(customResult);
 assertEquals(false, customResult.getAutoCommit());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
 singleConnectionDataSource.setUsername("invalid_username");
 singleConnectionDataSource.setPassword("invalid_password");
 try {
 singleConnectionDataSource.getConnection();
 fail("Expected SQLException to be thrown");
 } catch (SQLException e) {
 assertEquals("Invalid username or password", e.getMessage());
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 singleConnectionDataSource.resetConnection();
 verify(connectionMock).close();
 singleConnectionDataSource.setEventPublisher(Mockito.mock(EventPublisher.class));
 singleConnectionDataSource.resetConnection();
 verify(connectionMock).close();
 verify(singleConnectionDataSource.eventPublisher).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testAddInitializer() {
 singleConnectionDataSource.addInitializer(connectionMock);
 assertTrue(singleConnectionDataSource.initializerList.contains(connectionMock));
 }

 @Test(expected = ConflictException.class)
 public void testLockIsOccupied() throws SQLException {
 SingleConnectionDataSource lockedDataSource = new SingleConnectionDataSource();
 lockedDataSource.lock = new ReentrantLock();
 lockedDataSource.lock.lock();
 try {
 SingleConnectionDataSource testDataSource = new SingleConnectionDataSource();
 testDataSource.lock = new ReentrantLock();
 testDataSource.getConnection();
 fail("Expected ConflictException to be thrown");
 } catch (ConflictException e) {
 assertEquals("Connection is occupied, waited " + testDataSource.timeOutMillis + " millis", e.getMessage());
 }
 }
}