package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory.ValueMapper;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 connectionConfig = new ConnectionConfig();
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setDefaultSchema("testschema");
 connectionConfig.setSslConfig(new SSLConfig(true));
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetDataSource() throws SQLException {
 // Test getting datasource with default parameters
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 DBUtils.closeQuietly(connection);

 // Test getting datasource with custom ConnectionInitializer
 ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Arrays.asList(connectionInitializer));
 DataSource dataSourceWithInitializer = dataSourceFactory.getDataSource();
 verify(connectionInitializer).initializeDatabase(dataSourceWithInitializer.getConnection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithInvalidConfiguration() {
 connectionConfig.setPort(-1);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 dataSourceFactory.getDataSource();
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertEquals("jdbc:obc:obmysql://localhost:3306/testschema?useSSL=true&trustServerCertificate=true&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false", jdbcUrl);
 }

 @Test
 public void testResetUsername() {
 ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
 when(mapper.map((String) any())).thenReturn("testUser");
 dataSourceFactory.resetUsername(mapper);
 assertEquals("testUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
 when(mapper.map((String) any())).thenReturn("testPassword");
 dataSourceFactory.resetPassword(mapper);
 assertEquals("testPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
 when(mapper.map((String) any())).thenReturn("localhost");
 dataSourceFactory.resetHost(mapper);
 assertEquals("localhost", dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 ValueMapper<Integer> mapper = Mockito.mock(ValueMapper.class);
 when(mapper.map((int) anyInt())).thenReturn(3307);
 dataSourceFactory.resetPort(mapper);
 assertEquals(3307, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
 when(mapper.map((String) any())).thenReturn("testSchema2");
 dataSourceFactory.resetSchema(mapper);
 assertEquals("testSchema2", dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 ValueMapper<Map<String, String>> mapper = Mockito.mock(ValueMapper.class);
 Map<String, String> parameters = new HashMap<>();
 parameters.put("testKey", "testValue");
 when(mapper.map((Map<String, String>) any())).thenReturn(parameters);
 dataSourceFactory.resetParameters(mapper);
 assertEquals(parameters, dataSourceFactory.getParameters());
 }

 @Test
 public void testGetUsername() {
 assertEquals("testuser", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("testpassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 assertEquals(ConnectType.OB_MYSQL, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() throws SQLException {
 OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
 DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
 Connection clonedConnection = clonedDataSource.getConnection();
 assertNotNull(clonedDataSource);
 assertNotNull(clonedConnection);
 DBUtils.closeQuietly(clonedConnection);
 }
}