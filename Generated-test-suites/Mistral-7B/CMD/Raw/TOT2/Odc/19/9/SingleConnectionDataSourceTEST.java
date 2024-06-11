import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private EventPublisher eventPublisherMock;
 private ConnectionInitializer connectionInitializerMock;

 @Before
 public void setup() {
 this.eventPublisherMock = Mockito.mock(EventPublisher.class);
 this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
 this.dataSource = new SingleConnectionDataSource() {
 @Override
 public List<ConnectionInitializer> getInitializerList() {
 return new ArrayList<ConnectionInitializer>() {{
 add(connectionInitializerMock);
 }};
 }
 };
 this.dataSource.setEventPublisher(eventPublisherMock);
 this.dataSource.addInitializer(connectionInitializerMock);
 try {
 this.connection = DriverManager.getConnection("jdbc:mock:database", "user", "password");
 } catch (SQLException e) {
 throw new RuntimeException(e);
 }
 }

 @After
 public void teardown() throws SQLException {
 this.dataSource.close();
 this.connection.close();
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = this.dataSource.getConnection();
 Assert.assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
 this.dataSource.setUsername("invalid_username");
 this.dataSource.setPassword("invalid_password");
 try {
 this.dataSource.getConnection();
 } catch (SQLException e) {
 Assert.assertEquals("Invalid username or password", e.getMessage());
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithClosedConnection() throws SQLException {
 this.dataSource.getConnection();
 this.dataSource.close();
 try {
 this.dataSource.getConnection();
 } catch (SQLException e) {
 Assert.assertEquals("Connection was closed or not valid", e.getMessage());
 }
 }

 @Test
 public void testAddInitializer() {
 this.dataSource.addInitializer(connectionInitializerMock);
 Mockito.verify(connectionInitializerMock).init(this.connection);
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupied() throws SQLException {
 this.dataSource.getConnection();
 Mockito.doReturn(true).when(this.dataSource.tryLock());
 try {
 this.dataSource.getConnection();
 } catch (ConflictException e) {
 Assert.assertEquals(ErrorCodes.ConnectionOccupied, e.getErrorCode());
 }
 }
}