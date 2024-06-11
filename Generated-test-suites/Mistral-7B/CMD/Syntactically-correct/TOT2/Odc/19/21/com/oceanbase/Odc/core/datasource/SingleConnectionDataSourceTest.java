import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private EventPublisher eventPublisherMock;

 @Before
 public void setup() {
 connectionMock = Mockito.mock(Connection.class);
 eventPublisherMock = Mockito.mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource(true, eventPublisherMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertEquals("The connection should be equal to the one returned by innerCreateConnection", connectionMock, connection);
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 String username = "testUser";
 String password = "testPassword";
 dataSource.setUsername(username);
 dataSource.setPassword(password);
 Connection connection = dataSource.getConnection("testUser", "testPassword");
 assertEquals(username, dataSource.getUsername());
 assertEquals(password, dataSource.getPassword());
 assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUser", "wrongPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 assertNull(dataSource.getLock());
 Connection newConnection = dataSource.innerCreateConnection();
 dataSource.onConnectionReset(newConnection);
 Mockito.verify(eventPublisherMock).publishEvent(Mockito.any(ConnectionResetEvent.class));
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer((connection) -> {});
 List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
 assertEquals(1, initializerList.size());
 }

 @Test
 public void testClose() throws SQLException, InterruptedException {
 dataSource.close();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testTryLock() {
 assertTrue(dataSource.tryLock());
 ArgumentCaptor<Long> timeOutCaptor = ArgumentCaptor.forClass(Long.class);
 Mockito.verify(dataSource.getLock()).tryLock(timeOutCaptor.capture());
 assertEquals(10 * 1000, timeOutCaptor.getValue().longValue());
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithOccupiedLock() throws SQLException, InterruptedException {
 dataSource.tryLock();
 assertThrows(ConflictException.class, () -> dataSource.getConnection());
 }
}