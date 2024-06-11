package com.oceanbase.odc.core.datasource.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertiones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SingleConnectionDataSourceTest {

 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private Connection connectionMock;
 @Mock
 private ConnectionInitializer connectionInitializerMock;
 @InjectMocks
 private SingleConnectionDataSource singleConnectionDataSource;

 private static Connection connection;

 @BeforeEach
 public void setUp() throws SQLException {
 connection = mock(Connection.class);
 singleConnectionDataSource = new SingleConnectionDataSource();
 singleConnectionDataSource.setEventPublisher(eventPublisher);
 singleConnectionDataSource.addInitializer(connectionInitializerMock);
 }

 @AfterAll
 public static void tearDown() {
 if (connection != null) {
 try {
 connection.close();
 } catch (SQLException e) {
 // ignore
 }
 }
 }

 @Test
 public void testGetConnection_whenCalledWithValidCredentials_shouldReturnConnection() throws SQLException {
 assertEquals(connection, singleConnectionDataSource.getConnection());
 }

 @Test
 public void testGetConnection_whenCalledTwiceWithSameCredentials_shouldReturnSameConnection() throws SQLException {
 Connection connection1 = singleConnectionDataSource.getConnection();
 Connection connection2 = singleConnectionDataSource.getConnection();
 assertEquals(connection1, connection2);
 }

 @Test
 public void testGetConnection_whenCalledWithInvalidCredentials_shouldThrowSQLException() {
 assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalidUser", "invalidPass"));
 }

 @Test
 public void testResetConnection_whenCalled_shouldResetConnection() throws SQLException {
 singleConnectionDataSource.resetConnection();
 assertNull(singleConnectionDataSource.connection);
 }

 @Test
 public void testAddInitializer_whenCalledWithValidConnectionInitializer_shouldAddToList() {
 singleConnectionDataSource.addInitializer(connectionInitializerMock);
 assertEquals(1, singleConnectionDataSource.initializerList.size());
 }

 @Test
 public void testGetConnection_whenCalledAfterAddingInitializer_shouldInitializeConnection() throws SQLException {
 verify(connectionInitializerMock).init(connectionMock);
 }

 @Test
 public void testClose_whenCalled_shouldCloseConnection() {
 singleConnectionDataSource.close();
 verify(connection).close();
 }

 @Test
 public void testClose_whenCalledAfterResetConnection_shouldNotCloseConnection() {
 singleConnectionDataSource.resetConnection();
 singleConnectionDataSource.close();
 verifyZeroInteractions(connection);
 }

 @Test
 public void testInnerCreateConnection_whenCalledTwice_shouldReturnSameConnection() throws SQLException {
 Connection connection1 = singleConnectionDataSource.innerCreateConnection();
 Connection connection2 = singleConnectionDataSource.innerCreateConnection();
 assertEquals(connection1, connection2);
 }

 @Test
 public void testOnConnectionReset_whenCalled_shouldPublishEvent() throws SQLException {
 ConnectionResetEvent event = new ConnectionResetEvent(connection);
 verify(eventPublisher).publishEvent(event);
 }

 @Test
 public void testInnerCreateConnection_whenCalledWithNullConnection_shouldThrowSQLException() {
 assertThrows(SQLException.class, () -> singleConnectionDataSource.innerCreateConnection(null));
 }

 @Test
 public void testGetConnectionProxy_whenCalledWithNullConnection_shouldThrowConflictException() {
 assertThrows(ConflictException.class, () -> singleConnectionDataSource.getConnectionProxy(null));
 }

 @Test
 public void testGetConnectionProxy_whenCalledWithClosedConnection_shouldThrowConflictException() {
 when(connectionMock.isClosed()).thenReturn(true);
 assertThrows(ConflictException.class, () -> singleConnectionDataSource.getConnectionProxy(connectionMock));
 }

 @Test
 public void testGetConnectionProxy_whenCalledWithValidConnection_shouldReturnConnectionProxy() throws SQLException {
 Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connectionMock);
 assertNotNull(connectionProxy);
 }

 @Test
 public void testTryLock_whenCalledWithTimeout_shouldReturnFalseIfLocked() {
 singleConnectionDataSource.lock = mock(Lock.class);
 when(singleConnectionDataSource.lock.tryLock(anyLong(), anyInt())).thenReturn(false);
 assertFalse(singleConnectionDataSource.tryLock());
 }

 @Test
 public void testTryLock_whenCalledWithTimeout_shouldReturnTrueIfNotLocked() {
 singleConnectionDataSource.lock = mock(Lock.class);
 when(singleConnectionDataSource.lock.tryLock(anyLong(), anyInt())).thenReturn(true);
 assertTrue(singleConnectionDataSource.tryLock());
 }

 @Test
 public void testPrepareConnection_whenCalledWithNullAutoCommit_shouldDoNothing() throws SQLException {
 singleConnectionDataSource.setAutoCommit(null);
 singleConnectionDataSource.prepareConnection(connectionMock);
 verifyZeroInteractions(connectionMock);
 }

 @Test
 public void testPrepareConnection_whenCalledWithAutoCommitFalse_shouldSetAutoCommitToFalse() throws SQLException {
 singleConnectionDataSource.setAutoCommit(false);
 singleConnectionDataSource.prepareConnection(connectionMock);
 verify(connectionMock).setAutoCommit(false);
 }

 @Test
 public void testPrepareConnection_whenCalledWithAutoCommitTrue_shouldDoNothing() throws SQLException {
 singleConnectionDataSource.setAutoCommit(true);
 singleConnectionDataSource.prepareConnection(connectionMock);
 verifyZeroInteractions(connectionMock);
 }

 @Test
 public void testInitConnection_whenCalledWithNullConnection_shouldThrowSQLException() throws SQLException {
 singleConnectionDataSource.initConnection(null);
 assertThrows(SQLException.class, () -> singleConnectionDataSource.initConnection(null));
 }

 @Test
 public void testInitConnection_whenCalledWithConnection_shouldInitializeConnection() throws SQLException {
 singleConnectionDataSource.initConnection(connectionMock);
 verify(connectionInitializerMock).init(connectionMock);
 }

 @Test
 public void testInitConnection_whenCalledMultipleTimesWithSameConnection_shouldNotInitializeTwice() throws SQLException {
 singleConnectionDataSource.initConnection(connectionMock);
 singleConnectionDataSource.initConnection(connectionMock);
 verify(connectionInitializerMock, times(1)).init(connectionMock);
 }

 @Test
 public void testOnConnectionCreated_whenCalled_shouldLogMessage() {
 verify(log, times(1)).debug("Connection is created");
 }

 @Test
 public void testOnConnectionCreated_whenCalledMultipleTimesWithSameConnection_shouldNotLogTwice() {
 singleConnectionDataSource.onConnectionCreated(connectionMock);
 singleConnectionDataSource.onConnectionCreated(connectionMock);
 verify(log, times(1)).debug("Connection is created");
 }

 @Test
 public void testInnerCreateConnection_whenCalledMultipleTimesWithSameCredentials_shouldReturnSameConnection() throws SQLException {
 SingleConnectionDataSource singleConnectionDataSource1 = new SingleConnectionDataSource();
 SingleConnectionDataSource singleConnectionDataSource2 = new SingleConnectionDataSource();
 assertEquals(singleConnectionDataSource1.innerCreateConnection(), singleConnectionDataSource2.innerCreateConnection());
 }

 @Test
 public void testInnerCreateConnection_whenCalledWithTimeout_shouldThrowSQLException() throws SQLException {
 singleConnectionDataSource.timeOutMillis = -1;
 assertThrows(SQLException.class, () -> singleConnectionDataSource.innerCreateConnection());
 }

 @Test
 public void testInnerCreateConnection_whenCalledWithValidCredentials_shouldReturnConnection() throws SQLException {
 Connection connection = singleConnectionDataSource.innerCreateConnection();
 assertNotNull(connection);
 }

 @Test
 public void testInnerCreateConnection_whenCalledWithInvalidCredentials_shouldThrowSQLException() {
 assertThrows(SQLException.class, () -> singleConnectionDataSource.innerCreateConnection());
 }

}