package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private Properties jdbcUrlParameters;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        connectionConfig = Mockito.spy(new ConnectionConfig());
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        jdbcUrlParameters = new Properties();
        jdbcUrlParameters.put("maxAllowedPacket", "64000000");
        jdbcUrlParameters.put("allowMultiQueries", "true");
        jdbcUrlParameters.put("connectTimeout", "5000");
        jdbcUrlParameters.put("zeroDateTimeBehavior", "CONVERT_TO_NULL");
        jdbcUrlParameters.put("noDatetimeStringSync", "true");
        jdbcUrlParameters.put("jdbcCompliantTruncation", "false");
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test_keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("test_password");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);

        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        eventPublisher = Mockito.mock(EventPublisher.class);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSourceFactory != null) {
            DataSource dataSource = dataSourceFactory.getDataSource();
            if (dataSource != null) {
                Connection connection = dataSource.getConnection();
                if (connection != null) {
                    connection.close();
                }
                dataSource.close();
            }
        }
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:oceanbase:mysql://localhost:3306/test_user@test_schema#test_cluster@localhost:3306/?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=CONVERT_TO_NULL&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=true&trustServerCertificate=true&trustStore=test_keystore.p12&trustStorePassword=test_password&keyStore=test_keystore.p12&keyStorePassword=test_password";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("test_user", username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("test_password", password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAutoCommit() {
        new OBConsoleDataSourceFactory(connectionConfig, null);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        dataSourceFactory.resetUsername((value) -> newUsername);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        dataSourceFactory.resetPassword((value) -> newPassword);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        dataSourceFactory.resetHost((value) -> newHost);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory.resetPort((value) -> newPort);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        dataSourceFactory.resetSchema((value) -> newSchema);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("new_param", "new_value");
        dataSourceFactory.resetParameters((value) -> newParameters);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetUsernameWithNullMapper() {
        dataSourceFactory.resetUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordWithNullMapper() {
        dataSourceFactory.resetPassword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHostWithNullMapper() {
        dataSourceFactory.resetHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortWithNullMapper() {
        dataSourceFactory.resetPort(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetSchemaWithNullMapper() {
        dataSourceFactory.resetSchema(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetParametersWithNullMapper() {
        dataSourceFactory.resetParameters(null);
    }

    @Test
    public void testCloneableDataSourceFactory() throws CloneNotSupportedException {
        DataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(dataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
        assertEquals(dataSourceFactory.getAutoCommit(), clonedDataSourceFactory.getAutoCommit());
        assertEquals(dataSourceFactory.getInitConnection(), clonedDataSourceFactory.getInitConnection());
        assertEquals(dataSourceFactory.getEventPublisher(), clonedDataSourceFactory.getEventPublisher());
        assertEquals(dataSourceFactory.getConnectionExtensionPoint(), clonedDataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSourceFactory.getDataSource().getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'test_schema'";
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        ScriptUtils.executeSql(connection, query);
        connection.close();
    }

    @Test(expected = SQLException.class)
    public void testExecuteQueryWithNullConnection() throws SQLException {
        DataSource dataSource = Mockito.mock(DataSource.class);
        OBConsoleDataSourceFactory newDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        newDataSourceFactory.resetDataSource((value) -> dataSource);
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'test_schema'";
        newDataSourceFactory.getDataSource().getConnection();
        try {
            ScriptUtils.executeSql(null, query);
        } catch (SQLException e) {
            assertEquals("Connection must not be null", e.getMessage());
            throw e;
        }
    }
}