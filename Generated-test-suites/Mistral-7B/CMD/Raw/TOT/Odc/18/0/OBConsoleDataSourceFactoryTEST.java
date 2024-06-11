package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 connectionConfig.setEventPublisher(eventPublisher = new EventPublisher());
 connectionConfig.setSslConfig(new SSLConfig(true, null, null));
 connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.MYSQL);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @After
 public void tearDown() throws SQLException {
 if (dataSourceFactory != null) {
 dataSourceFactory.getDataSource().close();
 }
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 assertNotNull(dataSource.getConnection());
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
 assertEquals("testUser", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 assertNotNull(password);
 assertEquals("testPassword", password);
 }

 @Test
 public void testGetDefaultSchema() {
 String defaultSchema = dataSourceFactory.getDefaultSchema();
 assertNotNull(defaultSchema);
 assertEquals("testSchema", defaultSchema);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();
 assertEquals(DialectType.MYSQL, dialectType);
 }

 @Test
 public void testGetDataSourceWithEmptyConnectionConfig() {
 ConnectionConfig emptyConnectionConfig = new ConnectionConfig();
 OBConsoleDataSourceFactory emptyDataSourceFactory = new OBConsoleDataSourceFactory(emptyConnectionConfig, true);
 assertNull(emptyDataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithNullConnectionConfig() {
 OBConsoleDataSourceFactory nullDataSourceFactory = new OBConsoleDataSourceFactory(null, true);
 assertNull(nullDataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptyUsername() {
 connectionConfig.setUsername(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptyPassword() {
 connectionConfig.setPassword(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptyHost() {
 connectionConfig.setHost(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptyPort() {
 connectionConfig.setPort(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidPort() {
 connectionConfig.setPort(-1);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptyDefaultSchema() {
 connectionConfig.setDefaultSchema(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithNullDataSource() {
 OBConsoleDataSourceFactory nullDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, null);
 assertNull(nullDataSourceFactory.getDataSource());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullConnectionConfigAndNullDataSource() {
 OBConsoleDataSourceFactory nullDataSourceFactory = new OBConsoleDataSourceFactory(null, null);
 }

 @Test
 public void testGetDataSourceWithNoInitConnection() {
 connectionConfig.setInitConnection(false);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithNullEventPublisher() {
 connectionConfig.setEventPublisher(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithEmptySslConfig() {
 connectionConfig.setSslConfig(new SSLConfig(false, null, null));
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidSslConfig() {
 connectionConfig.setSslConfig(new SSLConfig(null, null, null));
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidSslFileEntry() {
 connectionConfig.setSslConfig(new SSLConfig(true, null, new SSLFileEntry("", "")));
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithValidSslFileEntry() {
 connectionConfig.setSslConfig(new SSLConfig(true, null, new SSLFileEntry("test.p12", "testPassword")));
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionExtensionPointNull() {
 ConnectionPluginUtil.setConnectionExtension(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionExtensionPointFail() {
 ConnectionPluginUtil.setConnectionExtension(connectionExtensionPointMock -> {
 throw new RuntimeException("ConnectionPluginUtil error");
 });
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionInitializerNull() {
 connectionExtensionPoint.setConnectionInitializers(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionInitializerFail() {
 List<ConnectionInitializer> connectionInitializers = Arrays.asList(connectionInitializerMock -> {
 throw new RuntimeException("ConnectionInitializer error");
 });
 connectionExtensionPoint.setConnectionInitializers(connectionInitializers);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithSessionCreatedInitializerFail() {
 List<ConnectionInitializer> connectionInitializers = Arrays.asList(new SessionCreatedInitializer(connectionConfig) {
 @Override
 public void afterPropertiesSet() throws SQLException {
 throw new RuntimeException("SessionCreatedInitializer error");
 }
 });
 connectionExtensionPoint.setConnectionInitializers(connectionInitializers);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionPoolSizeLessThanOne() {
 connectionConfig.setDataSourcePoolSize(0);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithConnectionPoolSizeGreaterThanOne() {
 connectionConfig.setDataSourcePoolSize(2);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithDataSourceFactoryClassMismatch() {
 OBConsoleDataSourceFactory dataSourceFactoryWithWrongClass = new OBConsoleDataSourceFactory(connectionConfig, true) {
 @Override
 public DataSource getDataSource() {
 throw new RuntimeException("DataSourceFactory Class Mismatch");
 }
 };
 assertNull(dataSourceFactoryWithWrongClass.getDataSource());
 }

 @Test
 public void testGetDataSourceWithDataSourceTypeMismatch() {
 SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true) {
 @Override
 public DataSource getDataSource() {
 return singleConnectionDataSource;
 }
 };
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithDataSourceClosed() {
 SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
 singleConnectionDataSource.setClosed(true);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true) {
 @Override
 public DataSource getDataSource() {
 return singleConnectionDataSource;
 }
 };
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidDriverClassName() {
 String invalidDriverClassName = "com.invalid.driver.ClassName";
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true) {
 @Override
 public DataSource getDataSource() throws SQLException {
 Class<?> driverClass = Class.forName(invalidDriverClassName);
 return DriverManager.getConnection(dataSourceFactory.getJdbcUrl(), connectionConfig.getUsername(),
 connectionConfig.getPassword(), new Properties());
 }
 };
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidConnectionProperties() {
 Properties invalidConnectionProperties = new Properties();
 invalidConnectionProperties.setProperty("allowLoadLocalInfile", "true");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true) {
 @Override
 public DataSource getDataSource() throws SQLException {
 return new SingleConnectionDataSource(false);
 }

 @Override
 public Properties getConnectionProperties() {
 return invalidConnectionProperties;
 }
 };
 assertNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidConnectionInitializerProperties() {
 List<ConnectionInitializer> connectionInitializers = Arrays.asList(new ConnectionInitializer() {
 @Override
 public void afterPropertiesSet() throws SQLException {
 Connection connection = getConnection();
 connection.createStatement().execute("drop table if exists test_table");
 connection.commit();
 }

 @Override
 public void afterPropertiesSet() throws Exception {
 Map<String, String> properties = new HashMap<>();
 properties.put("invalid.property", "invalidValue");
 setConnectionInitializerProperties(properties);
 }
 });
 connectionExtensionPoint.setConnectionInitializers(connectionInitializers);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertNull(dataSourceFactory.getDataSource());
 }

 private Connection getConnection() throws SQLException {
 DriverManager.registerDriver(new com.mysql.jdbc.Driver());
 return DriverManager.getConnection(dataSourceFactory.getJdbcUrl(), connectionConfig.getUsername(),
 connectionConfig.getPassword());
 }

}