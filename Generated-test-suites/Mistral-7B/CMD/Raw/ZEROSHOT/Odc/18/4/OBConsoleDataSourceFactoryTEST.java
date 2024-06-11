package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory factory;
 private ConnectionConfig config;

 @Before
 public void setUp() {
 config = new ConnectionConfig();
 config.setHost("localhost");
 config.setPort(6000);
 config.setUsername("user");
 config.setPassword("password");
 config.setDefaultSchema("schema");
 config.setDialectType(DialectType.OB_MYSQL);
 config.setType(ConnectType.OB_MYSQL);
 config.setSslConfig(new SSLConfig(true, null, null));
 }

 @Test
 public void testOBConsoleDataSourceFactory() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertNotNull(factory);
 assertTrue(factory instanceof OBConsoleDataSourceFactory);
 assertNotNull(factory.getUsername());
 assertNotNull(factory.getPassword());
 assertNotNull(factory.getHost());
 assertNotNull(factory.getPort());
 assertNotNull(factory.getDefaultSchema());
 assertNotNull(factory.getConnectionConfig());
 assertFalse(factory.getInitConnection());
 assertNull(factory.getConnectionExtensionPoint());
 }

 @Test
 public void testGetDataSource() throws SQLException {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 DataSource dataSource = factory.getDataSource();
 assertNotNull(dataSource);
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 connection.close();
 }

 @Test
 public void testGetDialectType() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 DialectType dialectType = factory.getDialectType();
 assertTrue(DialectType.OB_MYSQL.equals(dialectType));
 }

 @Test
 public void testDeepCopy() {
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 OBConsoleDataSourceFactory clone = factory.deepCopy();
 assertNull(clone.getUsername());
 assertNull(clone.getPassword());
 assertNull(clone.getHost());
 assertNull(clone.getPort());
 assertNull(clone.getDefaultSchema());
 assertNull(clone.getConnectionConfig());
 assertFalse(clone.getInitConnection());
 assertNull(clone.getConnectionExtensionPoint());
 factory.resetUsername(mapper);
 factory.resetPassword(mapper);
 factory.resetHost(mapper);
 factory.resetPort(mapper);
 factory.resetSchema(mapper);
 factory.resetParameters(mapper);
 clone = factory.deepCopy();
 assertEquals(factory.getUsername(), clone.getUsername());
 assertEquals(factory.getPassword(), clone.getPassword());
 assertEquals(factory.getHost(), clone.getHost());
 assertEquals(factory.getPort(), clone.getPort());
 assertEquals(factory.getDefaultSchema(), clone.getDefaultSchema());
 assertEquals(factory.getConnectionConfig(), clone.getConnectionConfig());
 }

 @Test
 public void testResetUsername() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 String oldUsername = factory.getUsername();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetUsername(mapper);
 String newUsername = factory.getUsername();
 assertNotSame(oldUsername, newUsername);
 }

 @Test
 public void testResetPassword() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 String oldPassword = factory.getPassword();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetPassword(mapper);
 String newPassword = factory.getPassword();
 assertNotSame(oldPassword, newPassword);
 }

 @Test
 public void testResetHost() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 String oldHost = factory.getHost();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetHost(mapper);
 String newHost = factory.getHost();
 assertNotSame(oldHost, newHost);
 }

 @Test
 public void testResetPort() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 int oldPort = factory.getPort();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetPort(mapper);
 int newPort = factory.getPort();
 assertNotSame(oldPort, newPort);
 }

 @Test
 public void testResetSchema() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 String oldSchema = factory.getDefaultSchema();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetSchema(mapper);
 String newSchema = factory.getDefaultSchema();
 assertNotSame(oldSchema, newSchema);
 }

 @Test
 public void testResetParameters() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 Map<String, String> oldParameters = factory.getParameters();
 Map<String, String> newParameters = new HashMap<>();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetParameters(mapper);
 Map<String, String> parameters = factory.getParameters();
 assertEquals(newParameters, parameters);
 }

 @Test
 public void testSetInitConnection() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertFalse(factory.getInitConnection());
 factory.setInitConnection(true);
 assertTrue(factory.getInitConnection());
 }

 @Test
 public void testSetEventPublisher() {
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertNull(factory.getEventPublisher());
 factory.setEventPublisher(eventPublisher);
 assertSame(eventPublisher, factory.getEventPublisher());
 }

 @Test
 public void testSetConnectionExtensionPoint() {
 ConnectionPluginUtil connectionPluginUtil = Mockito.mock(ConnectionPluginUtil.class);
 ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertNull(factory.getConnectionExtensionPoint());
 factory.setConnectionExtensionPoint(connectionExtensionPoint);
 assertSame(connectionExtensionPoint, factory.getConnectionExtensionPoint());
 }

 @Test
 public void testGetJdbcUrl() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 String jdbcUrl = factory.getJdbcUrl();
 assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertEquals("user", factory.getUsername());
 }

 @Test
 public void testGetPassword() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertEquals("password", factory.getPassword());
 }

 @Test
 public void testGetHost() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertEquals("localhost", factory.getHost());
 }

 @Test
 public void testGetPort() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertEquals(6000, factory.getPort());
 }

 @Test
 public void testgetDefaultSchema() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 assertEquals("schema", factory.getDefaultSchema());
 }

 @Test
 public void testGetConnectionConfig() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 ConnectionConfig connectionConfig = factory.getConnectionConfig();
 assertEquals(config.getHost(), connectionConfig.getHost());
 assertEquals(config.getPort(), connectionConfig.getPort());
 assertEquals(config.getUsername(), connectionConfig.getUsername());
 assertEquals(config.getPassword(), connectionConfig.getPassword());
 assertEquals(config.getDefaultSchema(), connectionConfig.getDefaultSchema());
 assertEquals(config.getDialectType(), connectionConfig.getDialectType());
 assertEquals(config.getType(), connectionConfig.getType());
 assertEquals(config.getTenantName(), connectionConfig.getTenantName());
 assertEquals(config.getClusterName(), connectionConfig.getClusterName());
 assertEquals(config.getSslConfig(), connectionConfig.getSslConfig());
 assertEquals(config.getJdbcUrlParameters(), connectionConfig.getJdbcUrlParameters());
 assertEquals(config.getEventPublisher(), connectionConfig.getEventPublisher());
 assertEquals(config.getConnectionExtensionPoint(), connectionConfig.getConnectionExtensionPoint());
 }

 @Test
 public void testGetJdbcParams() {
 factory = new OBConsoleDataSourceFactory(config, true, true);
 Map<String, String> jdbcUrlParams = factory.getParameters();
 Map<String, String> expectedJdbcUrlParams = new HashMap<>();
 expectedJdbcUrlParams.put("maxAllowedPacket", "64000000");
 expectedJdbcUrlParams.put("allowMultiQueries", "true");
 expectedJdbcUrlParams.put("connectTimeout", "5000");
 expectedJdbcUrlParams.put("zeroDateTimeBehavior", "0");
 expectedJdbcUrlParams.put("noDatetimeStringSync", "true");
 expectedJdbcUrlParams.put("jdbcCompliantTruncation", "false");
 expectedJdbcUrlParams.put("sendConnectionAttributes", "false");
 assertNotNull(jdbcUrlParams);
 assertEquals(expectedJdbcUrlParams, jdbcUrlParams);
 }

 @Test
 public void testGenerateJdbcUrl() {
 ConnectionPluginUtil connectionPluginUtil = Mockito.mock(ConnectionPluginUtil.class);
 ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
 Mockito.anyString(), Mockito.anyMap())).thenReturn("jdbcUrl");
 factory = new OBConsoleDataSourceFactory(config, true, true);
 factory.setConnectionExtensionPoint(connectionExtensionPoint);
 String jdbcUrl = factory.getJdbcUrl();
 assertEquals("jdbcUrl", jdbcUrl);
 }

}