import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;

 @Before
 public void setup() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 connection = dataSource.getConnection();
 }

 @Test
 public void testGetConnection() throws SQLException {
 assertEquals(connection, dataSource.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 try {
 dataSource.getConnection();
 fail();
 } catch (SQLException e) {
 assertEquals("Invalid username or password", e.getMessage());
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithNullConnection() {
 dataSource.setConnection(null);
 try {
 dataSource.getConnection();
 fail();
 } catch (SQLException e) {
 // expected exception
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionAlreadyOccupied() throws SQLException, InterruptedException {
 final SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();
 dataSource2.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 try {
 Thread.sleep(dataSource.getTimeOutMillis() + 100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 });
 dataSource.getConnection();
 try {
 dataSource2.getConnection();
 fail();
 } catch (ConflictException e) {
 // expected exception
 }
 }

 @Test(expected = SQLException.class)
 public void testConnectionResetEvent() throws SQLException, InterruptedException {
 final EventPublisher eventPublisher = new EventPublisher() {
 @Override
 public void publishEvent(Event event) {
 fail();
 }
 };
 dataSource = new SingleConnectionDataSource() {
 @Override
 EventPublisher eventPublisher = eventPublisher;
 };
 dataSource.setEventPublisher(eventPublisher);
 Connection connection = dataSource.getConnection();
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testCloseIgnoreInvocationHandler() throws SQLException {
 dataSource.close();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAutoCommit() throws SQLException {
 Connection connection = dataSource.getConnection();
 connection.setAutoCommit(false);
 dataSource.setAutoCommit(true);
 assertTrue(connection.isAutoCommit());
 }

 @Test
 public void testSetAutoReconnect() {
 dataSource = new SingleConnectionDataSource(true);
 assertTrue(dataSource.isAutoReconnect());
 }

 @Test
 public void testSetEventPublisher() {
 EventPublisher eventPublisher = new EventPublisher() {};
 dataSource.setEventPublisher(eventPublisher);
 assertEquals(eventPublisher, dataSource.getEventPublisher());
 }
}