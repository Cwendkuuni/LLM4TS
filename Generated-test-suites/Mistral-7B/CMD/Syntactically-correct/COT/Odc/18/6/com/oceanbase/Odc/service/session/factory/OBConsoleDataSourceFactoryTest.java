package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactoryTestUtils;
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

/**
 * Test class for OBConsoleDataSourceFactory
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final int TEST_PORT = 12345;
    private static final String TEST_DEFAULT_SCHEMA = "test_schema";
    private static final String TEST_HOST = "test_host";
    private static final String TEST_JDBC_URL = "jdbc:test:test_url";

    private OBConsoleDataSourceFactory factory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @After
    public void tearDown() throws SQLException {
        if (factory != null) {
            DataSourceFactoryTestUtils.cleanDataSource(factory.getDataSource());
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource expectedDataSource = DataSourceFactoryTestUtils.getExpectedDataSource(TEST_USERNAME, TEST_PASSWORD,
                TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, DialectType.OB_MYSQL);
        DataSource actualDataSource = factory.getDataSource();
        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_MYSQL;
        DialectType actualDialectType = factory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        factory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:obmysql://test_host:12345/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);

        ConnectionConfig connectionConfigWithSsl = new ConnectionConfig();
        connectionConfigWithSsl.setUsername(TEST_USERNAME);
        connectionConfigWithSsl.setPassword(TEST_PASSWORD);
        connectionConfigWithSsl.setHost(TEST_HOST);
        connectionConfigWithSsl.setPort(TEST_PORT);
        connectionConfigWithSsl.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfigWithSsl.setType(ConnectType.MYSQL);
        connectionConfigWithSsl.setDialectType(DialectType.OB_MYSQL);
        connectionConfigWithSsl.setAutoCommit(true);
        connectionConfigWithSsl.setJdbcUrlParameters(new HashMap<>());
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfigWithSsl.setSslConfig(sslConfig);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test_keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("test_password");
        connectionConfigWithSsl.setSslFileEntry(sslFileEntry);

        factory = new OBConsoleDataSourceFactory(connectionConfigWithSsl, true);
        String expectedJdbcUrlWithSsl = "jdbc:obmysql://test_host:12345/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=true&trustServerCertificate=true&trustStore="
                + "test_keystore.p12&trustStorePassword=test_password&keyStore=" + "test_keystore.p12&keyStorePassword=test_password";
        String actualJdbcUrlWithSsl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrlWithSsl, actualJdbcUrlWithSsl);
    }

    @Test
    public void testGetJdbcUrlWithEmptyDefaultSchema() {
        connectionConfig.setDefaultSchema("");
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrlWithEmptyDefaultSchema = "jdbc:obmysql://test_host:12345//?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        String actualJdbcUrlWithEmptyDefaultSchema = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrlWithEmptyDefaultSchema, actualJdbcUrlWithEmptyDefaultSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJdbcUrlWithInvalidDialectType() {
        connectionConfig.setDialectType(DialectType.INVALID_DIALECT_TYPE);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        factory.resetUsername((mapper) -> newUsername);
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        factory.resetPassword((mapper) -> newPassword);
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        factory.resetHost((mapper) -> newHost);
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 6789;
        factory.resetPort((mapper) -> newPort);
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        factory.resetSchema((mapper) -> newSchema);
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        factory.resetParameters((mapper) -> newParameters);
        assertEquals(newParameters, factory.getParameters());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory cloneFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();

        assertEquals(originalFactory.getUsername(), cloneFactory.getUsername());
        assertEquals(originalFactory.getPassword(), cloneFactory.getPassword());
        assertEquals(originalFactory.getHost(), cloneFactory.getHost());
        assertEquals(originalFactory.getPort(), cloneFactory.getPort());
        assertEquals(originalFactory.getDefaultSchema(), cloneFactory.getDefaultSchema());
        assertEquals(originalFactory.getParameters(), cloneFactory.getParameters());
        assertEquals(originalFactory.getDialectType(), cloneFactory.getDialectType());
        assertEquals(originalFactory.getAutoCommit(), cloneFactory.getAutoCommit());
        assertEquals(originalFactory.getInitConnection(), cloneFactory.getInitConnection());
        assertEquals(originalFactory.getEventPublisher(), cloneFactory.getEventPublisher());
        assertEquals(originalFactory.getConnectionExtensionPoint(), cloneFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USERNAME, factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, factory.getPassword());
    }

    @Test
    public void testGetHost() {
        assertEquals(TEST_HOST, factory.getHost());
    }

    @Test
    public void testGetPort() {
        assertEquals(TEST_PORT, factory.getPort());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals(TEST_DEFAULT_SCHEMA, factory.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        Map<String, String> actualParameters = factory.getParameters();
        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testGetConnectType() {
        ConnectType expectedConnectType = ConnectType.MYSQL;
        ConnectType actualConnectType = factory.getConnectType();
        assertEquals(expectedConnectType, actualConnectType);
    }

    @Test
    public void testResetUsernameWithNullMapper() {
        try {
            factory.resetUsername(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetPasswordWithNullMapper() {
        try {
            factory.resetPassword(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetHostWithNullMapper() {
        try {
            factory.resetHost(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetPortWithNullMapper() {
        try {
            factory.resetPort(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetSchemaWithNullMapper() {
        try {
            factory.resetSchema(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetParametersWithNullMapper() {
        try {
            factory.resetParameters(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}