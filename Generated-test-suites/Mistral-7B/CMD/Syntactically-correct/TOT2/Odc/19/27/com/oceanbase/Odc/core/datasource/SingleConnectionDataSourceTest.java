import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private ConnectionInitializer initializerMock;
 private EventPublisher eventPublisherMock;

 @Before
 public void setUp() {
 initializerMock = mock(ConnectionInitializer.class);
 eventPublisherMock = mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource() {
 @Override
 public void addInitializer(ConnectionInitializer connectionInitializer) {
 super.addInitializer(connectionInitializer);
 initializerList.add(initializerMock);
 }

 @Override
 public EventPublisher getEventPublisher() {
 return eventPublisherMock;
 }
 };
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test normal usage
 Connection connection = dataSource.getConnection();
 assertSame(connection, dataSource.getConnection());

 // Test reset connection
 dataSource.resetConnection();
 Connection resetConnection = dataSource.getConnection();
 assertNull(resetConnection);

 // Test addInitializer
 dataSource.addInitializer(initializerMock);
 Connection initializedConnection = dataSource.getConnection();
 verify(initializerMock).init(initializedConnection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 dataSource.getConnection();
 }

 @Test(expected = ConflictException.class)
 public void testConnectionOccupiedException() throws SQLException, InterruptedException {
 dataSource.lock.lock();
 dataSource.getConnection();
 Thread.sleep(50);
 dataSource.getConnection();
 }

}