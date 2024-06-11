package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.base.BaseClassBasedDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private EventPublisher eventPublisher;
 private Connection connectionMock;
 private ConnectionInitializer connectionInitializerMock;

 @Before
 public void setup() {
 this.dataSource = new SingleConnectionDataSource();
 this.eventPublisher = Mockito.mock(EventPublisher.class);
 this.connectionMock = Mockito.mock(Connection.class);
 this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test getting a connection for the first time
 Connection connection = this.dataSource.getConnection();
 assertNotNull(connection);
 assertNull(this.dataSource.connection);

 // Test getting a connection after it has been initialized
 this.dataSource.connection = this.connectionMock;
 Connection connection2 = this.dataSource.getConnection();
 assertNotNull(connection2);
 assertSame(connection, connection2);
 }

 @Test
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 assertThrows(SQLException.class, () -> this.dataSource.getConnection("invalid_username", "invalid_password"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 this.dataSource.connection = this.connectionMock;
 this.dataSource.resetConnection();
 assertNull(this.dataSource.connection);
 }

 @Test
 public void testAddInitializer() {
 this.dataSource.addInitializer(this.connectionInitializerMock);
 ArgumentCaptor<ConnectionInitializer> capture = ArgumentCaptor.forClass(ConnectionInitializer.class);
 Mockito.verify(this.dataSource).initConnection(Mockito.eq(this.connectionMock));
 Mockito.verify(this.connectionInitializerMock).init(Mockito.eq(this.connectionMock));
 }

 @Test
 public void testClose() {
 this.dataSource.close();
 assertNull(this.dataSource.connection);
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWhenConnectionIsOccupied() throws SQLException {
 SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();
 dataSource2.connection = this.connectionMock;
 dataSource2.lock = new ReentrantLock();
 Connection connection = dataSource2.getConnectionProxy(this.connectionMock);
 Mockito.doAnswer(invocation -> {
 if (tryLock()) {
 return connection;
 }
 }).when(dataSource2).getConnectionProxy(this.connectionMock);
 this.dataSource.getConnection();
 dataSource2.getConnection();
 }

 @After
 public void tearDown() throws SQLException {
 if (this.dataSource.connection != null) {
 this.dataSource.close();
 }
 }

}