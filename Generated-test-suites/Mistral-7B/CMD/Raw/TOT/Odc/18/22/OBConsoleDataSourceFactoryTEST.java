package com.oceanbase.odc.service.session.factory;

 import static org.junit.Assert.*;
 import static org.mockito.Mockito.*;

 import java.sql.Connection;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Properties;

 import org.apache.commons.dbcp2.BasicDataSource;
 import org.apache.commons.dbcp2.PoolingDataSource;
 import org.junit.Before;
 import org.junit.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;

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

 public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;
 @Mock
 private ConnectionInitializer connectionInitializer;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private PoolingDataSource dataSource;
 @Mock
 private Connection connection;
 @InjectMocks
 private OBConsoleDataSourceFactory testSubject;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetDataSource() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setParameters(parameters);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.Driver");
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { connectionInitializer });

 // When
 DataSource dataSource = testSubject.getDataSource();

 // Then
 assertEquals(PoolingDataSource.class, dataSource.getClass());
 verify(connectionExtensionPoint, times(1)).generateJdbcUrl(host, port, defaultSchema, parameters);
 verify(connectionExtensionPoint, times(1)).getDriverClassName();
 assertNotNull(dataSource.getConnection());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceNullConnectionConfig() {
 // Given
 ConnectionConfig connectionConfig = null;

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> testSubject.getDataSource(connectionConfig));
 }

 @Test
 public void testGetDataSourceWithConnectionInitializer() throws SQLException {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.Driver");
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { connectionInitializer });
 when(connectionInitializer.initializeDatabase(eq(connection)))
 .thenReturn(true);

 // When
 DataSource dataSource = testSubject.getDataSource();
 Connection connectionFromDataSource = dataSource.getConnection();

 // Then
 assertEquals(connectionFromDataSource, connection);
 verify(connectionInitializer, times(1)).initializeDatabase(eq(connectionFromDataSource));
 }

 @Test
 public void testGetDataSourceWithoutConnectionInitializer() throws SQLException {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.Driver");
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[0]);

 // When
 DataSource dataSource = testSubject.getDataSource();
 Connection connectionFromDataSource = dataSource.getConnection();

 // Then
 assertNotNull(connectionFromDataSource);
 verify(connectionExtensionPoint, never()).getConnectionInitializers();
 }

 @Test
 public void testGetDataSourceWithInvalidJdbcUrl() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenThrow(RuntimeException.class);

 // When & Then
 assertThrows(RuntimeException.class, () -> testSubject.getDataSource());
 }

 @Test
 public void testGetDataSourceWithNullDriverClassName() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn(null);

 // When & Then
 assertThrows(RuntimeException.class, () -> testSubject.getDataSource());
 }

 @Test
 public void testGetDataSourceWithInvalidParameters() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.Driver");
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[0]);
 parameters.put("testKey", "testValue");

 // When & Then
 assertThrows(RuntimeException.class, () -> testSubject.getDataSource(parameters));
 }

 @Test
 public void testGetDataSourceWithEmptyParameters() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 String username = "testuser";
 String password = "testpassword";
 String host = "localhost";
 Integer port = 1234;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setType(ConnectType.OB_MYSQL);
 connectionConfig.setSslConfig(new SSLConfig());
 when(connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, parameters)).thenReturn("jdbc:mock:myds:test");
 when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.Driver");
 when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[0]);
 parameters.clear();

 // When & Then
 testSubject.getDataSource(parameters);
 }

 @Test
 public void testGetDataSourceWithEmptyConnectionConfig() {
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 ConnectionMapper mapper = ConnectionMapper.INSTANCE;
 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenThrow(new RuntimeException("Test exception"));

 // When & Then
 assertThrows(RuntimeException.class, () -> testSubject.getDataSource(connectionConfig));
 }
}