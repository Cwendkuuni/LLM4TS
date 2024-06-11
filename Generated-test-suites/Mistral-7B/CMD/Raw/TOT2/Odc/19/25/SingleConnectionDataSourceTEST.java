import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private Connection resetConnectionMock;
 private EventPublisher eventPublisherMock;
 private List<ConnectionInitializer> initializerList;

 @Before
 public void setup() throws SQLException {
 connectionMock = Mockito.mock(Connection.class);
 resetConnectionMock = Mockito.mock(Connection.class);
 eventPublisherMock = Mockito.mock(EventPublisher.class);
 initializerList = new ArrayList<>();
 dataSource = new SingleConnectionDataSource() {
 @Override
 protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
 return connectionMock;
 }
 @Override
 public void initConnection(Connection connection) throws SQLException {
 super.initConnection(connection);
 initializerList.add(Mockito.mock(ConnectionInitializer.class));
 }
 @Override
 protected void onConnectionReset(Connection connection) {
 super.onConnectionReset(resetConnectionMock);
 }
 };
 dataSource.addInitializer(Mockito.mock(ConnectionInitializer.class));
 dataSource.eventPublisher = eventPublisherMock;
 dataSource.autoReconnect = true;
 }

 @Test
 public void getConnection_shouldReturnExistingConnection() throws SQLException {
 assertNotNull(dataSource.getConnection());
 }

 @Test
 public void getConnection_shouldThrowExceptionIfAutoReconnectIsFalseAndConnectionIsClosed() throws SQLException {
 dataSource.connection = connectionMock;
 dataSource.lock = Mockito.mock(Lock.class);
 Mockito.when(connectionMock.isClosed()).thenReturn(true);
 assertThrows(SQLException.class, () -> dataSource.getConnection());
 }

 @Test
 public void getConnection_shouldReturnNewConnectionIfCurrentOneIsClosedOrInvalid() throws SQLException {
 Mockito.when(connectionMock.isClosed()).thenReturn(true);
 Connection newConnection = Mockito.mock(Connection.class);
 dataSource.connection = connectionMock;
 dataSource.resetConnection();
 assertNotNull(dataSource.getConnection());
 assertNull(dataSource.connection);
 assertFalse(dataSource.lock.isHeldByCurrentThread());
 Mockito.verify(eventPublisherMock).publishEvent(Mockito.any(ConnectionResetEvent.class));
 }

 @Test
 public void addInitializer_shouldAddConnectionInitializerToList() {
 dataSource.addInitializer(Mockito.mock(ConnectionInitializer.class));
 assertFalse(initializerList.isEmpty());
 }

 @Test(expected = SQLException.class)
 public void getConnection_shouldThrowExceptionIfInvalidCredentials() throws SQLException {
 dataSource.username = "invalidUsername";
 dataSource.password = "invalidPassword";
 assertThrows(SQLException.class, () -> dataSource.getConnection());
 }

 @Test
 public void resetConnection_shouldCloseAndResetConnection() throws SQLException {
 dataSource.connection = connectionMock;
 dataSource.resetConnection();
 Mockito.verify(connectionMock).close();
 }

 @Test
 public void onConnectionReset_shouldPublishConnectionResetEvent() throws SQLException {
 ConnectionResetEvent event = Mockito.mock(ConnectionResetEvent.class);
 Mockito.when(eventPublisherMock.publishEvent(Mockito.any(ConnectionResetEvent.class)))
 .thenReturn(event);
 dataSource.onConnectionReset(resetConnectionMock);
 Mockito.verify(event).getConnection();
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection innerConnection = dataSource.innerCreateConnection();
 assertNotNull(innerConnection);
 }
}