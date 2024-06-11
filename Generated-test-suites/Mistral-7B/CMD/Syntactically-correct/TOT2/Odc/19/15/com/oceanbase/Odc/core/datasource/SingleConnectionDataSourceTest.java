import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource singleConnectionDataSource;
 private Connection connection;
 private EventPublisher eventPublisher;
 private ConnectionInitializer connectionInitializerMock;

 @Before
 public void setUp() throws Exception {
 singleConnectionDataSource = new SingleConnectionDataSource();
 connection = mock(Connection.class);
 eventPublisher = mock(EventPublisher.class);
 connectionInitializerMock = mock(ConnectionInitializer.class);
 }

 @Test
 public void testGetConnection() throws Exception {
 when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);
 when(connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(true);
 singleConnectionDataSource.addInitializer(connectionInitializerMock);

 Connection result = singleConnectionDataSource.getConnection();
 verify(singleConnectionDataSource).initConnection(result);
 verify(connectionInitializerMock).init(result);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionInvalidCredentials() throws Exception {
 when(singleConnectionDataSource.getUsername()).thenReturn("invalid_username");
 when(singleConnectionDataSource.getPassword()).thenReturn("invalid_password");

 try {
 singleConnectionDataSource.getConnection();
 fail();
 } catch (SQLException e) {
 //expected
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionNullConnection() throws Exception {
 when(singleConnectionDataSource.innerCreateConnection()).thenReturn(null);

 try {
 singleConnectionDataSource.getConnection();
 fail();
 } catch (SQLException e) {
 //expected
 }
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionTimeout() throws Exception {
 when(singleConnectionDataSource.lock.tryLock(singleConnectionDataSource.timeOutMillis, TimeUnit.MILLISECONDS))
 .thenReturn(false);
 when(singleConnectionDataSource.eventPublisher).publishEvent(Mockito.isA(ConnectionResetEvent.class))
 .thenThrow(new RuntimeException("Event publication failed"));

 try {
 singleConnectionDataSource.getConnection();
 fail();
 } catch (ConflictException e) {
 //expected
 }
 }

 @Test
 public void testResetConnection() throws Exception {
 singleConnectionDataSource.resetConnection();
 verify(eventPublisher).publishEvent(Mockito.isA(ConnectionResetEvent.class));
 }

 @Test
 public void testClose() throws Exception {
 singleConnectionDataSource.close();
 verify(connection).close();
 }

}