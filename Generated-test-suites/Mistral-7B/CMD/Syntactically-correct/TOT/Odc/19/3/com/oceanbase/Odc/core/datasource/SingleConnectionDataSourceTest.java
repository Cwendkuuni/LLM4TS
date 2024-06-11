package com.oceanbase.odc.core.datasource;

import static com.oceanbase.odc.common.event.EventPublisher.DEFAULT_PUBLISHER;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.exception.ConflictException;
import com.oceanbase.odc.core.datasource.exception.SQLException;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private EventPublisher eventPublisher;
 private Connection connection;
 private ConnectionInitializer connectionInitializer;

 @Before
 public void setUp() throws Exception {
 this.dataSource = new SingleConnectionDataSource();
 this.eventPublisher = Mockito.spy(DEFAULT_PUBLISHER);
 this.dataSource.setEventPublisher(this.eventPublisher);
 this.connection = Mockito.mock(Connection.class);
 this.connectionInitializer = Mockito.mock(ConnectionInitializer.class);
 }

 @Test
 public void testGetConnection() throws SQLException {
 assertEquals("Expected data source to return same connection on multiple calls", this.connection,
 this.dataSource.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 assertNotEquals("Expected data source to reject user with invalid credentials", this.connection,
 this.dataSource.getConnection("invalidUsername", "invalidPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 this.dataSource.resetConnection();
 assertNull("Expected connection to be null after reset", this.dataSource.getConnection());
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionOccupied() throws SQLException, InterruptedException {
 this.dataSource.setLock(new ReentrantLock());
 this.dataSource.addInitializer(this.connectionInitializer);
 this.dataSource.getConnection();
 Mockito.doThrow(new InterruptedException()).when(this.connectionInitializer).init(this.connection);
 this.dataSource.getConnection();
 }

 @Test
 public void testAddInitializer() {
 this.dataSource.addInitializer(this.connectionInitializer);
 Mockito.verify(this.connectionInitializer).init(this.connection);
 }

 @Test
 public void testClose() throws SQLException, InterruptedException {
 Connection newConnection = Mockito.mock(Connection.class);
 this.dataSource.setConnection(newConnection);
 Mockito.doThrow(new InterruptedException()).when(this.connection).close();
 this.dataSource.close();
 Mockito.verify(this.connection).close();
 }

 @Test
 public void testCloseTwice() throws SQLException {
 Connection newConnection = Mockito.mock(Connection.class);
 this.dataSource.setConnection(newConnection);
 this.dataSource.close();
 this.dataSource.close();
 Mockito.verify(newConnection, Mockito.times(1)).close();
 }

 @Test
 public void testOnConnectionReset() {
 this.dataSource.onConnectionReset(this.connection);
 Mockito.verify(this.eventPublisher).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
 }

 @After
 public void tearDown() throws Exception {
 this.dataSource.close();
 }

}