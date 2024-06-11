import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
import com.oceanbase.odc.utils.test.TestConstants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private ConnectionPluginUtil connectionPluginUtil;

    @Mock
    private ConnectionMapper connectionMapper;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = mock(SingleConnectionDataSource.class);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Arrays.asList(connectionInitializer));
        when(connectionExtensionPoint.getConnection(dataSourceFactory)).thenReturn(dataSource);
        when(connectionInitializer.initializeDatabase(dataSource)).thenReturn(true);
        when(sessionCreatedInitializer.initializeDatabase(dataSource)).thenReturn(true);

        DataSource testDataSource = dataSourceFactory.getDataSource();
        Assert.assertNotNull(testDataSource);
        Assert.assertTrue(testDataSource instanceof SingleConnectionDataSource);
        Assert.assertEquals(dataSource, testDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        Map<String, String> parameters = new HashMap<>();
        when(connectionConfig.getHost()).thenReturn("host");
        when(connectionConfig.getPort()).thenReturn(1234);
        when(connectionConfig.getDefaultSchema()).thenReturn("schema");
        when(connectionConfig.getParameters()).thenReturn(parameters);
        when(connectionExtensionPoint.generateJdbcUrl("host", 1234, "schema", parameters)).thenReturn("jdbc:url");

        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertEquals("jdbc:url", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionConfig.getUsername()).thenReturn("username");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(connectionConfig.getObTenantName()).thenReturn("tenantName");
        when(connectionConfig.getClusterName()).thenReturn("clusterName");

        String username = dataSourceFactory.getUsername(connectionConfig);
        Assert.assertEquals("username@tenantName#clusterName", username);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUsernameNullConnectionConfig() {
        dataSourceFactory.getUsername(null);
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("password");
        String password = dataSourceFactory.getPassword();
        Assert.assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcUrlParams = new HashMap<>();
        jdbcUrlParams.put("maxAllowedPacket", "64000000");
        jdbcUrlParams.put("allowMultiQueries", "true");
        jdbcUrlParams.put("connectTimeout", "5000");
        jdbcUrlParams.put("zeroDateTimeBehavior", "CONVERT_TO_NULL");
        jdbcUrlParams.put("noDatetimeStringSync", "true");
        jdbcUrlParams.put("jdbcCompliantTruncation", "false");
        jdbcUrlParams.put("sendConnectionAttributes", "false");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(Collections.emptyMap());
        when(connectionExtensionPoint.getConnection(dataSourceFactory)).thenReturn(dataSource);

        Map<String, String> params = dataSourceFactory.getJdbcParams(connectionConfig);
        Assert.assertEquals(jdbcUrlParams, params);
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "oldUsername";
        String newUsername = "newUsername";
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetUsername(mapper);

        Assert.assertEquals(newUsername, dataSourceFactory.getUsername());
        Assert.assertEquals(oldUsername, mapper.map(oldUsername));
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetPassword(mapper);

        Assert.assertEquals(newPassword, dataSourceFactory.getPassword());
        Assert.assertEquals(oldPassword, mapper.map(oldPassword));
    }

    @Test
    public void testResetHost() {
        String oldHost = "oldHost";
        String newHost = "newHost";
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetHost(mapper);

        Assert.assertEquals(newHost, dataSourceFactory.getHost());
        Assert.assertEquals(oldHost, mapper.map(oldHost));
    }

    @Test
    public void testResetPort() {
        int oldPort = 1234;
        int newPort = 5678;
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetPort(mapper);

        Assert.assertEquals(newPort, dataSourceFactory.getPort());
        Assert.assertEquals(oldPort, mapper.map(oldPort));
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "oldSchema";
        String newSchema = "newSchema";
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetSchema(mapper);

        Assert.assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
        Assert.assertEquals(oldSchema, mapper.map(oldSchema));
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParams = new HashMap<>();
        Map<String, String> newParams = new HashMap<>();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetParameters(mapper);

        Assert.assertEquals(newParams, dataSourceFactory.getParameters());
        Assert.assertEquals(oldParams, mapper.map(dataSourceFactory.getParameters()));
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = DialectType.OB_ORACLE;
        when(connectionConfig.getDialectType()).thenReturn(dialectType);

        DialectType result = dataSourceFactory.getDialectType();
        Assert.assertEquals(dialectType, result);
    }

    @Test
    public void testDeepCopy() {
        Map<String, String> parameters = new HashMap<>();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("host");
        connectionConfig.setPort(1234);
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setParameters(parameters);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_SQL);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));

        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = originalDataSourceFactory.deepCopy();

        Assert.assertNotSame(originalDataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        Assert.assertNotSame(originalDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        Assert.assertNotSame(originalDataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        Assert.assertNotSame(originalDataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        Assert.assertNotSame(originalDataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        Assert.assertNotSame(originalDataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        Assert.assertNotSame(originalDataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        Assert.assertEquals(originalDataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
    }

    @Test
    public void testGetUsernameNullConnectionConfigDialectType() {
        when(connectionConfig.getUsername()).thenReturn(null);
        when(connectionConfig.getDialectType()).thenReturn(null);

        String result = dataSourceFactory.getUsername();
        Assert.assertNull(result);
    }

    @Test
    public void testGetUsernameNullConnectionConfigUsername() {
        when(connectionConfig.getUsername()).thenReturn(null);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);

        String result = dataSourceFactory.getUsername();
        Assert.assertNull(result);
    }

    @Test
    public void testGetPasswordNullConnectionConfig() {
        when(connectionConfig.getPassword()).thenReturn(null);

        String result = dataSourceFactory.getPassword();
        Assert.assertNull(result);
    }
}