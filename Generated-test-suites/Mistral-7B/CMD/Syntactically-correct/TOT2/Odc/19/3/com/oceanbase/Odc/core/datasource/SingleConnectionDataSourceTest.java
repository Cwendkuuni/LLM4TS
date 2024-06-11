import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;

 @Before
 public void setUp() {
 dataSource = new SingleConnectionDataSource(true);
 connectionMock = Mockito.mock(Connection.class);
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 Mockito.doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // Do some initialization here
 return null;
 }
 }).when(connectionMock).close();
 }
 }).init(connectionMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test regular getConnection() call
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 // Test resetConnection() and getConnection()
 dataSource.resetConnection();
 Connection resetConnection = dataSource.getConnection();
 assertNotNull(resetConnection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 // Test getConnection() with invalid credentials
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 try {
 dataSource.getConnection("invalidUser", "invalidPassword");
 fail();
 } catch (SQLException e) {
 // Expected exception
 }
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupied() throws SQLException {
 // Test getConnection() when connection is occupied
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 dataSource.lock.lock();
 try {
 dataSource.getConnection();
 fail();
 } catch (ConflictException e) {
 // Expected exception
 } finally {
 dataSource.lock.unlock();
 }
 }

 @Test
 public void testClose() throws SQLException {
 // Test close()
 dataSource.close();
 assertNull(dataSource.connection);
 }

 @Test
 public void testAddInitializer() {
 // Test adding an initializer
 List<ConnectionInitializer> initializerList = dataSource.initializerList;
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // Do something
 }
 });
 assertEquals(2, initializerList.size());
 }

}