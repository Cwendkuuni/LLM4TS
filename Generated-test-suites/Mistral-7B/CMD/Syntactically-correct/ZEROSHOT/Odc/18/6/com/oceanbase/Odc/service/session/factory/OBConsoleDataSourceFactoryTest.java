import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;

    private static final String DATABASE_URL = "jdbc:oracle:thin:@//localhost:1521:test_db";
    private static final String USER = "test_user";
    private static final String PASSWORD = "test_password";
    private static final int PORT = 1521;
    private static final String DEFAULT_SCHEMA = "test_schema";
    private static final String JDBC_PARAMS = "maxAllowedPacket=64000000,allowMultiQueries=true,connectTimeout=5000";
    private static final String OB_TENANT_NAME = "test_tenant";
    private static final String TENANT_NAME = "test_tenant_name";
    private static final String CLUSTER_NAME = "test_cluster_name";
    private static final String PROXY_HOST = "test_proxy_host";
    private static final int PROXY_PORT = 8080;
    private static final String SSL_KEY_STORE_FILE_PATH = "/path/to/keystore.p12";
    private static final String SSL_KEY_STORE_FILE_PASSWORD = "test_password123";
    private static final boolean SSL_ENABLED = true;

    @Before
    public void setUp() {
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn(DATABASE_URL);
        when(connectionExtensionPoint.getDriverClassName()).thenReturn(DriverManager.class.getName());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
    }

    @After
    public void tearDown() throws SQLException {
        DriverManager.getConnection(DATABASE_URL).close();
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstruction() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(SSL_ENABLED, SSL_KEY_STORE_FILE_PATH, SSL_KEY_STORE_FILE_PASSWORD));

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false);

        assertEquals(USER, testObj.getUsername());
        assertEquals(PASSWORD, testObj.getPassword());
        assertEquals(PORT, testObj.getPort());
        assertEquals(DEFAULT_SCHEMA, testObj.getDefaultSchema());
        assertEquals(JDBC_PARAMS, testObj.getJdbcUrlParameters().toString());
        assertNull(testObj.getSslConfig());
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructionWithSsl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(SSL_ENABLED, SSL_KEY_STORE_FILE_PATH, SSL_KEY_STORE_FILE_PASSWORD));

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        assertEquals(USER, testObj.getUsername());
        assertEquals(PASSWORD, testObj.getPassword());
        assertEquals(PORT, testObj.getPort());
        assertEquals(DEFAULT_SCHEMA, testObj.getDefaultSchema());
        assertEquals(JDBC_PARAMS, testObj.getJdbcUrlParameters().toString());
        assertEquals(SSL_ENABLED, testObj.getSslConfig().isEnabled());
        assertEquals(SSL_KEY_STORE_FILE_PATH, testObj.getSslConfig().getKeyStoreFilePath());
        assertEquals(SSL_KEY_STORE_FILE_PASSWORD, testObj.getSslConfig().getKeyStoreFilePassword());
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false);

        Properties connectionProperties = testObj.getDataSource().getConnectionProperties();
        assertEquals("allowLoadLocalInfile=false,allowUrlInLocalInfile=false,allowLoadLocalInfileInPath=,autoDeserialize=false",
                connectionProperties.getProperty("connectionProperties"));

        Connection connection = testObj.getDataSource().getConnection();
        assertEquals(DATABASE_URL, connection.getConnectionURL());
        assertEquals(USER, connection.getUserName());
        assertEquals(PASSWORD, connection.getPassword());
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException, InterruptedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(SSL_ENABLED, SSL_KEY_STORE_FILE_PATH, SSL_KEY_STORE_FILE_PASSWORD));

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        Properties connectionProperties = testObj.getDataSource().getConnectionProperties();
        assertEquals("allowLoadLocalInfile=false,allowUrlInLocalInfile=false,allowLoadLocalInfileInPath=,autoDeserialize=false",
                connectionProperties.getProperty("connectionProperties"));

        Connection connection = testObj.getDataSource().getConnection();
        assertEquals(DATABASE_URL, connection.getConnectionURL());
        assertEquals(USER, connection.getUserName());
        assertEquals(PASSWORD, connection.getPassword());

        Thread.sleep(1000); // Give time for SSL handshake to complete

        connection.close();
    }

    @Test
    public void testGetDataSourceWithoutDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);

        AssertionError exception = AssertionError.class;
        try {
            OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false);
            testObj.getDataSource();
        } catch (Exception e) {
            exception = (AssertionError) e;
        }

        assertEquals("DialectType must be set", exception.getMessage());
    }

    @Test
    public void testGetDataSourceWithoutInitConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        Properties connectionProperties = testObj.getDataSource().getConnectionProperties();
        assertEquals("allowLoadLocalInfile=false,allowUrlInLocalInfile=false,allowLoadLocalInfileInPath=,autoDeserialize=false",
                connectionProperties.getProperty("connectionProperties"));
    }

    @Test
    public void testGetDataSourceWithTenantName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setTenantName(TENANT_NAME);

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        assertEquals(USER + "@" + TENANT_NAME, testObj.getUsername());
    }

    @Test
    public void testGetDataSourceWithClusterName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setClusterName(CLUSTER_NAME);

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        assertEquals(USER + "#" + CLUSTER_NAME, testObj.getUsername());
    }

    @Test
    public void testGetDataSourceWithProxyHostAndPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(PORT);
        connectionConfig.setUsername(USER);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setProxyHost(PROXY_HOST);
        connectionConfig.setProxyPort(PROXY_PORT);

        OBConsoleDataSourceFactory testObj = new OBConsoleDataSourceFactory(connectionConfig, false, false);

        Properties connectionProperties = testObj.getDataSource().getConnectionProperties();
        assertEquals("socksProxyHost=" + PROXY_HOST + ",socksProxyPort=" + PROXY_PORT + ",allowLoadLocalInfile=false,allowUrlInLocalInfile=false,allowLoadLocalInfileInPath=,autoDeserialize=false",
                connectionProperties.getProperty("connectionProperties"));
    }

}