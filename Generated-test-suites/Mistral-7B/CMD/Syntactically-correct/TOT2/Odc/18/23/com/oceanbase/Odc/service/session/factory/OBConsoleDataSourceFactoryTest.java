import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceType;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 private DataSourceFactory factory;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(1521);
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setType(ConnectType.OB_ORACLE);
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setSslConfig(new SSLConfig());
 connectionConfig.getSslConfig().setEnabled(true);
 connectionConfig.setSslFileEntry(new SSLFileEntry());
 connectionConfig.getSslFileEntry().setKeyStoreFilePath("test.p12");
 connectionConfig.getSslFileEntry().setKeyStoreFilePassword("testPassword");

 ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 factory = new OBConsoleDataSourceFactory(connectionConfig, true, true, connectionExtensionPoint);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = factory.getDataSource();
 assertNotNull(dataSource);
 assertNotNull(dataSource.getConnection());
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = factory.getJdbcUrl();
 assertNotNull(jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = factory.getUsername();
 assertNotNull(username);
 }

 @Test
 public void testGetPassword() {
 String password = factory.getPassword();
 assertNotNull(password);
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = factory.getDialectType();
 assertEquals(DialectType.OB_ORACLE, dialectType);
 }

 @Test
 public void testGetDataSourceWithSsl() {
 ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 ConnectionInitializer initializer = mock(ConnectionInitializer.class);
 SessionCreatedInitializer sessionInitializer = new SessionCreatedInitializer(connectionConfig);
 connectionExtensionPoint.getConnectionInitializers().add(initializer);
 factory = new OBConsoleDataSourceFactory(connectionConfig, true, true, connectionExtensionPoint);
 DataSource dataSource = factory.getDataSource();
 assertNotNull(dataSource);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithInvalidPort() {
 connectionConfig.setPort(-1);
 factory = new OBConsoleDataSourceFactory(connectionConfig, true, true, connectionExtensionPoint);
 // The constructor of SingleConnectionDataSource throws an IllegalArgumentException when the port is negative
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullConfig() {
 factory = new OBConsoleDataSourceFactory(null, true, true, connectionExtensionPoint);
 }
}