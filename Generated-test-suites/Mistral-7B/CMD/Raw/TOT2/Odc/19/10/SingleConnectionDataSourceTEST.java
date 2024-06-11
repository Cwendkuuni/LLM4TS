import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connectionMock;
 private Connection initializerMock;
 private AutoCloseable closeable;

 @Before
 public void setup() throws Exception {
 dataSource = new SingleConnectionDataSource(true);
 connectionMock = Mockito.mock(Connection.class);
 initializerMock = Mockito.mock(Connection.class);
 closeable = Mockito.mock(AutoCloseable.class);

 DataSourceDriver.registerDriver();
 DataSourceDriver dsDriver = new DataSourceDriver();
 Class<?> clazz = DataSourceDriver.class;
 Object obj = clazz.newInstance();
 Method method = clazz.getMethod("getConnection", String[].class);
 Proxy proxy = (Proxy) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{DataSource.class}, new DataSourceInvocationHandler(obj, method));
 dataSource.setDriver((DataSource) proxy);
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertSame(connectionMock, connection);
 verify(connectionMock).isValid(10);
 }

 @Test
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 try {
 dataSource.getConnection("wrongUsername", "wrongPassword");
 } catch (SQLException e) {
 assertSame("Invalid username or password", e.getMessage());
 }
 }

 @Test
 public void testGetConnectionTwice() throws SQLException {
 Connection connection1 = dataSource.getConnection();
 Connection connection2 = dataSource.getConnection();
 assertSame(connection1, connection2);
 }

 @Test
 public void testClose() throws Exception {
 dataSource.close();
 assertNull(dataSource.getConnection());
 }

 @Test
 public void testTryLock() throws InterruptedException {
 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(mock(ConnectionInitializer.class));
 List<ConnectionInitializer> initializers = dataSource.getInitializerList();
 assertFalse(initializers.isEmpty());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 dataSource.innerCreateConnection();
 verify(connectionMock).isValid(10);
 }

 @Test
 public void testOnConnectionCreated() {
 ArgumentCaptor<Connection> connectionCaptor = ArgumentCaptor.forClass(Connection.class);
 dataSource.getConnection();
 verify(connectionMock).isValid(10);
 verify(connectionMock).createStatement();
 verify(connectionMock).close();
 verify(closeable).close();
 verify(dataSource).onConnectionCreated(connectionCaptor.capture());
 assertSame(connectionMock, connectionCaptor.getValue());
 }
}

class DataSourceInvocationHandler implements InvocationHandler {
 private final Object target;
 private final Method method;

 public DataSourceInvocationHandler(Object target, Method method) {
 this.target = target;
 this.method = method;
 }

 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 if ("getConnection".equals(method.getName())) {
 return getConnection((String[]) args);
 } else {
 return method.invoke(target, args);
 }
 }

 private Connection getConnection(String[] args) throws SQLException {
 Connection connection = DriverManager.getConnection("jdbc:default:connection:testdb", args[0], args[1]);
 return connection;
 }
}