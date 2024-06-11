package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SingleConnectionDataSourceTest {

  private static final String USERNAME = "testUser";
  private static final String PASSWORD = "testPassword";
  private static final String JDBC_URL = "jdbc:test:ds";
  private static final int TIMEOUT = 10 * 1000;
  private static final long LOGIN_TIMEOUT = 10 * 1000;

  private EventPublisher eventPublisher;
  private SingleConnectionDataSource dataSource;
  private Connection connection;

  @Before
  public void setUp() throws Exception {
    Class.forName("com.test.jdbc.Driver");
    connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    Statement statement = connection.createStatement();
    statement.execute("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)");
    eventPublisher = mock(EventPublisher.class);
    dataSource = new SingleConnectionDataSource(true);
    dataSource.setEventPublisher(eventPublisher);
  }

  @After
  public void tearDown() throws Exception {
    connection.close();
    dataSource.close();
    statement.close();
    Class.forName("com.test.jdbc.Driver").getDeclaredMethod("shutdown").invoke(null);
  }

  @Test
  public void testGetConnectionWithUsernameAndPassword() throws Exception {
    assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));

    Connection connection = dataSource.getConnection();
    assertSame(connection, dataSource.getConnection());
    assertFalse(dataSource.getConnection().isClosed());
  }

  @Test
  public void testGetConnectionWithoutParameters() throws Exception {
    Connection connection = dataSource.getConnection();
    assertSame(connection, dataSource.getConnection());
    assertFalse(dataSource.getConnection().isClosed());
  }

  @Test(expected = SQLException.class)
  public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
    assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", PASSWORD));
    assertThrows(SQLException.class, () -> dataSource.getConnection(USERNAME, "invalidPassword"));
  }

  @Test
  public void testResetConnection() throws Exception {
    dataSource.resetConnection();
    assertNull(dataSource.getConnection());

    Connection connection = dataSource.getConnection();
    assertSame(connection, dataSource.getConnection());
    assertFalse(dataSource.getConnection().isClosed());

    dataSource.resetConnection();
    assertNull(dataSource.getConnection());
  }

  @Test
  public void testAddInitializer() {
    ConnectionInitializer initializer = mock(ConnectionInitializer.class);
    dataSource.addInitializer(initializer);
    assertTrue(dataSource.getInitializerList().contains(initializer));
  }

  @Test(expected = NullPointerException.class)
  public void testAddNullInitializer() {
    dataSource.addInitializer(null);
  }

  @Test
  public void testClose() throws Exception {
    dataSource.getConnection();
    dataSource.close();
    assertNull(dataSource.getConnection());
  }

  @Test
  public void testPrepareConnection() throws Exception {
    Boolean autoCommit = true;
    Connection connection = dataSource.getConnection();
    dataSource.prepareConnection(connection);
    assertTrue(connection.getAutoCommit() == autoCommit);
  }

  @Test
  public void testTryLockSuccess() throws InterruptedException, SQLException {
    assertTrue(dataSource.tryLock());
  }

  @Test
  public void testTryLockFailure() throws InterruptedException, SQLException {
    dataSource.setTimeOutMillis(0);
    assertFalse(dataSource.tryLock());
  }

  @Test
  public void testGetConnectionProxy() throws SQLException {
    Connection connection = dataSource.getConnection();
    CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
    Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] {Connection.class}, handler);
    assertSame(connection, proxy);
  }

  @Test
  public void testInitConnection() throws SQLException {
    Connection connection = dataSource.getConnection();
    dataSource.initConnection(connection);
    verify(eventPublisher).publishEvent(argThat(e -> e instanceof ConnectionResetEvent));
  }

  @Test
  public void testOnConnectionReset() throws Exception {
    Connection connection = dataSource.getConnection();
    dataSource.onConnectionReset(connection);
    verify(eventPublisher).publishEvent(argThat(e -> e instanceof ConnectionResetEvent));
  }

  @Test
  public void testInnerCreateConnection() throws SQLException {
    Connection connection = dataSource.innerCreateConnection();
    assertSame(connection, dataSource.getConnection());
    assertFalse(dataSource.getConnection().isClosed());
  }

  @Test(expected = IllegalStateException.class)
  public void testInnerCreateConnectionWithConnectionNotNull() throws Exception {
    dataSource.setConnection(connection);
    dataSource.innerCreateConnection();
  }

  @Test
  public void testCloseIgnoreInvocationHandler() throws SQLException {
    CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
    assertNull(handler.invoke(handler, new Object[0]));
  }

  @Test
  public void testIsWrapperFor() throws SQLException {
    assertTrue(Connection.class.isAssignableFrom(Connection.class));
    assertTrue(Connection.class.isInstance(connection));
    assertTrue(Connection.class.isWrapperFor(connection));
  }

  @Test
  public void testEquals() throws SQLException {
    assertTrue(connection.equals(connection));
    assertFalse(connection.equals(null));
    assertFalse(connection.equals(new Object()));
  }

  @Test
  public void testHashCode() throws SQLException {
    int hashCode = connection.hashCode();
    assertTrue(connection.hashCode() == connection.hashCode());
  }

  @Test
  public void testIsClosed() throws SQLException {
    assertFalse(connection.isClosed());
  }

  private ArgumentCaptor<ConnectionResetEvent> connectionResetEventArgumentCaptor =
      ArgumentCaptor.forClass(ConnectionResetEvent.class);

  private ArgumentCaptor<Throwable> throwableArgumentCaptor = ArgumentCaptor.forClass(Throwable.class);

  private static ArgumentCaptor<Throwable> throwableArgumentCaptor(Class<?> clazz) {
    return ArgumentCaptor.forClass(clazz);
  }
}