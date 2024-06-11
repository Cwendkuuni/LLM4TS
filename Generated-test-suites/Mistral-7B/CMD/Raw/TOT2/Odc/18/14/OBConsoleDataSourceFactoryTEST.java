import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
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

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "testHost";
    private static final int TEST_PORT = 1234;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final Map<String, String> TEST_PARAMETERS = new HashMap<>();
    private static final ConnectionConfig TEST_CONNECTION_CONFIG = new ConnectionConfig();
    private static final ConnectionExtensionPoint TEST_CONNECTION_EXTENSION_POINT = mock(ConnectionExtensionPoint.class);
    private static final EventPublisher TEST_EVENT_PUBLISHER = mock(EventPublisher.class);
    private static final ConnectionMapper TEST_CONNECTION_MAPPER = ConnectionMapper.INSTANCE;

    @Setter
    private DataSourceFactory testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TEST_CONNECTION_CONFIG.setUsername(TEST_USERNAME);
        TEST_CONNECTION_CONFIG.setPassword(TEST_PASSWORD);
        TEST_CONNECTION_CONFIG.setHost(TEST_HOST);
        TEST_CONNECTION_CONFIG.setPort(TEST_PORT);
        TEST_CONNECTION_CONFIG.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        TEST_CONNECTION_CONFIG.setDialectType(DialectType.OB_ORACLE);
        TEST_CONNECTION_CONFIG.setType(ConnectType.MYSQL);
        TEST_CONNECTION_CONFIG.setJdbcUrlParameters(TEST_PARAMETERS);
        TEST_CONNECTION_CONFIG.setSslConfig(new SSLConfig(true, new SSLFileEntry("testPath", "testPassword")));
        TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl = mock(String[].class).thenReturn(new String[]{"jdbc:test"});
        testee = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
    }

    @After
    public void tearDown() throws SQLException {
        if (testee != null) {
            testee.close();
        }
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = testee.getDataSource();
        assertNotNull(dataSource);
        assertEquals(TEST_USERNAME, dataSource.getUsername());
        assertEquals(TEST_PASSWORD, dataSource.getPassword());
        assertEquals(TEST_HOST, dataSource.getConnectionProperties().getProperty("url"));
        assertEquals(TEST_PORT, Integer.parseInt(StringUtils.substringBefore(testee.getJdbcUrl(), ":").split(":")[2]));
        assertFalse(dataSource.getConnectionProperties().containsKey("allowLoadLocalInfile"));
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = testee.getDialectType();
        assertEquals(TEST_CONNECTION_CONFIG.getDialectType(), dialectType);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        testee.resetUsername(TEST_CONNECTION_MAPPER);
        assertEquals(newUsername, testee.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        testee.resetPassword(TEST_CONNECTION_MAPPER);
        assertEquals(newPassword, testee.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        testee.resetHost(TEST_CONNECTION_MAPPER);
        assertEquals(newHost, testee.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 5678;
        testee.resetPort(TEST_CONNECTION_MAPPER);
        assertEquals(newPort, testee.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        testee.resetSchema(TEST_CONNECTION_MAPPER);
        assertEquals(newSchema, testee.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        String newParameterKey = "newParameterKey";
        String newParameterValue = "newParameterValue";
        testee.resetParameters(TEST_CONNECTION_MAPPER);
        TEST_PARAMETERS.put(newParameterKey, newParameterValue);
        testee.resetParameters(TEST_CONNECTION_MAPPER);
        assertNull(testee.getParameters().get(newParameterKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAutoCommit() {
        new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidDialectType() {
        TEST_CONNECTION_CONFIG.setDialectType(null);
        try {
            new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid dialect type", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
        try {
            testee.getDataSource();
        } catch (IllegalArgumentException e) {
            assertEquals("Connection config can not be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithFailedConnection() {
        doThrow(SQLException.class).when(TEST_CONNECTION_EXTENSION_POINT).generateJdbcUrl(anyStringArray());
        testee = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
        testee.getDataSource();
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
        OBConsoleDataSourceFactory copy = original.deepCopy();
        assertEquals(original.getUsername(), copy.getUsername());
        assertEquals(original.getPassword(), copy.getPassword());
        assertEquals(original.getHost(), copy.getHost());
        assertEquals(original.getPort(), copy.getPort());
        assertEquals(original.getDefaultSchema(), copy.getDefaultSchema());
        assertEquals(original.getParameters(), copy.getParameters());
    }

    @Test
    public void testResetUsernameWithNullMapper() {
        testee.resetUsername(null);
        assertNull(testee.getUsername());
    }

    @Test
    public void testResetPasswordWithNullMapper() {
        testee.resetPassword(null);
        assertNull(testee.getPassword());
    }

    @Test
    public void testResetHostWithNullMapper() {
        testee.resetHost(null);
        assertNull(testee.getHost());
    }

    @Test
    public void testResetPortWithNullMapper() {
        testee.resetPort(null);
        assertNull(testee.getPort());
    }

    @Test
    public void testResetSchemaWithNullMapper() {
        testee.resetSchema(null);
        assertNull(testee.getDefaultSchema());
    }

    @Test
    public void testResetParametersWithNullMapper() {
        testee.resetParameters(null);
        assertNull(testee.getParameters());
    }

    @Test
    public void testCloneWithNullMapper() {
        DataSourceFactory original = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true);
        DataSourceFactory clone = original.clone();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
    }

    @Test
    public void testClose() throws SQLException {
        DataSource dataSource = testee.getDataSource();
        testee.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = testee.getJdbcUrl();
        assertTrue(jdbcUrl.startsWith("jdbc:" + TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl[0]));
        assertTrue(jdbcUrl.contains(TEST_USERNAME));
        assertTrue(jdbcUrl.contains(TEST_PASSWORD));
        assertTrue(jdbcUrl.contains(TEST_HOST));
        assertTrue(jdbcUrl.contains(TEST_PORT + ""));
        assertTrue(jdbcUrl.contains(TEST_DEFAULT_SCHEMA));
    }
}