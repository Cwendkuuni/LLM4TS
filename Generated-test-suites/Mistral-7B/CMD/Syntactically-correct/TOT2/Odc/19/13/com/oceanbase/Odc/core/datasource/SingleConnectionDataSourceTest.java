import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private List<ConnectionInitializer> initializerList = new LinkedList<>();

 @Before
 public void setUp() {
 dataSource = new SingleConnectionDataSource(true);
 connectionMock = Mockito.mock(Connection.class);
 dataSource.addInitializer(mock(ConnectionInitializer.class));
 dataSource.addInitializer(mock(ConnectionInitializer.class));
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection for the first time
 Connection connection = dataSource.getConnection();
 Assert.assertNotNull(connection);

 // Test getting a connection after it has been closed
 dataSource.close();
 dataSource.getConnection();
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsername() throws SQLException {
 dataSource.getConnection("invalidUsername", "password");
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 verify(connectionMock, times(1)).close();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(mock(ConnectionInitializer.class));
 verify(connectionMock, never()).init(any(Connection.class));
 }

 @Test
 public void testClose() throws SQLException {
 dataSource.close();
 verify(connectionMock, times(1)).close();
 }

 @Test
 public void testSetAutoCommit() throws SQLException {
 Connection connection = dataSource.getConnection();
 dataSource.setAutoCommit(false);
 Assert.assertFalse(connection.getAutoCommit());
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupied() throws SQLException {
 dataSource.resetConnection();
 dataSource.getConnection();
 dataSource.getConnection();
 }
}