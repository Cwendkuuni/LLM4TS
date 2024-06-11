package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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

 private OBConsoleDataSourceFactory dataSourceFactory;
 private BasicDataSource basicDataSource;

 @Before
 public void setup() throws SQLException, ClassNotFoundException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(6306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setSslConfig(new SSLConfig(true, null));
 DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
 this.dataSourceFactory = (OBConsoleDataSourceFactory) dataSourceFactory;
 this.basicDataSource = (BasicDataSource) dataSourceFactory.getDataSource();
 }

 @After
 public void teardown() throws SQLException {
 this.basicDataSource.close();
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) this.dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = this.dataSourceFactory.getDialectType();
 assertEquals(DialectType.OB_MYSQL, dialectType);
 }

 @Test
 public void testGetUsername() {
 String username = this.dataSourceFactory.getUsername();
 assertEquals("testuser", username);
 }

 @Test
 public void testGetPassword() {
 String password = this.dataSourceFactory.getPassword();
 assertEquals("testpassword", password);
 }

 @Test
 public void testGetHost() {
 String host = this.dataSourceFactory.getHost();
 assertEquals("localhost", host);
 }

 @Test
 public void testGetPort() {
 Integer port = this.dataSourceFactory.getPort();
 assertEquals(6306, port.intValue());
 }

 @Test
 public void testGetDefaultSchema() {
 String defaultSchema = this.dataSourceFactory.getDefaultSchema();
 assertEquals("test_schema", defaultSchema);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
 String expectedJdbcUrl = "jdbc:mysql://localhost:6306/test_schema?allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath="
 + "&autoDeserialize=false&connectTimeout=5000&maxAllowedPacket=64000000&noDatetimeStringSync=true&useSSL=true&trustServerCertificate=true";
 assertEquals(expectedJdbcUrl, jdbcUrl);
 }

 @Test
 public void testGetConnection() throws SQLException {
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) this.dataSourceFactory.getDataSource();
 assertNotNull(dataSource.getConnection());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithNullDataSource() {
 OBConsoleDataSourceFactory dataSourceFactory = null;
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) dataSourceFactory.getDataSource();
 assertNull(dataSource);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithEmptyUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(6306);
 connectionConfig.setUsername("");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 try {
 dataSource.getConnection();
 fail("Expected SQLException.");
 } catch (SQLException e) {
 // expected
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithEmptyPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(6306);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 try {
 dataSource.getConnection();
 fail("Expected SQLException.");
 } catch (SQLException e) {
 // expected
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidPort() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(-1);
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 try {
 dataSource.getConnection();
 fail("Expected SQLException.");
 } catch (SQLException e) {
 // expected
 }
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithNullConnectionConfig() {
 OBConsoleDataSourceFactory dataSourceFactory = null;
 SingleConnectionDataSource dataSource = (SingleConnectionSessionDataSource) dataSourceFactory.getDataSource();
 assertNull(dataSource);
 }

 @Test
 public void testResetUsername() throws SQLException {
 String originalUsername = this.dataSourceFactory.getUsername();
 String newUsername = "newuser";
 this.dataSourceFactory.resetUsername((value) -> newUsername);
 assertEquals(newUsername, this.dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() throws SQLException {
 String originalPassword = this.dataSourceFactory.getPassword();
 String newPassword = "newpassword";
 this.dataSourceFactory.resetPassword((value) -> newPassword);
 assertEquals(newPassword, this.dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() throws SQLException {
 String originalHost = this.dataSourceFactory.getHost();
 String newHost = "newhost";
 this.dataSourceFactory.resetHost((value) -> newHost);
 assertEquals(newHost, this.dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() throws SQLException {
 Integer originalPort = this.dataSourceFactory.getPort();
 Integer newPort = 6307;
 this.dataSourceFactory.resetPort((value) -> newPort);
 assertEquals(newPort, this.dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() throws SQLException {
 String originalSchema = this.dataSourceFactory.getDefaultSchema();
 String newSchema = "new_schema";
 this.dataSourceFactory.resetSchema((value) -> newSchema);
 assertEquals(newSchema, this.dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() throws SQLException {
 Map<String, String> originalParameters = this.dataSourceFactory.getParameters();
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("testKey", "testValue");
 this.dataSourceFactory.resetParameters((value) -> newParameters);
 assertEquals(newParameters, this.dataSourceFactory.getParameters());
 }

 @Test
 public void testClone() throws CloneNotSupportedException, SQLException {
 OBConsoleDataSourceFactory originalDataSourceFactory = this.dataSourceFactory;
 OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) originalDataSourceFactory.deepCopy();
 SingleConnectionDataSource originalDataSource = (SingleConnectionSessionDataSource) originalDataSourceFactory.getDataSource();
 SingleConnectionDataSource clonedDataSource = (SingleConnectionSessionDataSource) clonedDataSourceFactory.getDataSource();
 assertNotSame(originalDataSource, clonedDataSource);
 assertEquals(originalDataSource.getConnection().getCatalog(), clonedDataSource.getConnection().getCatalog());
 }

}