import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private ConnectionInitializer connectionInitializerMock;
 private EventPublisher eventPublisherMock;
 private ReentrantLock lockMock;

 @Before
 public void setUp() {
 this.dataSource = new SingleConnectionDataSource();
 this.connectionMock = mock(Connection.class);
 this.connectionInitializerMock = mock(ConnectionInitializer.class);
 this.eventPublisherMock = mock(EventPublisher.class);
 this.lockMock = new ReentrantLock();
 this.dataSource.addInitializer(this.connectionInitializerMock);
 this.dataSource.setEventPublisher(this.eventPublisherMock);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting the connection for the first time
 Connection result = this.dataSource.getConnection();
 verify(this.dataSource).innerCreateConnection();
 verify(this.dataSource).prepareConnection(ArgumentMatchers.eq(this.connectionMock));
 verify(this.dataSource).getConnectionProxy(ArgumentMatchers.eq(this.connectionMock));

 // Test getting the same connection again
 when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
 Connection result2 = this.dataSource.getConnection();
 verify(this.dataSource, Mockito.times(1)).innerCreateConnection();

 // Test getting the connection with invalid username and password
 when(this.dataSource.innerCreateConnection()).thenThrow(new SQLException());
 try {
 this.dataSource.getConnection("invalid_username", "invalid_password");
 } catch (SQLException e) {
 verify(this.dataSource).getConnection();
 verify(this.dataSource).getConnection();
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithNullConnection() throws SQLException {
 this.dataSource.setConnection(null);
 this.dataSource.getConnection();
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupiedException() throws SQLException, InterruptedException {
 this.dataSource.setConnection(this.connectionMock);
 this.dataSource.setLock(this.lockMock);
 this.lockMock.lock();
 this.dataSource.getConnection();
 this.dataSource.getConnection();
 }

 @Test
 public void testResetConnection() throws SQLException {
 this.dataSource.resetConnection();
 verify(this.eventPublisherMock).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
 }

 @Test
 public void testAddInitializer() {
 this.dataSource.addInitializer(this.connectionInitializerMock);
 verify(this.dataSource).innerCreateConnection();
 }

 @Test
 public void testClose() {
 this.dataSource.close();
 verify(this.connectionMock).close();
 }
}