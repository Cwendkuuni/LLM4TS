import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.common.event.DefaultEventPublisher;

class SingleConnectionDataSourceTest {

 @BeforeAll
 static void setup() {
 EventPublisher eventPublisher = new DefaultEventPublisher();
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true, eventPublisher);
 }

 @AfterAll
 static void teardown() {
 // Ensure the data source is closed at the end of the test
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 try {
 dataSource.close();
 } catch (SQLException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 }

 @Test
 void testGetConnectionWithInvalidCredentials() throws SQLException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
 }

 @Test
 void testResetConnection() throws SQLException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 Connection connection = dataSource.getConnection();
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 void testAddInitializer() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 ConnectionInitializer initializer = mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializer);
 assertTrue(dataSource.getInitializerList().contains(initializer));
 }

 @Test
 void testClose() throws SQLException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 Connection connection = dataSource.getConnection();
 dataSource.close();
 assertNull(dataSource.getConnection());
 }

 @Test
 void testGetAutoCommit() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 assertNull(dataSource.getAutoCommit());
 dataSource.setAutoCommit(true);
 assertTrue(dataSource.getAutoCommit());
 }

 @Test
 void testGetConnectionWithAutoReconnect() throws SQLException, InterruptedException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
 Connection connection = dataSource.getConnection();
 dataSource.resetConnection();
 Connection newConnection = dataSource.getConnection();
 assertEquals(connection, newConnection);
 }

 @Test
 void testGetConnectionWithAutoReconnectAndException() throws SQLException, InterruptedException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
 Connection connection = dataSource.getConnection();
 dataSource.resetConnection();
 try {
 dataSource.getConnection();
 fail("Expected SQLException to be thrown");
 } catch (SQLException e) {
 // Expected
 }
 Connection newConnection = dataSource.getConnection();
 assertNotEquals(connection, newConnection);
 }

 @Test
 void testPublishEvent() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 EventPublisher eventPublisher = dataSource.getEventPublisher();
 assertNull(eventPublisher);
 DataSourceEvent event = new DataSourceEvent("testEvent");
 eventPublisher.publishEvent(event);
 assertTrue(dataSource.eventPublisher.getEventQueue().contains(event));
 }

}