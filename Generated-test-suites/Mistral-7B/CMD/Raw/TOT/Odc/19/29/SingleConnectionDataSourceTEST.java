package com.oceanbase.odc.core.datasource;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;
 import static org.junit.Assert.assertThrows;
 import static org.junit.Assert.assertTrue;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.Mockito;

 import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
 import com.oceanbase.odc.core.datasource.ConnectionInitializer;
 import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
 import com.oceanbase.odc.core.datasource.EventPublisher;
 import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
 import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;

 /**
 * Test class for SingleConnectionDataSource.
 */
 public class SingleConnectionDataSourceTest {
     private static final String DATABASE_URL = "jdbc:test:mydatabase";
     private static final String DATABASE_USER = "testuser";
     private static final String DATABASE_PASSWORD = "testpassword";
     private static final int TIMEOUT = 1000;
     private static final long TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(TIMEOUT, TimeUnit.SECONDS);

     private Connection connection;
     private SingleConnectionDataSource dataSource;
     private EventPublisher eventPublisherMock;
     private List<ConnectionInitializer> initializerListMock;

     @Before
     public void setUp() throws Exception {
         Class.forName("com.test.jdbc.Driver"); // Assuming a test JDBC driver is used
         eventPublisherMock = Mockito.mock(EventPublisher.class);
         initializerListMock = new LinkedList<>();
         dataSource = new SingleConnectionDataSource(true) {
             @Override
             public List<ConnectionInitializer> getInitializers() {
                 return initializerListMock;
             }

             @Override
             public EventPublisher getEventPublisher() {
                 return eventPublisherMock;
             }
         };
     }

     @Test
     public void testGetConnection() throws SQLException {
         Connection connection = dataSource.getConnection();
         assertNotNull(connection);
         assertTrue(connection.isValid(TIMEOUT));
     }

     @Test
     public void testGetConnectionWithValidCredentials() throws SQLException {
         dataSource.setUsername(DATABASE_USER);
         dataSource.setPassword(DATABASE_PASSWORD);
         Connection connection = dataSource.getConnection();
         assertEquals(DATABASE_USER, connection.getUserInfo().toString());
         assertEquals(DATABASE_PASSWORD, connection.getPassword());
     }

     @Test
     public void testGetConnectionWithInvalidCredentials() throws SQLException {
         dataSource.setUsername("wronguser");
         dataSource.setPassword("wrongpassword");
         assertThrows(SQLException.class, () -> dataSource.getConnection());
     }

     @Test
     public void testGetConnectionTwice() throws SQLException {
         Connection connection1 = dataSource.getConnection();
         Connection connection2 = dataSource.getConnection();
         assertSame(connection1, connection2);
     }

     @Test
     public void testGetConnectionTwiceWithReset() throws SQLException {
         Connection connection1 = dataSource.getConnection();
         dataSource.resetConnection();
         Connection connection2 = dataSource.getConnection();
         assertNotSame(connection1, connection2);
     }

     @Test
     public void testResetConnection() throws SQLException {
         Connection connection = dataSource.getConnection();
         dataSource.resetConnection();
         assertNull(dataSource.getConnection());

         Connection newConnection = dataSource.getConnection();
         assertFalse(Objects.equals(connection, newConnection));
         assertTrue(newConnection.isValid(TIMEOUT));
     }

     @Test
     public void testAddInitializer() {
         ConnectionInitializer connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
         dataSource.addInitializer(connectionInitializerMock);
         assertTrue(dataSource.getInitializerList().contains(connectionInitializerMock));
     }

     @Test
     public void testClose() throws Exception {
         Connection connection = dataSource.getConnection();
         dataSource.close();
         assertNull(dataSource.getConnection());

         connection = dataSource.getConnection();
         connection.close();
     }

     @Test
     public void testCloseWithException() throws Exception {
         Connection connection = dataSource.getConnection();
         Mockito.doThrow(new SQLException("Test error")).when(connection).close();
         dataSource.close();
         assertNull(dataSource.getConnection());
     }

     @Test
     public void testOnConnectionCreated() throws Exception {
         Connection connection = Mockito.mock(Connection.class);
         dataSource.onConnectionCreated(connection);
         assertTrue(dataSource.getConnection() == connection);
     }

     @Test
     public void testOnConnectionReset() {
         Connection connection = Mockito.mock(Connection.class);
         ConnectionResetEvent event = new ConnectionResetEvent(connection);
         dataSource.onConnectionReset(connection);
         Mockito.verify(eventPublisherMock).publishEvent(event);
     }

     @After
     public void tearDown() throws Exception {
         if (connection != null) {
             connection.close();
         }
         DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD).close();
     }
 }