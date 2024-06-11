import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBConsoleDataSourceFactory.class})
public class OBConsoleDataSourceFactoryTest {

 private static final Log LOG = LogFactory.getLog(OBConsoleDataSourceFactoryTest.class);
 private static final String TEST_USERNAME = "testUsername";
 private static final String TEST_PASSWORD = "testPassword";
 private static final String TEST_HOST = "testHost";
 private static final int TEST_PORT = 1234;
 private static final String TEST_DEFAULT_SCHEMA = "testSchema";
 private static final Map<String, String> TEST_PARAMETERS = new HashMap<>();
 private static final ConnectionConfig TEST_CONNECTION_CONFIG;
 private static final ConnectionExtensionPoint TEST_CONNECTION_EXTENSION_POINT = Mockito.mock(ConnectionExtensionPoint.class);
 private static final EventPublisher TEST_EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
 private static final SessionCreatedInitializer TEST_SESSION_CREATED_INITIALIZER = Mockito.mock(SessionCreatedInitializer.class);
 private static final ConnectionInitializer TEST_CONNECTION_INITIALIZER = Mockito.mock(ConnectionInitializer.class);
 private static final String TEST_JDBC_URL = "jdbc:test:url";
 private DataSource TEST_DATA_SOURCE;
 private OBConsoleDataSourceFactory TEST_OB_CONSOLE_DATA_SOURCE_FACTORY;

 static {
 TEST_CONNECTION_CONFIG = new ConnectionConfig();
 TEST_CONNECTION_CONFIG.setUsername(TEST_USERNAME);
 TEST_CONNECTION_CONFIG.setPassword(TEST_PASSWORD);
 TEST_CONNECTION_CONFIG.setHost(TEST_HOST);
 TEST_CONNECTION_CONFIG.setPort(TEST_PORT);
 TEST_CONNECTION_CONFIG.setDefaultSchema(TEST_DEFAULT_SCHEMA);
 TEST_CONNECTION_CONFIG.setConnectionExtensionPoint(TEST_CONNECTION_EXTENSION_POINT);
 TEST_CONNECTION_CONFIG.setEventPublisher(TEST_EVENT_PUBLISHER);
 TEST_CONNECTION_CONFIG.setType(ConnectType.TEST);
 TEST_CONNECTION_CONFIG.setDialectType(DialectType.TEST);
 TEST_CONNECTION_CONFIG.setSslConfig(new SSLConfig(true));
 TEST_CONNECTION_CONFIG.setSslFileEntry(new SSLFileEntry("testKeyStoreFilePath", "testKeyStoreFilePassword"));
 TEST_CONNECTION_CONFIG.setJdbcUrlParameters(TEST_PARAMETERS);
 }

 @Before
 public void setUp() {
 PowerMockito.mockStatic(ConnectionSessionUtil.class);
 PowerMockito.mockStatic(ConnectionMapper.class);
 PowerMockito.mockStatic(ConnectionPluginUtil.class);
 PowerMockito.when(ConnectionSessionUtil.getUserOrSchemaString(anyString(), any(DialectType.class)))
 .thenReturn("testUsername");
 PowerMockito.when(ConnectionMapper.clone(any(ConnectionConfig.class)))
 .thenReturn(TEST_CONNECTION_CONFIG);
 PowerMockito.when(ConnectionPluginUtil.getConnectionExtension(any(DialectType.class)))
 .thenReturn(TEST_CONNECTION_EXTENSION_POINT);
 PowerMockito.when(TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn(TEST_JDBC_URL);
 PowerMockito.when(TEST_CONNECTION_EXTENSION_POINT.getDriverClassName())
 .thenReturn("testDriverClassName");
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, false);
 }

 @After
 public void tearDown() {
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY = null;
 TEST_DATA_SOURCE = null;
 }

 @Test
 public void testGetDataSource() {
 TEST_DATA_SOURCE = TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getDataSource();

 Validate.isInstanceOf(SingleConnectionDataSource.class, TEST_DATA_SOURCE);
 VerifyThat(TEST_DATA_SOURCE).hasUrl(TEST_JDBC_URL);
 VerifyThat(TEST_DATA_SOURCE).hasUsername(TEST_USERNAME);
 VerifyThat(TEST_DATA_SOURCE).hasPassword(TEST_PASSWORD);
 VerifyThat(TEST_DATA_SOURCE).setDriverClassName("testDriverClassName");
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getDialectType();

 Assert.assertEquals(TEST_CONNECTION_CONFIG.getDialectType(), dialectType);
 }

 @Test
 public void testCloneableDataSourceFactory() {
 DataSourceFactory clone = TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.deepCopy();

 Assert.assertNotSame(TEST_OB_CONSOLE_DATA_SOURCE_FACTORY, clone);
 Assert.assertEquals(TEST_CONNECTION_CONFIG, clone.getConnectionConfig());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetUsername(argThat(isA(StringMapper.class)));

 Assert.assertEquals(newUsername, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetPassword(argThat(isA(StringMapper.class)));

 Assert.assertEquals(newPassword, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetHost(argThat(isA(StringMapper.class)));

 Assert.assertEquals(newHost, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 5678;
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetPort(argThat(isA(IntegerMapper.class)));

 Assert.assertEquals(newPort, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetSchema(argThat(isA(StringMapper.class)));

 Assert.assertEquals(newSchema, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.resetParameters(argThat(isA(MapMapper.class)));

 Assert.assertEquals(newParameters, TEST_OB_CONSOLE_DATA_SOURCE_FACTORY.getParameters());
 }

 @Test
 public void testInitConnection() {
 PowerMockito.doNothing().when(TEST_CONNECTION_EXTENSION_POINT).getConnectionInitializers();
 TEST_OB_CONSOLE_DATA_SOURCE_FACTORY = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
 PowerMockito.verifyStatic(ConnectionInitializer.class);
 ConnectionInitializer.initDatabase(any(SingleConnectionDataSource.class), any(ConnectionConfig.class));
 PowerMockito.verifyStatic(SessionCreatedInitializer.class);
 SessionCreatedInitializer.init(any(ConnectionConfig.class));
 }
}