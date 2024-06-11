import org.apache.commons.dbcp2.BasicDatasetDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.SSLConfig;
import com.oceanbase.odc.service.session.factory.SSLFileEntry;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private DataSource dataSource;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testuser");
 connectionConfig.setPassword("testpassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testschema");
 connectionConfig.setType(ConnectType.MYSQL);
 connectionConfig.setJdbcUrlParameters(new HashMap<String, String>() {{
 put("allowLoadLocalInfile", "false");
 put("allowUrlInLocalInfile", "false");
 put("allowLoadLocalInfileInPath", "");
 put("autoDeserialize", "false");
 }});
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setSslConfig(new SSLConfig(true, null, null));
 connectionConfig.setEndpoint(new OBTenantEndpoint());
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
 dataSource = dataSourceFactory.getDataSource();
 }

 @Test
 public void testGetDataSource() {
 Assert.assertNotNull(dataSource);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 Assert.assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = dataSourceFactory.getUsername();
 Assert.assertEquals("testuser", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 Assert.assertEquals("testpassword", password);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();
 Assert.assertEquals(DialectType.MYSQL, dialectType);
 }

 @Test
 public void testGetDataSourceType() {
 ConnectType dataSourceType = dataSourceFactory.getConnectType();
 Assert.assertEquals(ConnectType.MYSQL, dataSourceType);
 }

 @Test
 public void testGetConnection() throws Exception {
 BasicDataSource ds = (BasicDataSource) dataSource;
 BasicDatasetDataSource dsTest = ds.createClone();
 Connection connection = dsTest.getConnection();
 String databaseProductName = DBUtils.getDatabaseProductName(connection);
 Assert.assertEquals("MySQL", databaseProductName);
 }

 @Test(expected = IllegalStateException.class)
 public void testFailedGetConnectionWithInitConnectionFalse() throws Exception {
 BasicDataSource ds = (BasicDataSource) dataSource;
 ds.setInitConnectionTestOnBorrow(false);
 ds.setInitialSize(0);
 ds.setMaxTotal(1);
 ds.setTestWhileIdle(false);
 ds.setTestOnReturn(false);
 ds.setTestOnBorrow(false);
 ds.setMinEvictableIdleTimeMillis(0L);
 PoolingDataSource dsTest = (PoolingDataSource) ds;
 Connection connection = dsTest.getConnection();
 }

 @Test
 public void testGetConnectionWithInitConnectionTrue() throws Exception {
 BasicDataSource ds = (BasicDataSource) dataSource;
 ds.setInitConnectionTestOnBorrow(true);
 ds.setInitialSize(1);
 ds.setMaxTotal(1);
 ds.setTestWhileIdle(true);
 ds.setTestOnReturn(true);
 ds.setTestOnBorrow(true);
 ds.setMinEvictableIdleTimeMillis(0L);
 PoolingDataSource dsTest = (PoolingDataSource) ds;
 Connection connection = dsTest.getConnection();
 connection.close();
 }

 @Test
 public void testGetConnectionWithInvalidConfig() {
 ConnectionConfig invalidConfig = new ConnectionConfig();
 invalidConfig.setUsername("testuser");
 invalidConfig.setPassword("testpassword");
 invalidConfig.setHost("localhost");
 invalidConfig.setPort(-1);
 invalidSourceFactory = new OBConsoleDataSourceFactory(invalidConfig, false, true);
 Assert.assertThrows(IllegalArgumentException.class, () -> invalidSourceFactory.getDataSource());
 }

}