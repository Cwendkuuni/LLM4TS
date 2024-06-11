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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory factory;
 private ConnectionExtensionPoint extensionPoint;
 private ConnectionConfig config;
 private ValueMapper<String> mapper;
 private ConnectionInitializer initializer;

 @Before
 public void setUp() {
 this.mapper = Mockito.mock(ValueMapper.class);
 this.extensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 this.config = new ConnectionConfig();
 this.config.setUsername("username");
 this.config.setPassword("password");
 this.config.setHost("localhost");
 this.config.setPort(1234);
 this.config.setDefaultSchema("schema");
 this.config.setDialectType(DialectType.OB_ORACLE);
 this.config.setType(ConnectType.MYSQL);
 this.config.setSslConfig(new SSLConfig());
 this.config.setSslFileEntry(new SSLFileEntry());
 this.config.setJdbcUrlParameters(new HashMap<>());
 this.config.setEventPublisher(Mockito.mock(EventPublisher.class));

 when(this.extensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
 when(this.extensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
 when(this.extensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
 when(this.extensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
 .thenAnswer(new Answer<String>() {
 public String answer(InvocationOnMock invocation) throws Throwable {
 String host = (String) invocation.getArguments()[0];
 int port = (int) invocation.getArguments()[1];
 String schema = (String) invocation.getArguments()[2];
 Map<String, String> jdbcUrlParams = (Map<String, String>) invocation.getArguments()[3];
 return "jdbc:oracle:thin:@//" + host + ":" + port + "/" + schema;
 }
 });

 this.factory = new OBConsoleDataSourceFactory(this.config, true);
 this.initializer = Mockito.mock(ConnectionInitializer.class);
 }

 @Test
 public void testGetDataSource() throws SQLException {
 DataSource dataSource = this.factory.getDataSource();
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals("jdbc:oracle:thin:@//localhost:1234/schema", dataSource.getURL());
 }

 @Test
 public void testGetJdbcUrl() throws SQLException {
 String jdbcUrl = this.factory.getJdbcUrl();
 assertTrue(jdbcUrl.startsWith("jdbc:oracle:thin:@//"));
 assertEquals("localhost:1234/schema", jdbcUrl.substring(jdbcUrl.indexOf("/") + 1));
 }

 @Test
 public void testGetUsername() throws SQLException {
 String username = this.factory.getUsername();
 assertEquals("username", username);
 }

 @Test
 public void testGetPassword() throws SQLException {
 String password = this.factory.getPassword();
 assertEquals("password", password);
 }

 @Test
 public void testGetDialectType() throws SQLException {
 DialectType dialectType = this.factory.getDialectType();
 assertEquals(DialectType.OB_ORACLE, dialectType);
 }

 @Test
 public void testDeepCopy() throws SQLException {
 OBConsoleDataSourceFactory deepCopy = this.factory.deepCopy();
 assertEquals(this.factory.getUsername(), deepCopy.getUsername());
 assertEquals(this.factory.getPassword(), deepCopy.getPassword());
 assertEquals(this.factory.getHost(), deepCopy.getHost());
 assertEquals(this.factory.getPort(), deepCopy.getPort());
 assertEquals(this.factory.getDefaultSchema(), deepCopy.getDefaultSchema());
 assertEquals(this.factory.getParameters(), deepCopy.getParameters());
 }

 @Test
 public void testResetUsername() throws SQLException {
 String newUsername = "newUsername";
 this.factory.resetUsername(this.mapper);
 assertEquals(newUsername, this.factory.getUsername());
 verify(this.mapper, times(1)).map("username");
 }

 @Test
 public void testResetPassword() throws SQLException {
 String newPassword = "newPassword";
 this.factory.resetPassword(this.mapper);
 assertEquals(newPassword, this.factory.getPassword());
 verify(this.mapper, times(1)).map("password");
 }

 @Test
 public void testResetHost() throws SQLException {
 String newHost = "newHost";
 this.factory.resetHost(this.mapper);
 assertEquals(newHost, this.factory.getHost());
 verify(this.mapper, times(1)).map("localhost");
 }

 @Test
 public void testResetPort() throws SQLException {
 int newPort = 5678;
 this.factory.resetPort(this.mapper);
 assertEquals(newPort, this.factory.getPort());
 verify(this.mapper, times(1)).map(newPort);
 }

 @Test
 public void testResetSchema() throws SQLException {
 String newSchema = "newSchema";
 this.factory.resetSchema(this.mapper);
 assertEquals(newSchema, this.factory.getDefaultSchema());
 verify(this.mapper, times(1)).map("schema");
 }

 @Test
 public void testResetParameters() throws SQLException {
 Map<String, String> newParameters = new HashMap<>();
 this.factory.resetParameters(this.mapper);
 assertEquals(newParameters, this.factory.getParameters());
 verify(this.mapper, times(1)).map(this.factory.getParameters());
 }

 @Test
 public void testGetDataSourceWithInitializer() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 when(this.initializer.initializeDatabase()).thenReturn(connection);
 this.extensionPoint.addInitializer(this.initializer);
 DataSource dataSource = this.factory.getDataSource();
 assertNotNull(dataSource.getConnection());
 verify(this.initializer, times(1)).initializeDatabase();
 }

 @Test(expected = SQLException.class)
 public void testGetDataSourceWithInvalidUrl() throws SQLException {
 String jdbcUrl = "jdbc:oracle:thin:@//localhost:1234/";
 this.factory = new OBConsoleDataSourceFactory(this.config, true);
 this.factory.setJdbcUrl(jdbcUrl);
 DataSource dataSource = this.factory.getDataSource();
 fail("Should have thrown SQL exception");
 }
}