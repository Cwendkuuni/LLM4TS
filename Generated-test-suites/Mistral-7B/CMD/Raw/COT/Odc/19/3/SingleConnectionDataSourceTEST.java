package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.test.TestConnectionInitializer;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private Connection connection;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataSource = new SingleConnectionDataSource(false) {
 @Override
 public void addInitializer(ConnectionInitializer connectionInitializer) {
 super.addInitializer(connectionInitializer);
 }

 @Override
 protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
 return connection;
 }
 };
 dataSource.setEventPublisher(eventPublisher);
 }

 @After
 public void teardown() {
 dataSource.close();
 }

 @Test
 public void testGetConnection() throws SQLException {
 assertNotNull(dataSource.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 assertFalse(dataSource.getConnection().isValid(1000));
 try {
 dataSource.getConnection();
 fail();
 } catch (SQLException e) {
 // expected
 }
 }

 @Test
 public void testGetConnectionWithAutoCommit() throws SQLException {
 dataSource.setAutoCommit(true);
 Connection conn = dataSource.getConnection();
 assertFalse(conn.getAutoCommit());
 dataSource.setAutoCommit(true);
 assertEquals(true, conn.getAutoCommit());
 }

 @Test
 public void testGetConnectionWithAutoReconnect() throws SQLException {
 dataSource.setAutoReconnect(true);
 dataSource.resetConnection();
 assertNotNull(dataSource.getConnection());
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.setAutoReconnect(false);
 dataSource.getConnection();
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 List<ConnectionInitializer> initializers = new ArrayList<>();
 dataSource.addInitializer(new TestConnectionInitializer());
 dataSource.addInitializer(new TestConnectionInitializer());
 assertEquals(2, dataSource.getInitializerList().size());
 }

 @Test(expected = ConflictException.class)
 public void testTryLockWithTimeout() throws SQLException, InterruptedException {
 dataSource.tryLock();
 fail("Lock should not be acquired with timeout");
 }

 @Test
 public void testGetConnectionProxy() throws SQLException {
 assertNotNull(dataSource.getConnectionProxy(connection));
 }

 @Test
 public void testInitConnection() throws SQLException {
 dataSource.addInitializer(new TestConnectionInitializer());
 dataSource.getConnection();
 }

 @Test
 public void testOnConnectionCreated() {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 Connection connection = newConnectionFromDriver("username", "password");
 dataSource.onConnectionCreated(connection);
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
 @Override
 public void onConnectionReset(Connection connection) {
 super.onConnectionReset(connection);
 fail("onConnectionReset should not be invoked");
 }
 };
 dataSource.setEventPublisher(eventPublisher);
 dataSource.getConnection();
 dataSource.resetConnection();
 verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

}