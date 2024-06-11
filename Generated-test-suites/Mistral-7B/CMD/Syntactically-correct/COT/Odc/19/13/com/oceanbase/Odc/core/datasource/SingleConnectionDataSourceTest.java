package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

 private List<ConnectionInitializer> initializerList;
 @Mock
 private Connection connection;
 @Mock
 private EventPublisher eventPublisher;
 @InjectMocks
 private SingleConnectionDataSource dataSource;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 initializerList = new ArrayList<>();
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 }

 @After
 public void tearDown() throws Exception {
 dataSource.close();
 }

 /**
 * Test method for {@link SingleConnectionDataSource#getConnection()}
 */
 @Test
 public void testGetConnection() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);

 // test closed connection scenario
 dataSource.resetConnection();
 try {
 dataSource.getConnection();
 fail("Expected an exception");
 } catch (SQLException e) {
 // expected
 }

 // test invalid username and password scenario
 try {
 dataSource.getConnection("invalidUsername", "invalidPassword");
 fail("Expected an exception");
 } catch (SQLException e) {
 assertEquals("Invalid username or password", e.getMessage());
 }
 }

 /**
 * Test method for {@link SingleConnectionDataSource#resetConnection()}
 */
 @Test
 public void testResetConnection() throws SQLException {
 // test normal scenario
 dataSource.resetConnection();
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);

 // test concurrent access scenario
 Connection connection1 = dataSource.getConnection();
 Connection connection2 = dataSource.getConnection();
 assertNotSame(connection1, connection2);
 }

 /**
 * Test method for {@link SingleConnectionDataSource#addInitializer(ConnectionInitializer)}
 */
 @Test
 public void testAddInitializer() throws SQLException {
 // test normal scenario
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 Connection connection = dataSource.getConnection();
 // initialization logic should be executed

 // test duplicate initializer scenario
 dataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 Connection connection2 = dataSource.getConnection();
 // initialization logic should be executed only once
 }

 /**
 * Test method for {@link SingleConnectionDataSource#close()}
 */
 @Test(expected = SQLException.class)
 public void testClose() throws SQLException {
 dataSource.close();
 dataSource.getConnection();
 fail("Expected an exception");
 }

 /**
 * Test method for {@link SingleConnectionDataSource#prepareConnection(Connection)}
 */
 @Test
 public void testPrepareConnection() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.getConnection();
 dataSource.prepareConnection(connection);
 assertTrue(connection.isAutoCommit());
 connection.setAutoCommit(false);
 dataSource.prepareConnection(connection);
 assertFalse(connection.isAutoCommit());

 // test autoCommit is null scenario
 dataSource.setAutoCommit(null);
 Connection connection2 = dataSource.getConnection();
 dataSource.prepareConnection(connection2);
 assertTrue(connection2.isAutoCommit());
 }

 /**
 * Test method for {@link SingleConnectionDataSource#tryLock()}
 */
 @Test
 public void testTryLock() throws InterruptedException {
 // test normal scenario
 assertTrue(dataSource.tryLock());

 // test timeout scenario
 assertFalse(dataSource.tryLock(1, TimeUnit.MILLISECONDS));

 // test interrupted scenario
 Thread thread = new Thread(() -> {
 try {
 dataSource.tryLock();
 fail("Expected an interruption");
 } catch (InterruptedException e) {
 // expected
 }
 });
 thread.start();
 thread.join();
 }

 /**
 * Test method for {@link SingleConnectionDataSource#getConnectionProxy(Connection)}
 */
 @Test
 public void testGetConnectionProxy() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.getConnection();
 Connection connectionProxy = dataSource.getConnectionProxy(connection);
 assertSame(connection, connectionProxy);

 // test concurrent access scenario
 Connection connection1 = dataSource.getConnection();
 Connection connection2 = dataSource.getConnection();
 Connection connectionProxy1 = dataSource.getConnectionProxy(connection1);
 Connection connectionProxy2 = dataSource.getConnectionProxy(connection2);
 assertNotSame(connectionProxy1, connectionProxy2);
 }

 /**
 * Test method for {@link SingleConnectionDataSource#initConnection(Connection)}
 */
 @Test(expected = ConflictException.class)
 public void testInitConnection() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.getConnection();
 dataSource.initConnection(connection);
 // initialization logic should be executed

 // test concurrent access scenario
 Thread thread1 = new Thread(() -> {
 try {
 dataSource.initConnection(dataSource.getConnection());
 fail("Expected an exception");
 } catch (ConflictException e) {
 // expected
 }
 });
 Thread thread2 = new Thread(() -> {
 try {
 dataSource.initConnection(dataSource.getConnection());
 fail("Expected an exception");
 } catch (ConflictException e) {
 // expected
 }
 });
 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();
 }

 /**
 * Test method for {@link SingleConnectionDataSource#onConnectionCreated(Connection)}
 */
 @Test
 public void testOnConnectionCreated() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.getConnection();
 dataSource.onConnectionCreated(connection);

 // test concurrent access scenario
 Thread thread = new Thread(() -> {
 try {
 dataSource.getConnection();
 dataSource.onConnectionCreated(dataSource.getConnection());
 fail("Expected an exception");
 } catch (SQLException e) {
 // expected
 }
 });
 thread.start();
 thread.join();
 }

 /**
 * Test method for {@link SingleConnectionDataSource#innerCreateConnection()}
 */
 @Test(expected = SQLException.class)
 public void testInnerCreateConnection() throws SQLException {
 // test normal scenario
 Connection connection = dataSource.innerCreateConnection();
 assertNotNull(connection);

 // test exception scenario
 throw new RuntimeException("Test exception");
 }
}