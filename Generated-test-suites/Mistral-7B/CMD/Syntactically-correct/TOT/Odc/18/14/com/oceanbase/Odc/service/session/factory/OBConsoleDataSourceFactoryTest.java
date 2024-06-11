package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.quality.Strictness;

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

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit(Strictness.STRICT_STUBS);

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    private OBConsoleDataSourceFactory factory;
    private BasicDataSource dataSource;

    @Before
    public void setup() {
        when(connectionConfig.getUsername()).thenReturn("testUser");
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
        when(connectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.isAutoCommit()).thenReturn(false);
        when(connectionConfig.getEndpoint()).thenReturn(null);
        when(connectionConfig.getSslConfig()).thenReturn(null);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());

        ConnectionPluginUtil.setConnectionExtensionPoint(connectionExtensionPoint);

        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testSchema");
        dataSource.setConnectionTestQuery("SELECT 1");
        factory.setDataSource(dataSource);
        factory.setEventPublisher(eventPublisher);
        factory.setConnectionExtensionPoint(connectionExtensionPoint);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection connectionFromFactory = factory.getDataSource().getConnection();
        assertEquals(connection.getClass(), connectionFromFactory.getClass());
        assertTrue(connectionFromFactory.isClosed());
        connectionFromFactory.createStatement().execute("DROP TABLE IF EXISTS testTable");
        connectionFromFactory.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        connectionFromFactory.createStatement().execute("INSERT INTO testTable (id, name) VALUES (1, 'test')");
        String testQuery = "SELECT * FROM testTable WHERE id = 1";
        assertNotNull(connectionFromFactory.createStatement().executeQuery(testQuery));
        connectionFromFactory.close();
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy_ConnectionConfig() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        ConnectionConfig clonedConnectionConfig = clonedFactory.getConnectionConfig();
        assertEquals(connectionConfig, clonedConnectionConfig);
        assertNotSame(connectionConfig, clonedConnectionConfig);
        assertFalse(Objects.equals(connectionConfig.getUsername(), clonedConnectionConfig.getUsername()));
        assertTrue(Objects.equals(connectionConfig.getUsername(), clonedConnectionConfig.getUsername()));
    }

    @Test
    public void testDeepCopy_Username() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetUsername(mapper);
        assertEquals("testUser_test", factory.getUsername());

        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals("testUser_test", clonedFactory.getUsername());
    }

    @Test
    public void testDeepCopy_Password() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetPassword(mapper);
        assertEquals("testPassword_test", factory.getPassword());

        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals("testPassword_test", clonedFactory.getPassword());
    }

    @Test
    public void testDeepCopy_Host() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetHost(mapper);
        assertEquals("localhost_test", factory.getHost());

        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals("localhost_test", clonedFactory.getHost());
    }

    @Test
    public void testDeepCopy_Port() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> value * 2;
        factory.resetPort(mapper);
        assertEquals(3306 * 2, factory.getPort());

        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals(3306 * 2, clonedFactory.getPort());
    }

    @Test
    public void testDeepCopy_Schema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetSchema(mapper);
        assertEquals("testSchema_test", factory.getDefaultSchema());

        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals("testSchema_test", clonedFactory.getDefaultSchema());
    }

    @Test
    public void testDeepCopy_Parameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);

        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> {
            Map<String, String> newValue = new HashMap<>();
            newValue.putAll(value);
            newValue.put("key2", "value2");
            return newValue;
        };

        factory.resetParameters(mapper);
        Map<String, String> newParameters = factory.getParameters();
        assertEquals(parameters, newParameters);
        assertFalse(Objects.equals(parameters, newParameters));
        assertFalse(newParameters.containsKey("key1"));
        assertTrue(newParameters.containsKey("key2"));
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetUsername(mapper);
        assertEquals("testUser_test", factory.getUsername());

        String newUsername = "newUsername";
        factory.resetUsername(value -> newUsername);
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetPassword(mapper);
        assertEquals("testPassword_test", factory.getPassword());

        String newPassword = "newPassword";
        factory.resetPassword(value -> newPassword);
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetHost(mapper);
        assertEquals("localhost_test", factory.getHost());

        String newHost = "newHost";
        factory.resetHost(value -> newHost);
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> value * 2;
        factory.resetPort(mapper);
        assertEquals(3306 * 2, factory.getPort());

        int newPort = 4408;
        factory.resetPort(value -> newPort);
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "test_" + value;
        factory.resetSchema(mapper);
        assertEquals("testSchema_test", factory.getDefaultSchema());

        String newSchema = "newSchema";
        factory.resetSchema(value -> newSchema);
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=false&trustServerCertificate=true&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=" + StringUtils.EMPTY
                + "&autoDeserialize=false";

        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testConnectionInitializer() throws SQLException {
        factory.setInitConnection(false);
        DataSource dataSource = factory.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        connection.createStatement().execute("INSERT INTO testTable (id, name) VALUES (1, 'test')");

        factory.setInitConnection(true);
        dataSource = factory.getDataSource();
        connection = dataSource.getConnection();
        connection.createStatement().execute("DROP TABLE IF EXISTS testTable");
        connection.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        connection.createStatement().execute("INSERT INTO testTable (id, name) VALUES (1, 'test')");
        connection.close();

        verify(connectionInitializer, times(1)).initialize(connection);
    }

    @Test
    public void testSessionCreatedInitializer() throws SQLException {
        factory.setInitConnection(true);
        factory.setEventPublisher(mock(EventPublisher.class));

        DataSource dataSource = factory.getDataSource();
        Connection connection = dataSource.getConnection();

        factory.setConnectionExtensionPoint(mock(ConnectionExtensionPoint.class));
        ConnectionExtensionPoint connectionExtensionPoint = factory.getConnectionExtensionPoint();
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());

        SessionCreatedInitializer sessionCreatedInitializer = mock(SessionCreatedInitializer.class);
        when(connectionExtensionPoint.getSessionCreatedInitializer()).thenReturn(sessionCreatedInitializer);

        connection.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        connection.createStatement().execute("INSERT INTO testTable (id, name) VALUES (1, 'test')");
        connection.close();

        verify(sessionCreatedInitializer, times(1)).initialize(connection, eventPublisher);
    }

    @Test
    public void testGetDataSourceWithSSLConfig() throws SQLException {
        SSLConfig sslConfig = new SSLConfig(true, "C:/path/to/truststore.p12", "testPassword");
        ConnectionConfig connectionConfigWithSSL = new ConnectionConfig("test", "test", "localhost", 3306, null, null, null, null, null, null, null, null, null, sslConfig, null, null, null);
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.generateJdbcUrl(eq("localhost"), anyInt(), eq(null), eq(Map.of()), anyString())).thenReturn("jdbc:mysql://localhost:3306/?useSSL=true&trustServerCertificate=true");
        OBConsoleDataSourceFactory factoryWithSSL = new OBConsoleDataSourceFactory(connectionConfigWithSSL, false, true);
        DataSource dataSourceWithSSL = factoryWithSSL.getDataSource();
        Connection connectionWithSSL = dataSourceWithSSL.getConnection();
        connectionWithSSL.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        connectionWithSSL.createStatement().execute("INSERT INTO testTable (id, name) VALUES (1, 'test')");
        connectionWithSSL.close();
        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), anyInt(), eq(null), eq(Map.of()), anyString());
    }
}