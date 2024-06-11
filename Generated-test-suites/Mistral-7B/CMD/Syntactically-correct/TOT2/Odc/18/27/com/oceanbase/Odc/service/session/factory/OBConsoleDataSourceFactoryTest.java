import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DriverManagerDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private DataSourceFactory factory;
 private ConnectionConfig config;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 config = new ConnectionConfig();
 config.setHost("localhost");
 config.setPort(3306);
 config.setUsername("testuser");
 config.setPassword("testpassword");
 config.setDialectType(DialectType.OB_MYSQL);
 config.setType(ConnectType.JDBC);
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 eventPublisher = mock(EventPublisher.class);
 factory = new OBConsoleDataSourceFactory(config, false, true);
 }

 @After
 public void tearDown() throws Exception {
 if (factory instanceof PoolingDataSource) {
 ((PoolingDataSource) factory).close();
 }
 }

 @Test
 public void testGetDataSource() throws Exception {
 DataSource dataSource = factory.getDataSource();
 assertNotNull(dataSource);
 assertEquals(config.getDialectType(), dataSource.getDialect());
 Connection connection = dataSource.getConnection();
 try {
 assertEquals(config.getUsername(), connection.createStatement().executeQuery("SELECT USER()").nextString());
 } finally {
 connection.close();
 }
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = factory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("jdbc:" + config.getDialectType().getDriverClassName() + ":"));
 assertTrue(jdbcUrl.contains(config.getHost()));
 assertTrue(jdbcUrl.contains(config.getPort() + ""));
 assertTrue(jdbcUrl.contains(config.getDefaultSchema()));
 }

 @Test
 public void testGetConnection() throws Exception {
 Connection connection = factory.getDataSource().getConnection();
 assertNotNull(connection);
 connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
 connection.createStatement().execute("INSERT INTO test_table VALUES (1)");
 connection.createStatement().execute("SELECT * FROM test_table");
 connection.commit();
 connection.close();
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithException() throws Exception {
 Connection connection = mock(Connection.class);
 when(connection.createStatement()).thenThrow(new SQLException("Test error"));
 when(factory.getDataSource().getConnection()).thenReturn(connection);
 try {
 factory.getDataSource().getConnection();
 fail("Should throw SQLException");
 } catch (SQLException e) {
 assertEquals("Test error", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConfig() {
 config.setHost(null);
 factory = new OBConsoleDataSourceFactory(config, false, true);
 try {
 factory.getDataSource();
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Host can not be null", e.getMessage());
 }
 }

 @Test
 public void testCloneableDataSourceFactory() {
 DataSourceFactory clonedFactory = factory.deepCopy();
 assertEquals(config, clonedFactory.getConnectionConfig());
 assertEquals(factory.getUsername(), clonedFactory.getUsername());
 assertEquals(factory.getPassword(), clonedFactory.getPassword());
 assertEquals(factory.getHost(), clonedFactory.getHost());
 assertEquals(factory.getPort(), clonedFactory.getPort());
 assertEquals(factory.getDefaultSchema(), clonedFactory.getDefaultSchema());
 assertEquals(factory.getParameters(), clonedFactory.getParameters());
 assertEquals(factory.getDialectType(), clonedFactory.getDialectType());
 assertEquals(factory.getAutoCommit(), clonedFactory.getAutoCommit());
 assertEquals(factory.getInitConnection(), clonedFactory.getInitConnection());
 assertEquals(factory.getEventPublisher(), clonedFactory.getEventPublisher());
 assertEquals(factory.getConnectionExtensionPoint(), clonedFactory.getConnectionExtensionPoint());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithSslConfig() throws Exception {
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 sslConfig.setTrustStoreFile("test.p12");
 sslConfig.setTrustStorePassword("testpassword");
 config.setSslConfig(sslConfig);
 factory = new OBConsoleDataSourceFactory(config, false, true);
 DataSource dataSource = factory.getDataSource();
 assertNotNull(dataSource);
 Connection connection = dataSource.getConnection();
 try {
 // This test case is not fully covered as it depends on actual SSL certificate
 // verification logic in oceanbase-jdbc driver. It is recommended to test SSL
 // configuration with actual oceanbase-jdbc driver instead of mocking it.
 String certificateAlias = "test_certificate";
 String certificatePassword = "testpassword";
 Properties properties = new Properties();
 properties.setProperty("trustStore", "classpath:test.p12");
 properties.setProperty("trustStorePassword", certificatePassword);
 properties.setProperty("keyStore", "classpath:test.p12");
 properties.setProperty("keyStorePassword", certificatePassword);
 DriverManagerDataSource ds = new DriverManagerDataSource();
 ds.setURL(factory.getJdbcUrl());
 ds.setDriverClassName(factory.getDialectType().getDriverClassName());
 ds.setConnectionProperties(properties);
 Connection conn = ds.getConnection();
 assertNotNull(conn);
 assertNotNull(conn.getCertificate());
 String alias = conn.getCertificate().getAlias();
 assertEquals(certificateAlias, alias);
 connection.close();
 } finally {
 connection.close();
 }
 }
}