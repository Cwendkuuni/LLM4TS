package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource singleConnectionDataSource;
 private Connection connection;
 private EventPublisher eventPublisher;
 private List<SingleConnectionDataSource.ConnectionInitializer> connectionInitializers;

 @Before
 public void setUp() throws Exception {
 singleConnectionDataSource = new SingleConnectionDataSource(true);
 connection = Mockito.mock(Connection.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 connectionInitializers = new LinkedList<>();
 singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 singleConnectionDataSource.setEventPublisher(eventPublisher);
 singleConnectionDataSource.setInitializerList(connectionInitializers);
 }

 @After
 public void tearDown() throws Exception {
 singleConnectionDataSource.close();
 }

 @Test
 public void testGetConnection_whenValidDataSource_shouldReturnConnection() throws SQLException {
 Connection actualConnection = singleConnectionDataSource.getConnection();
 assertTrue(actualConnection != null);
 }

 @Test(expected = SQLException.class)
 public void testGetConnection_whenDataSourceHasNoConnection_shouldThrowException() throws SQLException {
 singleConnectionDataSource.close();
 try {
 singleConnectionDataSource.getConnection();
 } catch (Exception e) {
 throw new SQLException("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetConnection_whenDataSourceHasClosedConnection_shouldReturnNewConnection() throws SQLException {
 singleConnectionDataSource.getConnection();
 singleConnectionDataSource.close();
 Connection actualConnection = singleConnectionDataSource.getConnection();
 assertNotSame(connection, actualConnection);
 }

 @Test(expected = ConflictException.class)
 public void testGetConnection_whenConnectionIsOccupied_shouldThrowException() throws SQLException {
 SingleConnectionDataSource anotherDataSource = new SingleConnectionDataSource();
 anotherDataSource.setLock(new ReentrantLock());
 Connection anotherConnection = anotherDataSource.getConnection();
 try {
 singleConnectionDataSource.getConnection();
 } catch (Exception e) {
 throw new ConflictException("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testResetConnection_whenConnectionIsReset_shouldResetConnection() throws SQLException {
 singleConnectionDataSource.resetConnection();
 assertEquals(Connection.HOLD_ABORTED, connection.getTransactionIsolation());
 }

 @Test
 public void testAddInitializer_whenInitializerAddedToDataSource_shouldAddInitializer() {
 singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
 @Override
 public void init(Connection connection) throws SQLException {
 // initialization logic here
 }
 });
 assertEquals(1, singleConnectionDataSource.getInitializerList().size());
 }

 @Test
 public void testEventPublisher_whenEventPublisherIsSet_shouldPublishEvent() throws SQLException {
 Mockito.when(eventPublisher.publishEvent(Mockito.any())).thenReturn(true);
 singleConnectionDataSource.resetConnection();
 Mockito.verify(eventPublisher).publishEvent(Mockito.any());
 }
}