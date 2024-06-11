import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.BasePooledObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.event.EventPublisher;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static com.oceanbase.odc.core.shared.constant.ConnectType.OB_MYSQL;
import static com.oceanbase.odc.core.shared.constant.DialectType.OB_ORACLE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory testObj;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionMapper connectionMapper;

    private final PooledObjectFactory<SingleConnectionDataSource> singleConnectionDataSourcePooledObjectFactory =
            new BasePooledObjectFactory<SingleConnectionDataSource>() {
                @Override
                public SingleConnectionDataSource create() {
                    return new SingleConnectionDataSource();
                }
            };

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(
                ConnectionInitializer.singletonList(connectionInitializer));
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(
                ConnectionInitializer.singletonList(sessionCreatedInitializer));
        when(connectionPluginUtil.getConnectionExtension(OB_ORACLE)).thenReturn(connectionExtensionPoint);
        when(connectionConfig.getType()).thenReturn(OB_MYSQL);
        when(connectionConfig.getDefaultSchema()).thenReturn("default_schema");
        when(connectionConfig.getDialectType()).thenReturn(OB_ORACLE);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getUsername()).thenReturn("testUser");
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        when(connectionConfig.getSslConfig()).thenReturn(null);
        when(connectionConfig.getEndpoint()).thenReturn(null);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(Map.of());
        when(connectionConfig.getUsername()).thenCallRealMethod();
        when(connectionMapper.clone(connectionConfig)).thenReturn(connectionConfig);
    }

    @After
    public void tearDown() {
        testObj = null;
        connectionExtensionPoint = null;
        connectionInitializer = null;
        sessionCreatedInitializer = null;
        connectionConfig = null;
        connectionPluginUtil = null;
        connectionMapper = null;
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select count(*) from information_schema.tables";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        Verify.notNull(dataSource);
        Verify.isTrue(dataSource instanceof SingleConnectionDataSource);
        Verify.isTrue(jdbcTemplate.getDataSource() == dataSource);
        Verify.isTrue(count > 0);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = testObj.getJdbcUrl();
        Verify.isTrue(jdbcUrl.contains("jdbc:odps:"));
        Verify.isTrue(jdbcUrl.contains("default_schema"));
        Verify.isTrue(jdbcUrl.contains("localhost"));
        Verify.isTrue(jdbcUrl.contains("3306"));
        Verify.isTrue(jdbcUrl.contains("useSSL=false"));
    }

    @Test
    public void testGetUsername() {
        String username = testObj.getUsername();
        Verify.assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        String password = testObj.getPassword();
        Verify.assertEquals("testPassword", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = testObj.getDialectType();
        Verify.assertEquals(OB_ORACLE, dialectType);
    }

    @Test
    public void testCloneableDataSourceFactory() {
        OBConsoleDataSourceFactory factory1 = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory factory2 = factory1.deepCopy();
        Verify.notSame(factory1, factory2);
        Verify.isTrue(factory1.getUsername().equals(factory2.getUsername()));
        Verify.isTrue(factory1.getPassword().equals(factory2.getPassword()));
        Verify.isTrue(factory1.getHost().equals(factory2.getHost()));
        Verify.isTrue(factory1.getPort().equals(factory2.getPort()));
        Verify.isTrue(factory1.getDefaultSchema().equals(factory2.getDefaultSchema()));
        Verify.isTrue(factory1.getParameters().equals(factory2.getParameters()));
        Verify.isTrue(factory1.getConnectionConfig().equals(factory2.getConnectionConfig()));
        Verify.isTrue(factory1.getConnectionExtensionPoint().equals(factory2.getConnectionExtensionPoint()));
        Verify.isTrue(factory1.getAutoCommit() == factory2.getAutoCommit());
        Verify.isTrue(factory1.getInitConnection() == factory2.getInitConnection());
        Verify.isTrue(factory1.getEventPublisher() == factory2.getEventPublisher());
    }

    @Test
    public void testResetUsername() {
        String oldUsername = testObj.getUsername();
        String newUsername = "newUsername";
        testObj.resetUsername(s -> s.replace("testUser", "newUsername"));
        Verify.assertEquals(newUsername, testObj.getUsername());
        Verify.assertEquals(oldUsername, ConnectionSessionUtil.getUserOrSchemaString(connectionConfig.getUsername(), OB_ORACLE));
    }

    @Test
    public void testResetPassword() {
        String oldPassword = testObj.getPassword();
        String newPassword = "newPassword";
        testObj.resetPassword(s -> s.replace("testPassword", "newPassword"));
        Verify.assertEquals(newPassword, testObj.getPassword());
        Verify.assertEquals(oldPassword, connectionConfig.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = testObj.getHost();
        String newHost = "newHost";
        testObj.resetHost(s -> s.replace("localhost", "newHost"));
        Verify.assertEquals(newHost, testObj.getHost());
        Verify.assertEquals(oldHost, "localhost");
    }

    @Test
    public void testResetPort() {
        Integer oldPort = testObj.getPort();
        Integer newPort = 1234;
        testObj.resetPort(i -> i + 1);
        Verify.assertEquals(newPort, testObj.getPort());
        Verify.isTrue(oldPort > 0);
    }

    @Test
    public void testResetSchema() {
        String oldSchema = testObj.getDefaultSchema();
        String newSchema = "newSchema";
        testObj.resetSchema(s -> s.replace("default_schema", "newSchema"));
        Verify.assertEquals(newSchema, testObj.getDefaultSchema());
        Verify.assertEquals(oldSchema, "default_schema");
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParams = testObj.getParameters();
        Map<String, String> newParams = Map.of("newKey", "newValue");
        testObj.resetParameters(m -> m.putAll(newParams));
        Verify.assertTrue(newParams.equals(testObj.getParameters()));
        Verify.assertFalse(oldParams.equals(testObj.getParameters()));
    }
}