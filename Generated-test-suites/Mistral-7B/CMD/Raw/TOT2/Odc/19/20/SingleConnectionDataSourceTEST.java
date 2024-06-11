import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private EventPublisher eventPublisherMock;
 private Connection connectionMock;
 private ConnectionInitializer connectionInitializerMock;

 @Before
 public void setup() {
 eventPublisherMock = Mockito.mock(EventPublisher.class);
 connectionMock = Mockito.mock(Connection.class);
 connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
 dataSource = new SingleConnectionDataSource(true, eventPublisherMock);
 dataSource.addInitializer(connectionInitializerMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection for the first time
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertEquals(dataSource.getConnection(), connection);

 // Test getting a connection after it has been closed
 dataSource.close();
 Connection closedConnection = dataSource.getConnection();
 assertNotNull(closedConnection);
 assertTrue(closedConnection.isClosed());

 // Test getting a connection with a different username and password
 assertThrows(SQLException.class, () -> dataSource.getConnection("testUser", "testPassword"));

 // Test resetting the connection
 dataSource.resetConnection();
 Connection resetConnection = dataSource.getConnection();
 assertNotNull(resetConnection);
 assertNotEquals(dataSource.getConnection(), resetConnection);
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializerMock);
 verify(connectionInitializerMock).init(dataSource.getConnection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInitializer() {
 dataSource.addInitializer(null);
 }

 @Test
 public void testClose() throws Exception {
 dataSource.close();
 ArgumentCaptor<Connection> connectionCaptor = ArgumentCaptor.forClass(Connection.class);
 verify(connectionMock).close();
 verify(eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testInitConnection() throws SQLException {
 Connection connection = Mockito.spy(connectionMock);
 dataSource.initConnection(connection);
 verify(connectionInitializerMock).init(connection);
 }

 @Test(expected = ConflictException.class)
 public void testConnectionTimeout() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(0);
 try {
 dataSource.getConnection();
 fail();
 } catch (SQLException e) {
 verify(eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
 }
 }
}