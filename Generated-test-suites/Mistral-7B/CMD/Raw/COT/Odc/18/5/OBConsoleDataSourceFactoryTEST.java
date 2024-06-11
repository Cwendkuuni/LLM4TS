import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.common.event.EventType.SESSION_CREATED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for OBConsoleDataSourceFactory
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private ConnectionMapper connectionMapper;

    @InjectMocks
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Arrays.asList(connectionInitializer));
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertNotNull(jdbcUrl);
        DataSource dataSource = dataSourceFactory.getDataSource();
        Assert.assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String username = dataSourceFactory.getUsername();
        Assert.assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String password = dataSourceFactory.getPassword();
        Assert.assertNotNull(password);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        DialectType dialectType = dataSourceFactory.getDialectType();
        Assert.assertNotNull(dialectType);
    }

    @Test
    public void testCloneableDataSourceFactoryDeepCopy() throws CloneNotSupportedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) originalDataSourceFactory.deepCopy();
        Assert.assertNotNull(clonedDataSourceFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetUsernameWithNullMapper() {
        dataSourceFactory.resetUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordWithNullMapper() {
        dataSourceFactory.resetPassword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHostWithNullMapper() {
        dataSourceFactory.resetHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortWithNullMapper() {
        dataSourceFactory.resetPort(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetSchemaWithNullMapper() {
        dataSourceFactory.resetSchema(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetParametersWithNullMapper() {
        dataSourceFactory.resetParameters(null);
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "oldUsername";
        String newUsername = "newUsername";
        dataSourceFactory.resetUsername(connectionMapper);
        String username = dataSourceFactory.getUsername();
        Assert.assertEquals(newUsername, username);
        Assert.assertEquals(oldUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(connectionMapper);
        String password = dataSourceFactory.getPassword();
        Assert.assertEquals(newPassword, password);
        Assert.assertEquals(oldPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = "oldHost";
        String newHost = "newHost";
        dataSourceFactory.resetHost(connectionMapper);
        String host = dataSourceFactory.getHost();
        Assert.assertEquals(newHost, host);
        Assert.assertEquals(oldHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer oldPort = 123;
        Integer newPort = 456;
        dataSourceFactory.resetPort(connectionMapper);
        Integer port = dataSourceFactory.getPort();
        Assert.assertEquals(newPort, port);
        Assert.assertEquals(oldPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "oldSchema";
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(connectionMapper);
        String schema = dataSourceFactory.getDefaultSchema();
        Assert.assertEquals(newSchema, schema);
        Assert.assertEquals(oldSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParameters = new HashMap<>();
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters(connectionMapper);
        Map<String, String> parameters = dataSourceFactory.getParameters();
        Assert.assertEquals(newParameters, parameters);
        Assert.assertEquals(oldParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetConnectionInitializers() {
        List<ConnectionInitializer> initializers = dataSourceFactory.getConnectionInitializers();
        Assert.assertEquals(1, initializers.size());
        verify(connectionExtensionPoint).getConnectionInitializers();
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> params = dataSourceFactory.getJdbcParams(connectionConfig);
        Assert.assertNotNull(params);
    }

    @Test
    public void testGenerateJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String jdbcUrl = dataSourceFactory.connectionExtensionPoint.generateJdbcUrl("localhost", 1234, "schema", params);
        Assert.assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsernameFromConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String username = dataSourceFactory.getUsername(connectionConfig);
        Assert.assertNotNull(username);
    }

    @Test
    public void testGetPasswordFromConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String password = dataSourceFactory.getPassword(connectionConfig);
        Assert.assertNotNull(password);
    }

    @Test
    public void testGetDefaultSchemaFromConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String defaultSchema = dataSourceFactory.getDefaultSchema(connectionConfig);
        Assert.assertNotNull(defaultSchema);
    }

    @Test
    public void testGetJdbcUrlWithSsl() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertTrue(jdbcUrl.contains("useSSL=true"));
        Assert.assertTrue(jdbcUrl.contains("trustStore=" + sslFileEntry.getKeyStoreFilePath()));
        Assert.assertTrue(jdbcUrl.contains("trustStorePassword=" + sslFileEntry.getKeyStoreFilePassword()));
        Assert.assertTrue(jdbcUrl.contains("keyStore=" + sslFileEntry.getKeyStoreFilePath()));
        Assert.assertTrue(jdbcUrl.contains("keyStorePassword=" + sslFileEntry.getKeyStoreFilePassword()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJdbcUrlWithNullConnectionConfig() {
        dataSourceFactory.getJdbcUrl(null);
    }

    @Test
    public void testSessionCreatedInitializerIsAdded() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SessionCreatedInitializer initializer = mock(SessionCreatedInitializer.class);
        dataSourceFactory.addInitializer(initializer);
        List<ConnectionInitializer> initializers = dataSourceFactory.getConnectionInitializers();
        Assert.assertEquals(1, initializers.size());
        verify(initializer).afterPropertiesSet();
        verify(connectionPublisher).publishEvent(argThat(event -> event.getType() == SESSION_CREATED), any());
    }
}