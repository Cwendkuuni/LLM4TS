package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private ConnectionInitializer connectionInitializerMock;
 private EventPublisher eventPublisherMock;

 @Before
 public void setUp() {
 connectionMock = Mockito.mock(Connection.class);
 connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
 eventPublisherMock = Mockito.mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource() {
 @Override
 public void addInitializer(ConnectionInitializer connectionInitializer) {
 super.addInitializer(connectionInitializerMock);
 }

 @Override
 public EventPublisher getEventPublisher() {
 return eventPublisherMock;
 }
 };
 DataSourceUtils.setDataSource(dataSource);
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test normal scenario
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 // Test connection reset
 dataSource.resetConnection();
 Connection connectionReset = dataSource.getConnection();
 assertNotNull(connectionReset);
 // Test connection initializer
 Mockito.doAnswer(invocation -> connectionMock).when(connectionInitializerMock).init(connectionMock);
 dataSource.addInitializer(connectionInitializerMock);
 Connection connectionInitialized = dataSource.getConnection();
 assertEquals(connectionMock, connectionInitialized);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 try {
 dataSource.getConnection();
 fail();
 } catch (SQLException e) {
 // Expected exception
 }
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionTimeout() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(1);
 try {
 dataSource.getConnection();
 fail();
 } catch (ConflictException e) {
 // Expected exception
 }
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 Connection connectionAfterReset = dataSource.getConnection();
 assertNotNull(connectionAfterReset);
 }

 @Test
 public void testEventPublisher() throws SQLException, InterruptedException {
 Connection connection = Mockito.mock(Connection.class);
 Mockito.doAnswer(invocation -> connection).when(connectionMock).getMetaData();
 dataSource.setEventPublisher(eventPublisherMock);
 dataSource.addInitializer(connectionMock -> {});
 dataSource.getConnection();
 Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
 }
}