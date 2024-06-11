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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import lombok.SneakyThrows;

/**
 * Test class for OBConsoleDataSourceFactory
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_HOST = "test_host";
    private static final int TEST_PORT = 1234;
    private static final String TEST_DEFAULT_SCHEMA = "test_default_schema";
    private static final String TEST_JDBC_URL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_DEFAULT_SCHEMA;
    private static final String TEST_DRIVER_CLASS = "test.driver.class";
    private static final String TEST_DATABASE_NAME = "test_database_name";
    private static final String TEST_TENANT_NAME = "test_tenant_name";
    private static final String TEST_CLUSTER_NAME = "test_cluster_name";
    private static final String TEST_ENDPOINT_HOST = "test_endpoint_host";
    private static final int TEST_ENDPOINT_PORT = 5678;
    private static final String TEST_KEY_STORE_FILE_PATH = "test_key_store_file_path";
    private static final String TEST_KEY_STORE_FILE_PASSWORD = "test_key_store_file_password";
    private static final String TEST_KEY_STORE_PASSWORD = "test_key_store_password";
    private static final String TEST_TRUST_STORE_FILE_PATH = "test_trust_store_file_path";
    private static final String TEST_TRUST_STORE_FILE_PASSWORD = "test_trust_store_file_password";
    private static final String TEST_TRUST_SERVER_CERTIFICATE = "test_trust_server_certificate";
    private static final Map<String, String> TEST_JDBC_URL_PARAMS = new HashMap<String, String>() {{
        put("maxAllowedPacket", "64000000");
        put("allowMultiQueries", "true");
        put("connectTimeout", "5000");
        put("zeroDateTimeBehavior", "CONVERT_TO_NULL");
        put("noDatetimeStringSync", "true");
        put("jdbcCompliantTruncation", "false");
        put("sendConnectionAttributes", "false");
        put("socksProxyHost", TEST_ENDPOINT_HOST);
        put("socksProxyPort", TEST_ENDPOINT_PORT + "");
    }};

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(TEST_JDBC_URL_PARAMS);
        connectionConfig.setEndpoint(new OBTenantEndpoint().setProxyHost(TEST_ENDPOINT_HOST).setProxyPort(TEST_ENDPOINT_PORT));

        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath(TEST_KEY_STORE_FILE_PATH);
        sslFileEntry.setKeyStoreFilePassword(TEST_KEY_STORE_FILE_PASSWORD);
        sslConfig.setEnabled(true);
        sslConfig.setKeyStoreFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);

        eventPublisher = Mockito.mock(EventPublisher.class);

        when(connectionExtensionPoint.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, TEST_JDBC_URL_PARAMS))
                .thenReturn(TEST_JDBC_URL);
        when(connectionExtensionPoint.getDriverClassName()).thenReturn(TEST_DRIVER_CLASS);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertEquals(TEST_JDBC_URL, dataSource.getConnection().getConnectionURL());
        assertEquals(TEST_USERNAME, dataSource.getConnection().getUserName());
        assertEquals(TEST_PASSWORD, dataSource.getConnection().getPassword());
        assertNotNull(dataSource.getConnection().getDriverManager().getDriver(TestDatabaseDriver.class.getName()));
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(TEST_JDBC_URL, clonedDataSourceFactory.getJdbcUrl());
        assertEquals(TEST_USERNAME, clonedDataSourceFactory.getUsername());
        assertEquals(TEST_PASSWORD, clonedDataSourceFactory.getPassword());
        assertEquals(TEST_HOST, clonedDataSourceFactory.getHost());
        assertEquals(TEST_PORT, clonedDataSourceFactory.getPort());
        assertEquals(TEST_DEFAULT_SCHEMA, clonedDataSourceFactory.getDefaultSchema());
        assertEquals(TEST_JDBC_URL_PARAMS, clonedDataSourceFactory.getParameters());
        assertEquals(connectionExtensionPoint, clonedDataSourceFactory.getConnectionExtensionPoint());
        assertEquals(TEST_AUTOCOMMIT, clonedDataSourceFactory.isAutoCommit());
        assertEquals(TEST_INIT_CONNECTION, clonedDataSourceFactory.isInitConnection());
        assertEquals(eventPublisher, clonedDataSourceFactory.getEventPublisher());
    }

    @Test
    public void testResetValues() throws SQLException {
        DataSource originalDataSource = dataSourceFactory.getDataSource();
        BasicDataSource basicDataSource = (BasicDataSource) originalDataSource;

        dataSourceFactory.resetUsername(valueMapper -> TEST_NEW_USERNAME);
        assertEquals(TEST_NEW_USERNAME, basicDataSource.getUsername());

        dataSourceFactory.resetPassword(valueMapper -> TEST_NEW_PASSWORD);
        assertEquals(TEST_NEW_PASSWORD, basicDataSource.getPassword());

        dataSourceFactory.resetHost(valueMapper -> TEST_NEW_HOST);
        assertEquals(TEST_NEW_HOST, basicDataSource.getUrl().split("/")[4]);

        dataSourceFactory.resetPort(valueMapper -> TEST_NEW_PORT);
        assertEquals(TEST_NEW_PORT, (int) (long) Integer.parseInt(basicDataSource.getUrl().split("/")[5].split(":")[1]));

        dataSourceFactory.resetSchema(valueMapper -> TEST_NEW_SCHEMA);
        assertEquals(TEST_NEW_SCHEMA, TEST_DEFAULT_SCHEMA);

        dataSourceFactory.resetParameters(valueMapper -> TEST_NEW_PARAMS);
        assertEquals(TEST_NEW_PARAMS, basicDataSource.getConnectionProperties());
    }

    private boolean TEST_AUTOCOMMIT = true;
    private boolean TEST_INIT_CONNECTION = true;

    @Test
    public void testDefaultValues() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(TEST_AUTOCOMMIT, dataSourceFactory.isAutoCommit());
        assertEquals(TEST_INIT_CONNECTION, dataSourceFactory.isInitConnection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConnectionConfig() {
        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(invalidConnectionConfig, true);
        assertNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testConnectionExtensionPoint() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        try {
            when(connectionExtensionPoint.getConnection(Mockito.anyString(), Mockito.anyInt(), anyString(), anyMap()))
                    .thenReturn(Mockito.mock(Connection.class));
            assertEquals(1, dataSourceFactory.getDataSource().getConnectionCount());
        } finally {
            connection.close();
        }
    }

    @Test
    public void testConnectionInitializers() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = Mockito.mock(SingleConnectionDataSource.class);
        when(singleConnectionDataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        when(singleConnectionDataSource.addInitializer(any())).thenReturn(singleConnectionDataSource);

        List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
        if (!CollectionUtils.isEmpty(initializers)) {
            OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
            dataSourceFactory.setDataSource(singleConnectionDataSource);
            dataSourceFactory.initConnection();
            verify(singleConnectionDataSource, times(initializers.size())).addInitializer(any());
        }
    }

    @Test
    public void testSessionCreatedInitializer() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = Mockito.mock(SingleConnectionDataSource.class);
        when(singleConnectionDataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        when(singleConnectionDataSource.addInitializer(any())).thenReturn(singleConnectionDataSource);

        SessionCreatedInitializer sessionCreatedInitializer = Mockito.mock(SessionCreatedInitializer.class);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setDataSource(singleConnectionDataSource);
        dataSourceFactory.initConnection();
        verify(singleConnectionDataSource).addInitializer(sessionCreatedInitializer);
    }

    @Test
    public void testResetValuesWithNullMapper() {
        DataSource originalDataSource = dataSourceFactory.getDataSource();
        BasicDataSource basicDataSource = (BasicDataSource) originalDataSource;

        dataSourceFactory.resetUsername(null);
        assertEquals(TEST_USERNAME, basicDataSource.getUsername());

        dataSourceFactory.resetPassword(null);
        assertEquals(TEST_PASSWORD, basicDataSource.getPassword());

        dataSourceFactory.resetHost(null);
        assertEquals(TEST_HOST, basicDataSource.getUrl().split("/")[4]);

        dataSourceFactory.resetPort(null);
        assertEquals(TEST_PORT, (int) (long) Integer.parseInt(basicDataSource.getUrl().split("/")[5].split(":")[1]));

        dataSourceFactory.resetSchema(null);
        assertEquals(TEST_DEFAULT_SCHEMA, TEST_DEFAULT_SCHEMA);

        dataSourceFactory.resetParameters(null);
        assertEquals(TEST_JDBC_URL_PARAMS, basicDataSource.getConnectionProperties());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        assertNull(dataSourceFactory.getDataSource());
    }

    @SneakyThrows
    @Test
    public void testGetJdbcUrlWithInvalidDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.INVALID_DIALECT_TYPE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals(TEST_JDBC_URL, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USERNAME, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetHost() {
        assertEquals(TEST_HOST, dataSourceFactory.getHost());
    }

    @Test
    public void testGetPort() {
        assertEquals(TEST_PORT, dataSourceFactory.getPort());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals(TEST_DEFAULT_SCHEMA, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        assertEquals(TEST_JDBC_URL_PARAMS, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetConnectionExtensionPoint() {
        assertEquals(connectionExtensionPoint, dataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testIsAutoCommit() {
        assertTrue(dataSourceFactory.isAutoCommit());
    }

    @Test
    public void testIsInitConnection() {
        assertTrue(dataSourceFactory.isInitConnection());
    }

    @Test
    public void testGetEventPublisher() {
        assertEquals(eventPublisher, dataSourceFactory.getEventPublisher());
    }

    @Test
    public void testGetUsernameFromConnectionConfig() {
        assertEquals(TEST_USERNAME, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPasswordFromConnectionConfig() {
        assertEquals(TEST_PASSWORD, OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParamsFromConnectionConfig() {
        assertEquals(TEST_JDBC_URL_PARAMS, OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }
}