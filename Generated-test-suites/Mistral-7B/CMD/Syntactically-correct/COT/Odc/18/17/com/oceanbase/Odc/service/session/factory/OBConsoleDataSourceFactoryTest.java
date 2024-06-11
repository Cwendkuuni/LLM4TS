import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DriverManagerDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private BasicDataSource basicDataSource;
 private SingleConnectionDataSource singleConnectionDataSource;

 @Before
 public void setUp() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig(true, null, null));
 this.connectionConfig = connectionConfig;
 this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 this.basicDataSource = new BasicDataSource();
 this.singleConnectionDataSource = new SingleConnectionDataSource(true);
 }

 @After
 public void tearDown() throws SQLException {
 if (this.basicDataSource != null) {
 this.basicDataSource.close();
 }
 if (this.singleConnectionDataSource != null) {
 this.singleConnectionDataSource.destroy();
 }
 }

 @Test
 public void testGetDataSource() {
 DataSource datasource = dataSourceFactory.getDataSource();
 assertNotNull(datasource);
 assertEquals(DataSources.MYSQL, datasource.getDriver());
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = dataSourceFactory.getUsername();
 assertEquals("user@schema", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 assertEquals("password", password);
 }

 @Test
 public void testGetConnectType() {
 ConnectType connectType = dataSourceFactory.getConnectType();
 assertEquals(ConnectType.MYSQL, connectType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullConnectionConfig() {
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
 assertThrows(IllegalArgumentException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setUsername(null);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertThrows(IllegalArgumentException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword(null);
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertThrows(IllegalArgumentException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullHost() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost(null);
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertThrows(IllegalArgumentException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullPort() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(0);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 assertThrows(IllegalArgumentException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test
 public void testGetDataSourceWithInitConnectionFalse() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig(true, null, null));
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
 assertNotNull(dataSourceFactory.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInitConnectionTrue() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig(true, null, null));
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 BasicDataSource basicDataSourceExpect = new BasicDataSource();
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertEquals(basicDataSourceExpect, dataSource);
 }

 @Test(expected = SQLException.class)
 public void testGetDataSourceWithNoDriverClass() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig(true, null, null));
 ConnectionPluginUtil connectionPluginUtil = Mockito.mock(ConnectionPluginUtil.class);
 Mockito.when(connectionPluginUtil.getDriverClassName(connectionConfig.getDialectType()))
 .thenReturn(null);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionPluginUtil);
 assertThrows(SQLException.class, () -> {
 dataSourceFactory.getDataSource();
 });
 }

 @Test
 public void testGetDataSourceWithInitConnectionInitializer() {
 ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, null, connectionInitializer);
 ConnectionInitializer[] initializers = { connectionInitializer };
 SingleConnectionDataSource singleConnectionDataSourceExpect = new SingleConnectionDataSource(true);
 singleConnectionDataSourceExpect.addInitializer(connectionInitializer);
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertEquals(singleConnectionDataSourceExpect, dataSource);
 }

}