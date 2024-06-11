import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 private DataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 connectionConfig = new ConnectionConfig()
 .setHost("localhost")
 .setPort(3306)
 .setUsername("root")
 .setPassword("root")
 .setDialectType(DialectType.MYSQL)
 .setType(ConnectType.OB_MYSQL)
 .setDefaultSchema("test_schema")
 .setSslConfig(new SSLConfig().setEnabled(false))
 .setJdbcUrlParameters(Map.of("useSSL", "false"));
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @After
 public void tearDown() throws Exception {
 DBUtils.destroyQuietly(Objects.requireNonNull(dataSourceFactory.getDataSource()).getConnection());
 }

 /**
 * Test for getDataSource() method
 */
 @Test
 public void testGetDataSource() {
 // Check if the getDataSource() method returns a valid DataSource object
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertNotNull(dataSource);
 assertTrue(dataSource instanceof SingleConnectionDataSource);

 // Test if the connection can be obtained from the DataSource
 ConnectionSessionUtil connectionSessionUtil = new ConnectionSessionUtil();
 ConnectionSessionUtil.setConnectionMapper(ConnectionMapper.INSTANCE);
 ConnectionSessionUtil.setConnectionExtensionPoint(connectionExtensionPoint);
 ConnectionSessionUtil.setEventPublisher(eventPublisher);
 ConnectionSessionUtil.setConnectionInitializers(List.of(new ConnectionInitializer() {
 @Override
 public void initializeDataSource(DataSource dataSource) {
 // performing some initialization logic
 }
 }));
 ConnectionSessionUtil.setTenantName("test_tenant");
 ConnectionSessionUtil.setClusterName("test_cluster");
 ConnectionSessionUtil.setUsername("test_user");
 ConnectionSessionUtil.setPassword("test_password");
 ConnectionSessionUtil.setDatasource(dataSource);
 ConnectionSessionUtil.setDialectType(DialectType.MYSQL);
 ConnectionSessionUtil.setDefaultSchema("test_schema");
 assertNotNull(connectionSessionUtil.getConnection());
 DBUtils.closeQuietly(connectionSessionUtil.getConnection());
 }

 /**
 * Test for the getJdbcUrl() method with valid parameters
 */
 @Test
 public void testGetJdbcUrlWithValidParameters() {
 String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?useSSL=false";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 /**
 * Test for the getJdbcUrl() method with invalid parameters
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetJdbcUrlWithInvalidParameters() {
 ConnectionConfig invalidConnectionConfig = new ConnectionConfig()
 .setHost("localhost")
 .setPort(-1)
 .setUsername("root")
 .setPassword("root")
 .setDialectType(DialectType.MYSQL)
 .setType(ConnectType.OB_MYSQL)
 .setDefaultSchema("test_schema")
 .setSslConfig(new SSLConfig().setEnabled(false))
 .setJdbcUrlParameters(Map.of("useSSL", "false"));
 OBConsoleDataSourceFactory invalidDataSourceFactory = new OBConsoleDataSourceFactory(invalidConnectionConfig, true, true);
 String invalidJdbcUrl = invalidDataSourceFactory.getJdbcUrl();
 fail("Expected an IllegalArgumentException but got " + invalidJdbcUrl);
 }
}