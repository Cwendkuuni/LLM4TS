import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.ValueMapper;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private CloneableDataSourceFactory cloneableDataSourceFactory;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, int port, String schema, Map<String, String> parameters) {
 throw new UnsupportedOperationException("Not supported.");
 }

 @Override
 public String getDriverClassName() {
 return "com.mysql.jdbc.Driver";
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return null;
 }

 @Override
 public void validateConnection(String jdbcUrl, String username, String password) {
 // Do some validation here
 }
 };
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 cloneableDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 Assert.assertNotNull(dataSource);
 SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
 Assert.assertNotNull(singleConnectionDataSource.getConnection());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) cloneableDataSourceFactory.deepCopy();
 ValueMapper mapper = new ValueMapper() {
 @Override
 public <T> T map(T original) {
 if (original instanceof String) {
 return (T) "cloned" + ((String) original);
 } else {
 return (T) original;
 }
 }
 };
 clonedDataSourceFactory.resetUsername(mapper);
 clonedDataSourceFactory.resetPassword(mapper);
 clonedDataSourceFactory.resetHost(mapper);
 clonedDataSourceFactory.resetPort(mapper);
 clonedDataSourceFactory.resetSchema(mapper);
 clonedDataSourceFactory.resetParameters(mapper);
 DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
 Assert.assertEquals(dataSourceFactory.getDataSource(), clonedDataSource);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPort() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(-1);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(null);
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword(null);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHost() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost(null);
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setDialectType(DialectType.MYSQL);
 connectionConfig.setAutoCommit(true);
 connectionConfig.setInitConnection(true);
 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

}