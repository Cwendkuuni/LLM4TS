import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private List<ConnectionInitializer> initializerList;
 private EventPublisher eventPublisherMock;

 @Before
 public void setup() {
 connectionMock = mock(Connection.class);
 initializerList = new ArrayList<>();
 eventPublisherMock = mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource(true, initializerList, eventPublisherMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection from the data source
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertEquals(connectionMock, connection);
 // Test that the connection is initialized
 verify(connectionMock, times(1)).createStatement();
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 // Test getting a connection with invalid username or password
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 dataSource.getConnection();
 fail("Should have thrown an SQLException");
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionOccupied() throws SQLException, InterruptedException {
 // Test getting a connection when the connection is occupied
 when(connectionMock.isValid()).thenReturn(false);
 dataSource.setConnection(connectionMock);
 dataSource.setLock(mock(Lock.class));

 ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
 try {
 dataSource.getConnection();
 fail("Should have thrown a ConflictException");
 } catch (ConflictException e) {
 assertEquals("Connection is occupied, waited 10000 millis", errorMessageCaptor.getValue());
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 // Test resetting a connection
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 // Test adding a connection initializer
 ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializerMock);
 assertEquals(initializerMock, dataSource.getInitializerList().get(0));
 }

 @Test
 public void testClose() throws SQLException, InterruptedException {
 // Test closing the data source
 dataSource.close();
 verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 // Test creating a connection internally
 when(newConnectionFromDriver(anyString(), anyString())).thenReturn(connectionMock);
 dataSource.setConnection(null);
 dataSource.setLock(null);
 dataSource.innerCreateConnection();
 assertEquals(connectionMock, dataSource.getConnection());
 }

}