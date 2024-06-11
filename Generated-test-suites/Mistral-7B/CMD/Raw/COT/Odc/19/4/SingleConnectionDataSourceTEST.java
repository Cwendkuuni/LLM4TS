import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource singleConnectionDataSource;
 private Connection connectionMock;
 private List<ConnectionInitializer> initializerList = new ArrayList<>();
 private Lock lockMock = Mockito.mock(Lock.class);
 private EventPublisher eventPublisherMock = mock(EventPublisher.class);

 @Before
 public void setUp() {
 singleConnectionDataSource = new SingleConnectionDataSource();
 connectionMock = mock(Connection.class);
 }

 @After
 public void tearDown() throws Exception {
 singleConnectionDataSource.close();
 }

 @Test
 public void testGetConnection() throws SQLException {
 singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic
 }
 });
 singleConnectionDataSource.setAutoReconnect(true);
 singleConnectionDataSource.setEventPublisher(eventPublisherMock);

 // Test normal scenario
 Connection connection = singleConnectionDataSource.getConnection();
 assertThat(connection).isNotNull();
 verify(eventPublisherMock).publishEvent(Mockito.any(ConnectionResetEvent.class));

 // Test scenario when connection is closed or invalid
 singleConnectionDataSource.close();
 try {
 singleConnectionDataSource.getConnection();
 } catch (SQLException e) {
 // expected exception
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 singleConnectionDataSource.setUsername("invalid_username");
 singleConnectionDataSource.setPassword("invalid_password");
 singleConnectionDataSource.getConnection();
 }

 @Test
 public void testResetConnection() throws SQLException {
 singleConnectionDataSource.resetConnection();
 singleConnectionDataSource.setAutoReconnect(false);
 try {
 singleConnectionDataSource.getConnection();
 } catch (SQLException e) {
 // expected exception
 }
 }

 @Test
 public void testAddInitializer() {
 singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic
 }
 });
 List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
 assertThat(initializerList).hasSize(1);
 }

 @Test
 public void testClose() throws SQLException {
 singleConnectionDataSource.close();
 singleConnectionDataSource.getConnection();
 // expected exception
 }
}