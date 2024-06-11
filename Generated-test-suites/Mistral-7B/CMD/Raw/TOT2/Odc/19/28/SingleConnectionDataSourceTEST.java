import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection mockConnection;
 private List<ConnectionInitializer> initializerList;
 private EventPublisher mockEventPublisher;
 private ConnectionResetEvent connectionResetEvent;

 @Before
 public void setUp() throws Exception {
 mockConnection = Mockito.mock(Connection.class);
 initializerList = new ArrayList<>();
 mockEventPublisher = Mockito.mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource() {
 @Override
 protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
 return mockConnection;
 }

 @Override
 public void initConnection(Connection connection) throws SQLException {
 for (ConnectionInitializer initializer : initializerList) {
 initializer.init(connection);
 }
 }

 @Override
 protected void onConnectionCreated(Connection connection) {
 // Do nothing
 }

 @Override
 protected void onConnectionReset(Connection connection) {
 connectionResetEvent = new ConnectionResetEvent(connection);
 }

 @Override
 public EventPublisher getEventPublisher() {
 return mockEventPublisher;
 }

 @Override
 public void addInitializer(ConnectionInitializer connectionInitializer) {
 initializerList.add(connectionInitializer);
 }

 @Override
 public void close() throws SQLException {
 // Do nothing
 }
 };
 }

 @After
 public void tearDown() throws Exception {
 Mockito.verifyNoMoreInteractions(mockConnection);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test normal usage
 Connection connection = dataSource.getConnection();
 verify(mockConnection).isValid(dataSource.getLoginTimeout());

 // Test connection is reset and reconnected
 dataSource.resetConnection();
 Connection resetConnection = dataSource.getConnection();
 verify(mockConnection).isValid(dataSource.getLoginTimeout());

 // Test error when username and password are invalid
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 try {
 dataSource.getConnection("username", "password");
 } catch (SQLException e) {
 verify(mockConnection).isValid(dataSource.getLoginTimeout());
 verify(mockEventPublisher).publishEvent(connectionResetEvent);
 }
 }

 @Test(expected = SQLException.class)
 public void testResetConnectionWhenConnectionIsNull() throws SQLException {
 dataSource.resetConnection();
 }

 @Test
 public void testAddInitializer() throws SQLException {
 ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializer);
 verify(initializer).init(mockConnection);
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWhenConnectionIsOccupied() throws SQLException {
 dataSource.lock.lock();
 dataSource.getConnection();
 try {
 dataSource.getConnection();
 } catch (ConflictException e) {
 // Expected
 }
 }

 @Test
 public void testGetConnectionProxy() throws SQLException {
 Connection connectionProxy = dataSource.getConnectionProxy(mockConnection);
 verify(mockConnection).isValid(dataSource.getLoginTimeout());
 }
}