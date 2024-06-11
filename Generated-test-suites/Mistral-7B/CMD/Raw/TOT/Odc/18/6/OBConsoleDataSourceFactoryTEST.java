package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource. ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory factory;
 private ConnectionConfig config;
 private ConnectionInitializer initializer;
 private EventPublisher eventPublisher;
 private ConnectionPluginUtil pluginUtil;

 @Before
 public void setUp() {
 this.config = new ConnectionConfig();
 this.config.setHost("localhost");
 this.config.setPort(3306);
 this.config.setUsername("test_user");
 this.config.setPassword("test_password");
 this.config.setDefaultSchema("test_schema");
 this.config.setDialectType(DialectType.MYSQL);
 this.config.setType(ConnectType.OB_MYSQL);
 this.config.setJdbcUrlParameters(new HashMap<String, String>() {{
 put("zeroDateTimeBehavior", "convertToNull");
 }});

 this.pluginUtil = Mockito.mock(ConnectionPluginUtil.class);
 this.eventPublisher = Mockito.mock(EventPublisher.class);
 this.initializer = Mockito.mock(ConnectionInitializer.class);

 this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
 }

 @Test
 public void testGetDataSource() throws SQLException {
 // Test getting data source with default parameters
 DataSource dataSource = this.factory.getDataSource();
 assertNotNull(dataSource);
 try (Connection connection = dataSource.getConnection()) {
 assertTrue(connection.isClosed());
 }

 // Test getting data source with custom connection initializer
 when(this.initializer.initializeDatabase(any(Connection.class)))
 .thenReturn(true);
 this.factory = new OBConsoleDataSourceFactory(this.config, true, true) {
 @Override
 public void addInitializer(ConnectionInitializer initializer) {
 super.addInitializer(initializer);
 this.initializer = initializer;
 }
 };
 dataSource = this.factory.getDataSource();
 verify(this.initializer).initializeDatabase(dataSource.getConnection());

 // Test getting data source with event publisher
 this.factory = new OBConsoleDataSourceFactory(this.config, true, true) {
 @Setter
 EventPublisher eventPublisher;
 };
 this.factory.setEventPublisher(this.eventPublisher);
 dataSource = this.factory.getDataSource();
 verify(this.eventPublisher).publishEvent(any());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithInvalidConfig() {
 this.config.setPort(-1);
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(this.config, true);
 factory.getDataSource();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullConfig() {
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(null, true);
 factory.getDataSource();
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullPluginUtil() {
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(this.config, true) {
 @Override
 public ConnectionPluginUtil getConnectionPluginUtil() {
 return null;
 }
 };
 factory.getDataSource();
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEventPublisher() {
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(this.config, true) {
 @Setter
 EventPublisher eventPublisher;

 @Override
 public EventPublisher getEventPublisher() {
 return null;
 }
 };
 factory.setEventPublisher(null);
 factory.getDataSource();
 fail("Expected NullPointerException");
 }
}