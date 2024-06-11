import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private List<ConnectionInitializer> initializerList = new ArrayList<>();

 @Before
 public void setup() {
 dataSource = new SingleConnectionDataSource(true);
 connectionMock = Mockito.mock(Connection.class);
 dataSource.addInitializer(createConnectionInitializerMock());
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection for the first time
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertSame(connection, dataSource.connection);

 // Test getting a connection after it has been closed and reset
 dataSource.close();
 dataSource.resetConnection();
 Connection newConnection = dataSource.getConnection();
 assertNotNull(newConnection);
 assertNotSame(connection, newConnection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_username", "invalid_password"));
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionTimeout() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(0);
 assertThrows(ConflictException.class, () -> dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(createConnectionInitializerMock());
 assertTrue(dataSource.initializerList.contains(createConnectionInitializerMock()));
 }

 private ConnectionInitializer createConnectionInitializerMock() {
 return Mockito.mock(ConnectionInitializer.class);
}

 @Test
 public void testClose() throws SQLException {
 dataSource.close();
 assertNull(dataSource.connection);
 }

}