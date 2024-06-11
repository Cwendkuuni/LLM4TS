import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "testHost";
    private static final int TEST_PORT = 3306;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final Map<String, String> TEST_PARAMETERS = new HashMap<>();
    static {
        TEST_PARAMETERS.put("allowMultiQueries", "true");
        TEST_PARAMETERS.put("connectTimeout", "5000");
        TEST_PARAMETERS.put("zeroDateTimeBehavior", "DEFAULT_CURRENT_TIMESTAMP");
    }

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionMapper connectionMapper;
    private DataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionMapper = ConnectionMapper.INSTANCE;
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setParameters(TEST_PARAMETERS);
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setInitConnection(true);

        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        this.dataSourceFactory = dataSourceFactory;
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(connectionExtensionPoint);
    }

    @Test
    public void getDataSource_TestValidConnectionConfig_ShouldReturnSingleConnectionDataSource() {
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
        assertTrue(dataSource != null);
        assertEquals(TEST_HOST, dataSource.getUrl().substring(0, dataSource.getUrl().indexOf(":")));
        assertEquals(TEST_PORT, dataSource.getPort());
        assertEquals(TEST_USERNAME, dataSource.getUsername());
        assertEquals(TEST_PASSWORD, dataSource.getPassword());
        assertEquals(connectionConfig.getDialectType(), dataSource.getDialectType());
        assertEquals(TEST_DEFAULT_SCHEMA, dataSource.getDefaultSchema());
        assertEquals(TEST_PARAMETERS, dataSource.getConnectionProperties());
    }

    @Test
    public void getDataSource_TestEmptyConnectionConfig_ShouldReturnNull() {
        ConnectionConfig emptyConnectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory emptyDataSourceFactory = new OBConsoleDataSourceFactory(emptyConnectionConfig, true);
        assertNull(emptyDataSourceFactory.getDataSource());
    }

    @Test
    public void getDataSource_TestInvalidConnectionConfig_ShouldThrowException() {
        connectionConfig.setUsername(null);
        OBConsoleDataSourceFactory invalidDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("ConnectionConfig is missing mandatory properties: username", e.getMessage());
        }
    }

    @Test
    public void getDataSource_TestConnectionFailure_ShouldThrowException() {
        connectionExtensionPoint = Mockito.spy(ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL));
        Mockito.doThrow(new RuntimeException("Test error")).when(connectionExtensionPoint).generateJdbcUrl(connectionConfig.getHost(), connectionConfig.getPort(), TEST_DEFAULT_SCHEMA, TEST_PARAMETERS);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
            assertTrue(false);
        } catch (RuntimeException e) {
            assertEquals("Test error", e.getMessage());
        }
        Mockito.verify(connectionExtensionPoint).generateJdbcUrl(connectionConfig.getHost(), connectionConfig.getPort(), TEST_DEFAULT_SCHEMA, TEST_PARAMETERS);
    }

    @Test
    public void getDataSource_TestValidConnectionConfigWithSsl_ShouldReturnSingleConnectionDataSourceWithSsl() {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
        assertTrue(dataSource != null);
        assertEquals(TEST_HOST, dataSource.getUrl().substring(0, dataSource.getUrl().indexOf(":")));
        assertEquals(TEST_PORT, dataSource.getPort());
        assertEquals(TEST_USERNAME, dataSource.getUsername());
        assertEquals(TEST_PASSWORD, dataSource.getPassword());
        assertEquals(connectionConfig.getDialectType(), dataSource.getDialectType());
        assertEquals(TEST_DEFAULT_SCHEMA, dataSource.getDefaultSchema());
        assertEquals(TEST_PARAMETERS, dataSource.getConnectionProperties());
        assertTrue(dataSource.getUseSSL());
        assertTrue(dataSource.getTrustServerCertificate());
        assertTrue(dataSource.getTrustStore() != null);
        assertTrue(dataSource.getKeyStore() != null);
        assertTrue(dataSource.getKeyStorePassword() != null);
    }

    @Test
    public void getDataSource_TestValidConnectionConfigWithInitializer_ShouldAddInitializers() {
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        List<ConnectionInitializer> initializers = ConnectionPluginUtil.getConnectionInitializers();
        initializers.add(initializerMock);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(initializers);

        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
        assertTrue(dataSource != null);
        assertTrue(dataSource.getAddInitializers().size() > 0);
        Mockito.verify(initializerMock).initializeDataSource(dataSource);
    }

    @Test
    public void deepCopy_TestValidDataSourceFactory_ShouldReturnDeepCopiedDataSourceFactory() {
        DataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(connectionConfig, clonedDataSourceFactory.getConnectionConfig());
        assertTrue(clonedDataSourceFactory instanceof OBConsoleDataSourceFactory);
        SingleConnectionDataSource clonedDataSource = (SingleConnectionDataSource) clonedDataSourceFactory.getDataSource();
        assertEquals(dataSourceFactory.getDataSource().getConnection(), clonedDataSource.getConnection());
    }

    @Test
    public void resetUsername_TestValidDataSourceFactory_ShouldResetUsername() {
        String newUsername = "newUsername";
        DataSourceFactory.ValueMapper<String> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(TEST_USERNAME)).thenReturn(newUsername);

        OracleDataSourceFactory dataSourceFactory = (OracleDataSourceFactory) dataSourceFactory;
        dataSourceFactory.setUsernameMapper(mapper);
        dataSourceFactory.resetUsername();
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }
}