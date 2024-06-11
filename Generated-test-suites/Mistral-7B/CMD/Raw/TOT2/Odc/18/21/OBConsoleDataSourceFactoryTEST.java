import static org.junit.Assert.*;
import static com.oceanbase.odc.core.shared.constant.OdcConstants.*;
import static com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 ConnectionConfig sslConfig = new ConnectionConfig();
 sslConfig.setSslConfig(new SSLConfig(true, null, null));
 connectionConfig.setSslConfig(sslConfig);
 this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
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
 assertEquals("testuser", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 assertEquals("testpassword", password);
 }

 @Test
 public void testGetDefaultSchema() {
 String defaultSchema = dataSourceFactory.getDefaultSchema();
 assertEquals("test_schema", defaultSchema);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();
 assertEquals(DialectType.OB_MYSQL, dialectType);
 }

 @Test
 public void testGetDataSourceWithInitialization() throws Exception {
 BasicDataSource basicDataSource = (BasicDataSource) dataSourceFactory.getDataSource();
 basicDataSource.setInitialSize(1);
 dataSourceFactory.initConnection();
 DataSource dataSource = basicDataSource.getConnection();
 assertNotNull(dataSource);
 dataSource.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithInvalidConnectionConfig() {
 ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
 invalidConnectionConfig.setHost("localhost");
 invalidConnectionConfig.setPort(-1);
 invalidConnectionConfig.setUsername("testuser");
 invalidConnectionConfig.setPassword("testpassword");
 invalidConnectionConfig.setDefaultSchema("test_schema");
 invalidConnectionConfig.setType(ConnectType.OB_MYSQL);
 invalidConnectionConfig.setDialectType(DialectType.OB_MYSQL);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(invalidConnectionConfig, true, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullConnectionConfig() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullAutoCommit() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), null, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullInitConnection() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullEventPublisher() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, true);
 obConsoleDataSourceFactory.setEventPublisher(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullConnectionExtensionPoint() {
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, true);
 obConsoleDataSourceFactory.setConnectionExtensionPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithEmptyParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 Map<String, String> emptyParameters = new HashMap<>();
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 obConsoleDataSourceFactory.setParameters(emptyParameters);
 }

 @Test(expected = IllegalArgumentException.class) public void testGetDataSourceWithNullJdbcUrl() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 obConsoleDataSourceFactory.setJdbcUrl(null);
 }

 @Test
 public void testGetDataSourceWithEmptyUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 String emptyUsername = obConsoleDataSourceFactory.getUsername();
 assertEquals("", emptyUsername);
 }

 @Test
 public void testGetDataSourceWithEmptyPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 String emptyPassword = obConsoleDataSourceFactory.getPassword();
 assertEquals("", emptyPassword);
 }

 @Test
 public void testGetDataSourceWithEmptyDefaultSchema() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 String emptyDefaultSchema = obConsoleDataSourceFactory.getDefaultSchema();
 assertEquals("", emptyDefaultSchema);
 }

 @Test
 public void testGetDataSourceWithEmptySslConfig() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 ConnectionConfig sslConfig = new ConnectionConfig();
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 obConsoleDataSourceFactory.setSslConfig(sslConfig);
 }

 @Test
 public void testGetDataSourceWithValidSslConfig() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(63306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 ConnectionConfig sslConfig = new ConnectionConfig();
 SSLConfig ssl = new SSLConfig(true, "/path/to/keystore.jks", "/path/to/truststore.jks");
 sslConfig.setSslConfig(ssl);
 OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 obConsoleDataSourceFactory.setSslConfig(sslConfig);
 }

}