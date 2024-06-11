import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
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
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertInstanceOf(SingleConnectionDataSource.class, dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testCloneableDataSourceFactoryDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotSame(dataSourceFactory, clonedDataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "test_username";
        dataSourceFactory.resetUsername(stringMapper -> newUsername);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "test_password";
        dataSourceFactory.resetPassword(stringMapper -> newPassword);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "test_host";
        dataSourceFactory.resetHost(stringMapper -> newHost);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer newPort = 1234;
        dataSourceFactory.resetPort(integerMapper -> newPort);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "test_schema";
        dataSourceFactory.resetSchema(stringMapper -> newSchema);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("test_param_1", "test_value_1");
        dataSourceFactory.resetParameters(mapMapper -> newParameters);
        assertEquals(newParameters, dataSourceFactory.getParameters());
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
    public void testGetConnectType() {
        ConnectType connectType = dataSourceFactory.getConnectType();
        assertEquals(ConnectType.CONSOLE, connectType);
    }

    @Test
    public void testGetDataSourceWithConnectionInitializer() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(connectionInitializer));
        when(connectionInitializer.initDatabase(any())).thenReturn(true);

        DataSource dataSource = dataSourceFactory.getDataSource();
        assertTrue(dataSource.isInitialized());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidConnectionConfig() {
        connectionConfig.setHost(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDataSourceWithEmptyUsernameAndPassword() {
        connectionConfig.setUsername("");
        connectionConfig.setPassword("");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptyJdbcUrlParameters() {
        Map<String, String> jdbcUrlParams = new HashMap<>();
        connectionConfig.setJdbcUrlParameters(jdbcUrlParams);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithInvalidJdbcUrl() {
        String jdbcUrl = "invalid_jdbc_url";
        connectionConfig.setJdbcUrl(jdbcUrl);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
            fail("Expected an exception");
        } catch (SQLException e) {
            assertEquals("Invalid JDBC connection URL: " + jdbcUrl, e.getMessage());
        }
    }

    @Test
    public void testGetDataSourceWithEmptyUsernameAndNonEmptyPassword() {
        connectionConfig.setUsername("");
        String password = "test_password";
        connectionConfig.setPassword(password);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptyPasswordAndNonEmptyUsername() {
        String username = "test_username";
        connectionConfig.setUsername(username);
        connectionConfig.setPassword("");

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithInvalidSslFileEntry() {
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath(null);
        connectionConfig.setSslFileEntry(sslFileEntry);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
            fail("Expected an exception");
        } catch (SQLException e) {
            assertEquals("Invalid SSL configuration", e.getMessage());
        }
    }

    @Test
    public void testGetDataSourceWithValidSslFileEntry() {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceWithEmptySslFileEntry() {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @After
    public void tearDown() {
        dataSourceFactory = null;
    }
}