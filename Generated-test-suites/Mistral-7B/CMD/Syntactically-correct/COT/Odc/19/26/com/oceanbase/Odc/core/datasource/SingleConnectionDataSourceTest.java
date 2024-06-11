package com.oceanbase.odc.core.datasource.test;

 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.concurrent.TimeUnit;
 import java.util.concurrent.locks.Lock;
 import java.util.concurrent.locks.ReentrantLock;

 import org.junit.After;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.MockitoAnnotations;

 import com.oceanbase.odc.common.event.EventPublisher;
 import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
 import com.oceanbase.odc.core.datasource.ConnectionInitializer;
 import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
 import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
 import com.oceanbase.odc.core.datasource.impl.ConnectionInitializerImpl;
 import com.oceanbase.odc.core.shared.exception.ConflictException;

 /**
 * Test for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @since 1.0.0
 */
 public class SingleConnectionDataSourceTest {

 private static final String DATABASE_URL = "jdbc:mock:mydb";
 private static final String DATABASE_USERNAME = "test";
 private static final String DATABASE_PASSWORD = "test";
 private static final int LOGIN_TIMEOUT = 10;
 private static final long TIMEOUT_MILLIS = 10 * 1000;

 private SingleConnectionDataSource dataSource;
 private EventPublisher eventPublisher;
 private Connection connection;
 private List<ConnectionInitializer> initializerList;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataSource = new SingleConnectionDataSource();
 eventPublisher = mock(EventPublisher.class);
 connection = mock(Connection.class);
 initializerList = new LinkedList<>();
 dataSource.setEventPublisher(eventPublisher);
 doReturn(connection).when(dataSource).innerCreateConnection();
 }

 @Test
 public void testGetConnection() throws SQLException {
 assertNotNull(dataSource.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 dataSource.setUsername("invalid_username");
 dataSource.setPassword("invalid_password");
 assertThrows(SQLException.class, () -> dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 ConnectionInitializer initializer = new ConnectionInitializerImpl();
 dataSource.addInitializer(initializer);
 assertTrue(dataSource.getInitializerList().contains(initializer));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 assertNull(dataSource.getConnection());
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionWithConnectionOccupied() throws SQLException, InterruptedException {
 SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();
 doReturn(dataSource2).when(dataSource).innerCreateConnection();
 dataSource2.setLock(new ReentrantLock());
 doReturn(true).when(dataSource2).tryLock();
 dataSource.resetConnection();
 assertThrows(ConflictException.class, () -> dataSource.getConnection());
 }

 @After
 public void tearDown() throws SQLException {
 closeQuietly(dataSource);
 closeQuietly(eventPublisher);
 closeQuietly(connection);
 }

 private void closeQuietly(Object obj) {
 if (obj != null) {
 try {
 obj.close();
 } catch (Throwable ignored) {
 // ignore
 }
 }
 }
 }