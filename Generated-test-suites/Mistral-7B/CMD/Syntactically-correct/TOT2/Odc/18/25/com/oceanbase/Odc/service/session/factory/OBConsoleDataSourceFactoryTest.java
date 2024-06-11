import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionConfig = Mockito.spy(new ConnectionConfig());
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = dataSourceFactory.getUsername();
 assertNotNull(username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 assertNotNull(password);
 }

 @Test
 public void testGetDefaultSchema() {
 String defaultSchema = dataSourceFactory.getDefaultSchema();
 assertNotNull(defaultSchema);
 }

 @Test
 public void testGetParameters() {
 Map<String, String> parameters = dataSourceFactory.getParameters();
 assertNotNull(parameters);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
 assertNotNull(deepCopy);
 }

 @Test
 public void testResetUsername() {
 String oldUsername = dataSourceFactory.getUsername();
 dataSourceFactory.resetUsername((username) -> "new_username");
 String newUsername = dataSourceFactory.getUsername();
 assertNotNull(newUsername);
 assertNotEquals(oldUsername, newUsername);
 }

 @Test
 public void testResetPassword() {
 String oldPassword = dataSourceFactory.getPassword();
 dataSourceFactory.resetPassword((password) -> "new_password");
 String newPassword = dataSourceFactory.getPassword();
 assertNotNull(newPassword);
 assertNotEquals(oldPassword, newPassword);
 }

 @Test
 public void testResetHost() {
 String oldHost = dataSourceFactory.getHost();
 dataSourceFactory.resetHost((host) -> "new_host");
 String newHost = dataSourceFactory.getHost();
 assertNotNull(newHost);
 assertNotEquals(oldHost, newHost);
 }

 @Test
 public void testResetPort() {
 Integer oldPort = dataSourceFactory.getPort();
 dataSourceFactory.resetPort((port) -> 12345);
 Integer newPort = dataSourceFactory.getPort();
 assertNotNull(newPort);
 assertNotEquals(oldPort, newPort);
 }

 @Test
 public void testResetSchema() {
 String oldSchema = dataSourceFactory.getDefaultSchema();
 dataSourceFactory.resetSchema((schema) -> "new_schema");
 String newSchema = dataSourceFactory.getDefaultSchema();
 assertNotNull(newSchema);
 assertNotEquals(oldSchema, newSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> oldParameters = dataSourceFactory.getParameters();
 dataSourceFactory.resetParameters((map) -> new HashMap<>());
 Map<String, String> newParameters = dataSourceFactory.getParameters();
 assertNotNull(newParameters);
 assertNotEquals(oldParameters, newParameters);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativePort() {
 dataSourceFactory.resetPort((port) -> -1);
 }

 @Test
 public void testGetDataSource_ConnectionExtensionPointIsNull() {
 ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn("org.h2.Driver");
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint);
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSource_ConnectionConfigIsNull() {
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
 assertNull(dataSourceFactory.getDataSource());
 }

}