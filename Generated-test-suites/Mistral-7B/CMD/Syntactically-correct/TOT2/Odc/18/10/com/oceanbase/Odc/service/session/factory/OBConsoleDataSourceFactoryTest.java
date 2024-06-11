import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
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
import com.oceanbase.odc.test.util.DialectTypeHolder;
import com.oceanbase.odc.test.util.JdbcUrlParamHolder;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private ConnectionMapper connectionMapper;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @InjectMocks
    private OBConsoleDataSourceFactory dataSourceFactory;
    @Setter
    private ConnectionSessionUtil connectionSessionUtil = new ConnectionSessionUtil();

    private DialectTypeHolder dialectTypeHolder = new DialectTypeHolder();
    private JdbcUrlParamHolder jdbcUrlParamHolder = new JdbcUrlParamHolder();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dialectTypeHolder.setDialectType(DialectType.OB_ORACLE);
        jdbcUrlParamHolder.setJdbcUrlParams(new HashMap<>());
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
        dataSourceFactory.setConnectionMapper(connectionMapper);
        dataSourceFactory.setConnectionPluginUtil(connectionPluginUtil);
        dataSourceFactory.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetJdbcUrl_OB_ORACLE() throws SQLException {
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq(dataSourceFactory.getHost()),
                Mockito.eq(dataSourceFactory.getPort()),
                Mockito.eq(dialectTypeHolder.getDialectType().name()),
                Mockito.eq(jdbcUrlParamHolder.getJdbcUrlParams())))
                .thenReturn("jdbc:ob:oracle:");

        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertTrue(jdbcUrl.startsWith("jdbc:ob:oracle:"));
    }

    @Test
    public void testGetDataSource() throws Exception {
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);

        Mockito.when(connectionExtensionPoint.getDriverClassName())
                .thenReturn("com.ob.jdbc.OracleDriver");
        Mockito.when(connectionSessionUtil.getUserOrSchemaString(Mockito.eq(connectionConfig.getUsername()),
                Mockito.eq(DialectType.OB_ORACLE)))
                .thenReturn("test_user");

        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ConnectionInitializer[] { connectionInitializer });

        Mockito.when(connectionInitializer.initializeDatabase(ArgumentMatchers.eq(dataSource)))
                .thenReturn(true);

        DataSource dataSourceFromFactory = dataSourceFactory.getDataSource();
        assertSame(dataSource, dataSourceFromFactory);
    }

    @Test
    public void testGetDataSource_whenInitConnectionIsFalse() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSourceFactory.setDataSource(dataSource);
        assertNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDataSource_whenAutoCommitIsFalse() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSourceFactory.setDataSource(dataSource);
        assertFalse(dataSourceFactory.getDataSource().isAutoCommit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_whenInitConnectionIsFalseAndDataSourceIsNull() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        dataSourceFactory.setDataSource(null);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = DialectType.OB_ORACLE;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setConnectionConfig(connectionConfig);
        assertSame(dialectType, dataSourceFactory.getDialectType());
    }

    @Test
    public void testClone() {
        ObConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.clone();
        assertNotNull(clonedDataSourceFactory);
        assertFalse(clonedDataSourceFactory == dataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "old_username";
        String newUsername = "new_username";
        dataSourceFactory.setUsername(oldUsername);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetUsername(mapper);
        String newUsernameAfterReset = mapper.map(newUsername);
        assertEquals(newUsernameAfterReset, newUsername);
        assertEquals(newUsernameAfterReset, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "old_password";
        String newPassword = "new_password";
        dataSourceFactory.setPassword(oldPassword);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPassword(mapper);
        String newPasswordAfterReset = mapper.map(newPassword);
        assertEquals(newPasswordAfterReset, newPassword);
        assertEquals(newPasswordAfterReset, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = "old_host";
        String newHost = "new_host";
        dataSourceFactory.setHost(oldHost);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetHost(mapper);
        String newHostAfterReset = mapper.map(newHost);
        assertEquals(newHostAfterReset, newHost);
        assertEquals(newHostAfterReset, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int oldPort = 1234;
        int newPort = 5678;
        dataSourceFactory.setPort(oldPort);
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPort(mapper);
        int newPortAfterReset = mapper.map(newPort);
        assertEquals(newPortAfterReset, newPort);
        assertEquals(newPortAfterReset, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "old_schema";
        String newSchema = "new_schema";
        dataSourceFactory.setDefaultSchema(oldSchema);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetSchema(mapper);
        String newSchemaAfterReset = mapper.map(newSchema);
        assertEquals(newSchemaAfterReset, newSchema);
        assertEquals(newSchemaAfterReset, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Properties oldParams = new Properties();
        oldParams.put("param1", "value1");
        Properties newParams = new Properties();
        newParams.put("param2", "value2");
        dataSourceFactory.setParameters(oldParams);
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(
                CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetParameters(mapper);
        Map<String, String> newParamsAfterReset = mapper.map(newParams);
        assertEquals(newParamsAfterReset, newParams);
        assertEquals(newParamsAfterReset, dataSourceFactory.getParameters());
    }

    private void assertEquals(String expected, String actual) {
        assertTrue(StringUtils.equals(expected, actual));
    }
}