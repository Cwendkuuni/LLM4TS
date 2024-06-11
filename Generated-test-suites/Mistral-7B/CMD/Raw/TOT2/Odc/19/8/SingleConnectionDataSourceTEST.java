import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private ConnectionInitializer connectionInitializerMock;
 private EventPublisher eventPublisherMock;

 @Before
 public void setup() {
 this.connectionMock = mock(Connection.class);
 this.connectionInitializerMock = mock(ConnectionInitializer.class);
 this.eventPublisherMock = mock(EventPublisher.class);
 this.dataSource = new SingleConnectionDataSource() {
 private List<ConnectionInitializer> initializerList = new LinkedList<>();

 @Override
 public void addInitializer(ConnectionInitializer initializer) {
 super.addInitializer(initializer);
 this.initializerList.add(0, initializer);
 }
 };
 this.dataSource.setEventPublisher(this.eventPublisherMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 when(this.connectionMock.isValid(this.dataSource.getLoginTimeout())).thenReturn(true);
 when(this.connectionMock.isClosed()).thenReturn(false);
 when(this.connectionMock.getAutoCommit()).thenReturn(null);
 this.dataSource.addInitializer(this.connectionInitializerMock);
 when(this.connectionInitializerMock.init(this.connectionMock)).thenReturn(null);

 Connection connection = this.dataSource.getConnection();

 verify(this.connectionMock).isValid(this.dataSource.getLoginTimeout());
 verify(this.connectionMock).isClosed();
 verify(this.connectionMock).getAutoCommit();
 verify(this.connectionInitializerMock).init(this.connectionMock);

 assert (connection == this.connectionMock);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 when(this.connectionMock.isValid(this.dataSource.getLoginTimeout())).thenReturn(false);

 try {
 this.dataSource.getConnection();
 } catch (SQLException e) {
 // expected exception
 }

 verify(this.connectionMock).isValid(this.dataSource.getLoginTimeout());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithClosedConnection() throws SQLException {
 when(this.connectionMock.isValid(this.dataSource.getLoginTimeout())).thenReturn(true);
 when(this.connectionMock.isClosed()).thenReturn(true);

 try {
 this.dataSource.getConnection();
 } catch (SQLException e) {
 // expected exception
 }

 verify(this.connectionMock).isValid(this.dataSource.getLoginTimeout());
 verify(this.connectionMock).isClosed();
 }

 @Test
 public void testResetConnection() throws SQLException {
 Connection oldConnection = this.dataSource.getConnection();
 this.dataSource.resetConnection();

 assert (this.dataSource.getConnection() != oldConnection);
 verify(this.eventPublisherMock).publishEvent(Mockito.any(ConnectionResetEvent.class));
 }

 @Test(expected = ConflictException.class)
 public void testResetConnectionWhileConnectionIsOccupied() throws SQLException {
 when(this.connectionMock.isValid(this.dataSource.getLoginTimeout())).thenReturn(true);
 this.dataSource.addInitializer(this.connectionInitializerMock);
 when(this.connectionInitializerMock.init(this.connectionMock)).thenThrow(new SQLException());

 try {
 this.dataSource.resetConnection();
 } catch (ConflictException e) {
 // expected exception
 }
 }

 @Test
 public void testAddInitializer() {
 this.dataSource.addInitializer(this.connectionInitializerMock);
 List<ConnectionInitializer> initializers = this.dataSource.getInitializerList();

 assert (initializers.size() == 1);
 assert (initializers.get(0) == this.connectionInitializerMock);
 }

 @Test
 public void testClose() throws SQLException, InterruptedException {
 Connection connection = this.dataSource.getConnection();
 when(this.connectionMock.close()).thenThrow(new SQLException());
 this.dataSource.close();

 verify(this.connectionMock).close();
 }
}