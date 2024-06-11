package com.oceanbase.odc.service.session.factory;

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
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "testHost";
    private static final int TEST_PORT = 3306;
    private static final String TEST_SCHEMA = "testSchema";
    private static final String TEST_JDBC_URL = "jdbc:test:test";
    private static final String TEST_DRIVER_CLASS = "test.driver.class";
    private static final String TEST_DEFAULT_SCHEMA = "testDefaultSchema";
    private static final Map<String, String> TEST_PARAMETERS = new HashMap<>();
    static {
        TEST_PARAMETERS.put("test.key", "test.value");
    }

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource testDataSource;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        eventPublisher = Mockito.mock(EventPublisher.class);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.emptyList());
        Mockito.when(connectionExtensionPoint.getDriverClassName())
                .thenReturn(TEST_DRIVER_CLASS);
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(eq(TEST_HOST), eq(TEST_PORT), eq(TEST_SCHEMA),
                eq(TEST_PARAMETERS)))
                .thenReturn(TEST_JDBC_URL);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));
    }

    @After
    public void tearDown() throws Exception {
        if (testDataSource != null) {
            testDataSource.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn(TEST_DRIVER_CLASS);
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(eq(TEST_HOST), eq(TEST_PORT), eq(TEST_SCHEMA),
                eq(TEST_PARAMETERS))).thenReturn(TEST_JDBC_URL);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));

        DataSource expectedDataSource = new DriverManagerDataSource(TEST_DRIVER_CLASS, TEST_USERNAME, TEST_PASSWORD,
                TEST_JDBC_URL);
        DataSource actualDataSource = dataSourceFactory.getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testGetDataSourceWithEventPublisher() throws SQLException {
        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));
        Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn(TEST_DRIVER_CLASS);
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(eq(TEST_HOST), eq(TEST_PORT), eq(TEST_SCHEMA),
                eq(TEST_PARAMETERS))).thenReturn(TEST_JDBC_URL);

        when(eventPublisher.publishEvent(Mockito.any()))
                .thenReturn(Void.TYPE);

        DataSource expectedDataSource = new DriverManagerDataSource(TEST_DRIVER_CLASS, TEST_USERNAME, TEST_PASSWORD,
                TEST_JDBC_URL);
        DataSource actualDataSource = dataSourceFactory.newInstance(eventPublisher).getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testGetDataSourceWithAutoCommit() throws SQLException {
        Mockito.when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));
        Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn(TEST_DRIVER_CLASS);
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(eq(TEST_HOST), eq(TEST_PORT), eq(TEST_SCHEMA),
                eq(TEST_PARAMETERS))).thenReturn(TEST_JDBC_URL);

        DataSource expectedDataSource = new BasicDataSource(1, null) {{
            setDriverClassName(TEST_DRIVER_CLASS);
            setUsername(TEST_USERNAME);
            setPassword(TEST_PASSWORD);
            setUrl(TEST_JDBC_URL);
            setAutoCommit(true);
        }};

        DataSource actualDataSource = dataSourceFactory.newInstance(null, true).getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullEventPublisher() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        dataSourceFactory.newInstance(eventPublisher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.newInstance(eventPublisher, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullEventPublisherAndConnectionExtensionPoint() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        dataSourceFactory.newInstance(null, null);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        Mockito.when(connectionConfig.getDialectType()).thenReturn(expectedDialectType);

        DialectType actualDialectType = dataSourceFactory.getDialectType();

        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testGetCloneableDataSourceFactory() {
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory cloneDataSourceFactory = (OBConsoleDataSourceFactory) originalDataSourceFactory
                .deepCopy();

        assertEquals(originalDataSourceFactory.getUsername(), cloneDataSourceFactory.getUsername());
        assertEquals(originalDataSourceFactory.getPassword(), cloneDataSourceFactory.getPassword());
        assertEquals(originalDataSourceFactory.getHost(), cloneDataSourceFactory.getHost());
        assertEquals(originalDataSourceFactory.getPort(), cloneDataSourceFactory.getPort());
        assertEquals(originalDataSourceFactory.getDefaultSchema(), cloneDataSourceFactory.getDefaultSchema());
        assertEquals(originalDataSourceFactory.getParameters(), cloneDataSourceFactory.getParameters());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        dataSourceFactory.resetUsername(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory.resetHost(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory.resetPort(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "expectedUsername";
        connectionConfig.setUsername(expectedUsername);

        String actualUsername = dataSourceFactory.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "expectedPassword";
        connectionConfig.setPassword(expectedPassword);

        String actualPassword = dataSourceFactory.getPassword();

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetHost() {
        String expectedHost = "expectedHost";
        connectionConfig.setHost(expectedHost);

        String actualHost = dataSourceFactory.getHost();

        assertEquals(expectedHost, actualHost);
    }

    @Test
    public void testGetPort() {
        int expectedPort = 3308;
        connectionConfig.setPort(expectedPort);

        int actualPort = dataSourceFactory.getPort();

        assertEquals(expectedPort, actualPort);
    }

    @Test
    public void testGetDefaultSchema() {
        String expectedDefaultSchema = "expectedDefaultSchema";
        connectionConfig.setDefaultSchema(expectedDefaultSchema);

        String actualDefaultSchema = dataSourceFactory.getDefaultSchema();

        assertEquals(expectedDefaultSchema, actualDefaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        connectionConfig.setParameters(expectedParameters);

        Map<String, String> actualParameters = dataSourceFactory.getParameters();

        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testGetConnectType() {
        ConnectType expectedConnectType = ConnectType.JDBC;
        connectionConfig.setType(expectedConnectType);

        ConnectType actualConnectType = dataSourceFactory.getConnectType();

        assertEquals(expectedConnectType, actualConnectType);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:test:test";
        String host = "testHost";
        int port = 3306;
        String defaultSchema = "testSchema";
        Map<String, String> parameters = TEST_PARAMETERS;
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        connectionConfig.setHost(host);
        connectionConfig.setPort(port);
        connectionConfig.setDefaultSchema(defaultSchema);
        connectionConfig.setParameters(parameters);
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();

        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsernameWithOracleDialectType() {
        String expectedUsername = "expectedUsername";
        connectionConfig.setUsername(expectedUsername);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        String actualUsername = dataSourceFactory.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetUsernameWithoutOracleDialectType() {
        String expectedUsername = "expectedUsername";
        connectionConfig.setUsername(expectedUsername);
        connectionConfig.setDialectType(DialectType.MYSQL);

        String actualUsername = dataSourceFactory.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetDefaultSchemaWithOracleDialectType() {
        String expectedDefaultSchema = "expectedDefaultSchema";
        connectionConfig.setDefaultSchema(expectedDefaultSchema);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        String actualDefaultSchema = dataSourceFactory.getDefaultSchema();

        assertEquals(expectedDefaultSchema, actualDefaultSchema);
    }

    @Test
    public void testGetDefaultSchemaWithoutOracleDialectType() {
        String expectedDefaultSchema = "expectedDefaultSchema";
        connectionConfig.setDefaultSchema(expectedDefaultSchema);
        connectionConfig.setDialectType(DialectType.MYSQL);

        String actualDefaultSchema = dataSourceFactory.getDefaultSchema();

        assertEquals(expectedDefaultSchema, actualDefaultSchema);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> testJdbcParams = new HashMap<>();
        testJdbcParams.put("testKey", "testValue");
        connectionConfig.setJdbcUrlParameters(testJdbcParams);

        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("testKey", "testValue");

        Map<String, String> actualJdbcParams = dataSourceFactory.getJdbcParams();

        assertEquals(expectedJdbcParams, actualJdbcParams);
    }

    @Test
    public void testGetJdbcParamsWithEmptyJdbcUrlParameters() {
        connectionConfig.setJdbcUrlParameters(Collections.emptyMap());

        Map<String, String> actualJdbcParams = dataSourceFactory.getJdbcParams();

        assertEquals(Collections.emptyMap(), actualJdbcParams);
    }

    @Test
    public void testGetJdbcUrlWithEmptyJdbcUrlParameters() {
        String host = "testHost";
        int port = 3306;
        String defaultSchema = "testSchema";

        String expectedJdbcUrl = String.format("jdbc:test:test%s", StringUtils.join(TEST_PARAMETERS, "&"));
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();

        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }
}