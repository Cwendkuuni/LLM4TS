import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.ValueMapper;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;
 private ValueMapper<String> stringMapper;
 private ValueMapper<Integer> intMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("user");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setType(ConnectType.MYSQL);
 stringMapper = (from, to) -> to; // Placeholder implementation for ValueMapper<String>
 intMapper = (from, to) -> to; // Placeholder implementation for ValueMapper<Integer>
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
 }

 @After
 public void tearDown() {
 if (dataSourceFactory != null) {
 dataSourceFactory.close();
 }
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 assertEquals("user", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals("localhost", dataSource.getConnectionProperties().getProperty("url"));
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertEquals("jdbc:mysql://localhost:3306/schema", jdbcUrl);
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = dataSourceFactory.getParameters();
 assertEquals(10, jdbcParams.size());
 assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
 assertEquals("true", jdbcParams.get("allowMultiQueries"));
 assertEquals("5000", jdbcParams.get("connectTimeout"));
 assertEquals(DEFAULT_ZERO_DATE_TIME_BEHAVIOR, jdbcParams.get("zeroDateTimeBehavior"));
 assertEquals("true", jdbcParams.get("noDatetimeStringSync"));
 assertEquals("false", jdbcParams.get("jdbcCompliantTruncation"));
 assertEquals("false", jdbcParams.get("sendConnectionAttributes"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullConnectionConfig() {
 OBConsoleDataSourceFactory testFactory = new OBConsoleDataSourceFactory(null, false, false);
 assertNull(testFactory);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConnectionConfig() {
 ConnectionConfig invalidConfig = new ConnectionConfig();
 OBConsoleDataSourceFactory testFactory = new OBConsoleDataSourceFactory(invalidConfig, false, false);
 assertNull(testFactory);
 invalidConfig.setHost(null);
 testFactory = new OBConsoleDataSourceFactory(invalidConfig, false, false);
 assertNull(testFactory);
 invalidConfig.setPort(-1);
 testFactory = new OBConsoleDataSourceFactory(invalidConfig, false, false);
 assertNull(testFactory);
 }

 @Test
 public void testResetValues() {
 dataSourceFactory.resetUsername(stringMapper);
 dataSourceFactory.resetPassword(stringMapper);
 dataSourceFactory.resetHost(stringMapper);
 dataSourceFactory.resetPort(intMapper);
 dataSourceFactory.resetSchema(stringMapper);
 dataSourceFactory.resetParameters(stringMapper);
 assertEquals("", dataSourceFactory.getUsername());
 assertEquals("", dataSourceFactory.getPassword());
 assertEquals("", dataSourceFactory.getHost());
 assertEquals(0, dataSourceFactory.getPort());
 assertEquals(null, dataSourceFactory.getDefaultSchema());
 assertEquals(null, dataSourceFactory.getParameters());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory original = dataSourceFactory; // shallow copy
 OBConsoleDataSourceFactory deepCopy = original.deepCopy(); // deep copy
 assertEquals(original.getHost(), deepCopy.getHost());
 assertEquals(original.getPort(), deepCopy.getPort());
 assertNotEquals(original.getUsername(), deepCopy.getUsername());
 }
}