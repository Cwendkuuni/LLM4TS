package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
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

 private OBConsoleDataSourceFactory factory;
 private DataSource dataSource;
 private EventPublisher mockEventPublisher;
 private ConnectionExtensionPoint mockConnectionExtensionPoint;
 private ConnectionConfig mockConnectionConfig;

 @Before
 public void setUp() {
 mockConnectionExtensionPoint = new ConnectionPluginUtil.DefaultConnectionExtensionPoint();
 mockConnectionConfig = new ConnectionConfig();
 mockConnectionConfig.setHost("127.0.0.1");
 mockConnectionConfig.setPort(3306);
 mockConnectionConfig.setUsername("testUser");
 mockConnectionConfig.setPassword("testPassword");
 mockConnectionConfig.setType(ConnectType.MYSQL);
 mockConnectionConfig.setDefaultSchema("testSchema");
 mockConnectionConfig.setDialectType(DialectType.MYSQL);
 mockConnectionConfig.setJdbcUrlParameters(new HashMap<>());
 mockConnectionConfig.setSslConfig(new SSLConfig(true));
 mockConnectionConfig.setSslFileEntry(new SSLFileEntry("test.p12", "testPassword"));
 mockEventPublisher = new EventPublisher();
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 }

 @After
 public void tearDown() throws SQLException {
 if (dataSource != null) {
 dataSource.close();
 }
 }

 @Test
 public void testGetDataSource() {
 dataSource = factory.getDataSource();
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 assertEquals(mockConnectionConfig.getUsername(), dataSource.getUsername());
 assertEquals(mockConnectionConfig.getPassword(), dataSource.getPassword());
 assertEquals(mockConnectionConfig.getType(), factory.getDialectType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithEmptyUsername() {
 mockConnectionConfig.setUsername(null);
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 try {
 factory.getDataSource();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Username can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithEmptyPassword() {
 mockConnectionConfig.setPassword(null);
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 try {
 factory.getDataSource();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Password can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullHost() {
 mockConnectionConfig.setHost(null);
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 try {
 factory.getDataSource();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Host can not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNegativePort() {
 mockConnectionConfig.setPort(-1);
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 try {
 factory.getDataSource();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Port can not be negative", e.getMessage());
 }
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = factory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("jdbc:mysql://" + mockConnectionConfig.getHost() + ":" + mockConnectionConfig.getPort()));
 assertTrue(jdbcUrl.contains(mockConnectionConfig.getDefaultSchema()));
 assertTrue(jdbcUrl.contains("useSSL=true"));
 assertTrue(jdbcUrl.contains("trustServerCertificate=true"));
 assertTrue(jdbcUrl.contains("trustStore=" + mockConnectionConfig.getSslFileEntry().getKeyStoreFilePath()));
 assertTrue(jdbcUrl.contains("trustStorePassword=" + mockConnectionConfig.getSslFileEntry().getKeyStoreFilePassword()));
 assertTrue(jdbcUrl.contains("keyStore=" + mockConnectionConfig.getSslFileEntry().getKeyStoreFilePath()));
 assertTrue(jdbcUrl.contains("keyStorePassword=" + mockConnectionConfig.getSslFileEntry().getKeyStoreFilePassword()));
 }

 @Test
 public void testResetValues() {
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 factory.resetUsername(mapper::mapString);
 factory.resetPassword(mapper::mapString);
 factory.resetHost(mapper::mapString);
 factory.resetPort(mapper::mapInt);
 factory.resetSchema(mapper::mapString);
 factory.resetParameters(mapper::mapHashMap);
 assertEquals(mapper.mapString(mockConnectionConfig.getUsername()), factory.getUsername());
 assertEquals(mapper.mapString(mockConnectionConfig.getPassword()), factory.getPassword());
 assertEquals(mapper.mapString(mockConnectionConfig.getHost()), factory.getHost());
 assertEquals(mockConnectionConfig.getPort(), factory.getPort());
 assertEquals(mapper.mapString(mockConnectionConfig.getDefaultSchema()), factory.getDefaultSchema());
 assertEquals(factory.getParameters(), mockConnectionConfig.getJdbcUrlParameters());
 }

 @Test
 public void testClone() {
 DataSourceFactory clone = factory.deepCopy();
 assertEquals(factory.getDataSource(), clone.getDataSource());
 assertEquals(factory.getUsername(), clone.getUsername());
 assertEquals(factory.getPassword(), clone.getPassword());
 assertEquals(factory.getHost(), clone.getHost());
 assertEquals(factory.getPort(), clone.getPort());
 assertEquals(factory.getDefaultSchema(), clone.getDefaultSchema());
 assertEquals(factory.getParameters(), clone.getParameters());
 }

 @Test
 public void testInitializeConnections() throws SQLException {
 List<ConnectionInitializer> initializers = mockConnectionExtensionPoint.getConnectionInitializers();
 factory = new OBConsoleDataSourceFactory(mockConnectionConfig, true, true);
 factory.addInitializer(new SessionCreatedInitializer(mockConnectionConfig));
 dataSource = factory.getDataSource();
 initializers.forEach(initializer -> {
 try {
 initializer.initializeDatabase(dataSource.getConnection());
 } catch (SQLException e) {
 fail("Failed to initialize database: " + e.getMessage());
 }
 });
 }

}