package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
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
import com.oceanbase.odc.test.mockito.TestConnectionExtensionPoint;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory factory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionInitializer connectionInitializer;
    private SessionCreatedInitializer sessionCreatedInitializer;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(true, new SSLFileEntry("/path/to/keystore.p12", "password")));
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        sessionCreatedInitializer = mock(SessionCreatedInitializer.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(connectionInitializer));
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(sessionCreatedInitializer));
        when(connectionExtensionPoint.generateJdbcUrl("localhost", 3306, "test_schema", null)).thenAnswer(invocation -> "jdbc:mysql://localhost:3306/test_schema");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionProperties()).thenReturn(new Properties());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testGetDataSource() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        factory = new OBConsoleDataSourceFactory(null, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        ConnectionExtensionPoint connectionExtensionPoint = null;
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true, connectionExtensionPoint);
        factory.getDataSource();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = factory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetJdbcUrlWithNoDefaultSchema() {
        connectionConfig.setDefaultSchema(null);
        String jdbcUrl = factory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetJdbcUrlWithProxy() {
        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        tenantEndpoint.setProxyHost("proxy.com");
        tenantEndpoint.setProxyPort(8080);
        connectionConfig.setEndpoint(tenantEndpoint);
        String jdbcUrl = factory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://proxy.com:8080/test_schema";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetJdbcUrlWithSsl() {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String jdbcUrl = factory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema";
        assertEquals(expectedJdbcUrl, jdbcUrl);

        SSLConfig sslConfig = new SSLConfig(true, new SSLFileEntry("/path/to/keystore.p12", "password"));
        connectionConfig.setSslConfig(sslConfig);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        jdbcUrl = factory.getJdbcUrl();
        expectedJdbcUrl += "?useSSL=true&trustStore=" + "/path/to/keystore.p12" + "&trustStorePassword=password";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = factory.getUsername();
        String expectedUsername = "root";
        assertEquals(expectedUsername, username);
    }

    @Test
    public void testGetPassword() {
        String password = factory.getPassword();
        String expectedPassword = "password";
        assertEquals(expectedPassword, password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = factory.getDialectType();
        DialectType expectedDialectType = DialectType.OB_MYSQL;
        assertEquals(expectedDialectType, dialectType);
    }

    @Test
    public void testDeepCopy() throws SQLException {
        DataSourceFactory originalFactory = factory;
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertNotNull(clonedFactory.getDataSource());
    }

    @Test
    public void testResetUsername() {
        String oldUsername = factory.getUsername();
        String newUsername = "new_username";
        factory.resetUsername(Mockito.mock(ValueMapper.class));
        String newUsernameAfterReset = factory.getUsername();
        assertEquals(newUsername, newUsernameAfterReset);
        assertNotEquals(oldUsername, newUsernameAfterReset);
    }

    @Test
    public void testResetPassword() {
        String oldPassword = factory.getPassword();
        String newPassword = "new_password";
        factory.resetPassword(Mockito.mock(ValueMapper.class));
        String newPasswordAfterReset = factory.getPassword();
        assertEquals(newPassword, newPasswordAfterReset);
        assertNotEquals(oldPassword, newPasswordAfterReset);
    }

    @Test
    public void testResetHost() {
        String oldHost = factory.getHost();
        String newHost = "new_host";
        factory.resetHost(Mockito.mock(ValueMapper.class));
        String newHostAfterReset = factory.getHost();
        assertEquals(newHost, newHostAfterReset);
        assertNotEquals(oldHost, newHostAfterReset);
    }

    @Test
    public void testResetPort() {
        int oldPort = factory.getPort();
        int newPort = 1234;
        factory.resetPort(Mockito.mock(ValueMapper.class));
        int newPortAfterReset = factory.getPort();
        assertEquals(newPort, newPortAfterReset);
        assertNotEquals(oldPort, newPortAfterReset);
    }

    @Test
    public void testResetSchema() {
        String oldSchema = factory.getDefaultSchema();
        String newSchema = "new_schema";
        factory.resetSchema(Mockito.mock(ValueMapper.class));
        String newSchemaAfterReset = factory.getDefaultSchema();
        assertEquals(newSchema, newSchemaAfterReset);
        assertNotEquals(oldSchema, newSchemaAfterReset);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParameters = factory.getParameters();
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("testKey", "testValue");
        factory.resetParameters(Mockito.mock(ValueMapper.class));
        Map<String, String> newParametersAfterReset = factory.getParameters();
        assertEquals(newParameters, newParametersAfterReset);
        assertNotEquals(oldParameters, newParametersAfterReset);
    }

    @Test
    public void testGetDataSourceWithInitConnectionFalse() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNull(connection);
    }

    @Test
    public void testGetDataSourceWithInitConnectionTrue() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetDataSourceWithNoConnectionInitializers() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetDataSourceWithConnectionInitializers() throws SQLException {
        ArgumentCaptor<ConnectionInitializer> connectionInitializerCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        when(connectionInitializer.initializeDatabase(Mockito.any(Connection.class), Mockito.anyString())).thenReturn(true);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
        Mockito.verify(connectionInitializer).initializeDatabase(Mockito.capture(Connection.class), Mockito.eq("init_db"));
        Connection initialConnection = connection.clone();
        assertNotNull(initialConnection);
    }

    @Test
    public void testGetDataSourceWithSessionCreatedInitializer() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Arrays.asList(connectionInitializer, sessionCreatedInitializer));
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
        Mockito.verify(sessionCreatedInitializer).initializeSession(Mockito.capture(Connection.class));
        Connection sessionCreatedConnection = connection.clone();
        assertNotNull(sessionCreatedConnection);
    }

    private void assertEquals(String expected, String actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertEquals(expected, actual);
        }
    }

}