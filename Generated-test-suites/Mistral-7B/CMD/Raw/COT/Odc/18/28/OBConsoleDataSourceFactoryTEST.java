import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private DataSourceFactory dataSourceFactorySpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.DEFAULT);

        dataSourceFactorySpy = mock(DataSourceFactory.class);
        DataSource dataSource = new SingleConnectionDataSource();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.setDataSourceFactory(dataSourceFactorySpy);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertNotNull(password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() throws SQLException, CloneNotSupportedException {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();

        // Check if the cloned DataSource is not the same as the original one
        assertNotNull(clonedDataSource);
        assertNotSame(clonedDataSource, dataSourceFactory.getDataSource());

        // Test if the cloned DataSource's properties are correctly mapped
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        clonedDataSourceFactory.resetUsername(valueMapper);
        clonedDataSourceFactory.resetPassword(valueMapper);
        clonedDataSourceFactory.resetHost(valueMapper);
        clonedDataSourceFactory.resetPort(valueMapper);
        clonedDataSourceFactory.resetSchema(valueMapper);
        clonedDataSourceFactory.resetParameters(valueMapper);

        String originalUsername = dataSourceFactory.getUsername();
        String clonedUsername = clonedDataSourceFactory.getUsername();
        assertEquals(originalUsername, clonedUsername);

        String originalPassword = dataSourceFactory.getPassword();
        String clonedPassword = clonedDataSourceFactory.getPassword();
        assertEquals(originalPassword, clonedPassword);

        String originalHost = dataSourceFactory.getHost();
        String clonedHost = clonedDataSourceFactory.getHost();
        assertEquals(originalHost, clonedHost);

        int originalPort = dataSourceFactory.getPort();
        int clonedPort = clonedDataSourceFactory.getPort();
        assertEquals(originalPort, clonedPort);

        String originalSchema = dataSourceFactory.getDefaultSchema();
        String clonedSchema = clonedDataSourceFactory.getDefaultSchema();
        assertEquals(originalSchema, clonedSchema);

        Properties originalParameters = dataSourceFactory.getParameters();
        Properties clonedParameters = clonedDataSourceFactory.getParameters();
        assertEquals(originalParameters, clonedParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidConnectionConfig() {
        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactoryWithInvalidConfig = new OBConsoleDataSourceFactory(invalidConnectionConfig, false, true);
        dataSourceFactoryWithInvalidConfig.getDataSource();
    }

    @Test
    public void testGetJdbcUrlWithNullParameters() {
        connectionConfig.setParameters(null);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullParameters = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        String jdbcUrl = dataSourceFactoryWithNullParameters.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullJdbcUrl() {
        DataSourceFactory dataSourceFactorySpyWithNullJdbcUrl = mock(DataSourceFactory.class);
        dataSourceFactory.setDataSourceFactory(dataSourceFactorySpyWithNullJdbcUrl);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullJdbcUrl = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactoryWithNullJdbcUrl.getDataSource();
    }

    @Test
    public void testGetDataSourceWithEmptyUsername() {
        connectionConfig.setUsername(StringUtils.EMPTY);
        OBConsoleDataSourceFactory dataSourceFactoryWithEmptyUsername = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithEmptyUsername.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptyPassword() {
        connectionConfig.setPassword(StringUtils.EMPTY);
        OBConsoleDataSourceFactory dataSourceFactoryWithEmptyPassword = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithEmptyPassword.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithNullUsername() {
        connectionConfig.setUsername(null);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullUsername = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithNullUsername.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithNullPassword() {
        connectionConfig.setPassword(null);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullPassword = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithNullPassword.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptyDefaultSchema() {
        connectionConfig.setDefaultSchema(StringUtils.EMPTY);
        OBConsoleDataSourceFactory dataSourceFactoryWithEmptyDefaultSchema = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithEmptyDefaultSchema.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithNullDefaultSchema() {
        connectionConfig.setDefaultSchema(null);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullDefaultSchema = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithNullDefaultSchema.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptyJdbcParams() {
        connectionConfig.setParameters(new HashMap<>());
        OBConsoleDataSourceFactory dataSourceFactoryWithEmptyJdbcParams = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithEmptyJdbcParams.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithInvalidJdbcParams() {
        Map<String, String> invalidJdbcParams = new HashMap<>();
        invalidJdbcParams.put("invalid_param", "invalid_value");
        connectionConfig.setParameters(invalidJdbcParams);
        OBConsoleDataSourceFactory dataSourceFactoryWithInvalidJdbcParams = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithInvalidJdbcParams.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithInvalidSslConfig() {
        SSLConfig invalidSslConfig = new SSLConfig();
        invalidSslConfig.setSslFileEntry(null);
        connectionConfig.setSslConfig(invalidSslConfig);
        OBConsoleDataSourceFactory dataSourceFactoryWithInvalidSslConfig = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithInvalidSslConfig.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithNullSslConfig() {
        connectionConfig.setSslConfig(null);
        OBConsoleDataSourceFactory dataSourceFactoryWithNullSslConfig = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithNullSslConfig.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithValidSslConfig() {
        SSLConfig validSslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("path/to/keystore.jks");
        sslFileEntry.setKeyStoreFilePassword("password");
        validSslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(validSslConfig);

        OBConsoleDataSourceFactory dataSourceFactoryWithValidSslConfig = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DataSource dataSource = dataSourceFactoryWithValidSslConfig.getDataSource();
        assertNotNull(dataSource);
    }
}