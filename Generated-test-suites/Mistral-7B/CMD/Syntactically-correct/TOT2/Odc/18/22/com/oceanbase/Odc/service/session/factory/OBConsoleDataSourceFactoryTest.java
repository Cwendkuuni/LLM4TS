package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.constant.OdcConstants;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(6000);
 connectionConfig.setUsername("test");
 connectionConfig.setPassword("test");
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.LONG);
 connectionConfig.setSslConfig(new SSLConfig(false, null, null));
 }

 @Test
 public void testOBConsoleDataSourceFactory_basic() {
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 }

 @Test(expected = SQLException.class)
 public void testOBConsoleDataSourceFactory_getDataSource_sqlException() throws SQLException {
 connectionConfig.setUsername(null);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_typeLong() {
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 assertNotNull(ds.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testOBConsoleDataSourceFactory_getDataSource_typeUnknown() {
 connectionConfig.setType(ConnectType.SHORT);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_dialectTypeOracle() {
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 }

 @Test(expected = SQLException.class)
 public void testOBConsoleDataSourceFactory_getDataSource_dialectTypeUnknown() {
 connectionConfig.setDialectType(DialectType.UNKNOWN);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_sslEnabled() {
 SSLConfig sslConfig = new SSLConfig(true, "testKeyStore.p12", "testPassword");
 connectionConfig.setSslConfig(sslConfig);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_noSsl() {
 SSLConfig sslConfig = new SSLConfig(false, null, null);
 connectionConfig.setSslConfig(sslConfig);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_initConnection() throws SQLException {
 connectionConfig.setInitConnection(true);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 ConnectionInitializer initializer = (ConnectionInitializer) ds.getConnection();
 initializer.afterPropertiesSet();
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_noInitConnection() throws SQLException {
 connectionConfig.setInitConnection(false);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 ConnectionInitializer initializer = (ConnectionInitializer) ds.getConnection();
 assertNull(initializer);
 }

 @Test
 public void testOBConsoleDataSourceFactory_getDataSource_connectionProperties() {
 Properties connectionProperties = new Properties();
 connectionProperties.setProperty("testProperty", "testValue");
 connectionConfig.setJdbcUrlParameters(connectionProperties);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 DataSource ds = dataSourceFactory.getDataSource();
 assertNotNull(ds);
 BasicDataSource basicDataSource = (BasicDataSource) ds;
 Map<String, Object> connectionPropertiesMap = basicDataSource.getConnectionProperties();
 String testProperty = (String) connectionPropertiesMap.get("testProperty");
 assertNotNull(testProperty);
 assertNotNull(testProperty.equals("testValue"));
 }

 @Test
 public void testOBConsoleDataSourceFactory_deepCopy() throws Exception {
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
 ConnectionConfig clonedConfig = clonedFactory.getConnectionConfig();
 String originalUsername = originalFactory.getUsername();
 String clonedUsername = clonedFactory.getUsername();
 assertNotNull(clonedFactory);
 assertNotNull(clonedConfig);
 assertNotNull(originalUsername);
 assertNotNull(clonedUsername);
 assertNotNull(originalUsername.equals(clonedUsername));
 }

}