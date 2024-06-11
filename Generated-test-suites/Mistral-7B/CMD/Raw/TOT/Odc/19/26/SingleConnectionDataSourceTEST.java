package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 dataSource = new SingleConnectionDataSource();
 eventPublisher = Mockito.mock(EventPublisher.class);
 dataSource.setEventPublisher(eventPublisher);
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 assertEquals("The connection is not initialized", Connection.class, connection.getClass());
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 String username = "testUser";
 String password = "testPassword";
 dataSource.setUsername(username);
 dataSource.setPassword(password);
 Connection connection = dataSource.getConnection("testUser", "testPassword");
 assertNotNull(connection);
 assertEquals("The connection is not initialized with given username and password", Connection.class, connection.getClass());
 assertEquals(username, connection.getUserInfo().toString());
 assertEquals("The password is not matched", password, connection.getPassword());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 String invalidUsername = "invalidUser";
 String password = "testPassword";
 dataSource.setUsername(invalidUsername);
 dataSource.setPassword(password);
 assertThrows(SQLException.class, () -> dataSource.getConnection("testUser", "invalidPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 Connection initialConnection = dataSource.getConnection();
 dataSource.resetConnection();
 Connection newConnection = dataSource.getConnection();
 assertNotSame("The new connection should be different from the initial one", initialConnection, newConnection);
 }

 @Test
 public void testAddInitializer() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
 dataSource.addInitializer(initializer);
 assertEquals("The initializer size should be one after adding an initializer", 1, dataSource.getInitializerList().size());
 }

 @Test
 public void testCloseDataSource() {
 dataSource.close();
 assertNull("The connection should be closed and null", dataSource.getConnection());
 }

 @After
 public void tearDown() {
 try {
 if (connection != null) {
 connection.close();
 }
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
}