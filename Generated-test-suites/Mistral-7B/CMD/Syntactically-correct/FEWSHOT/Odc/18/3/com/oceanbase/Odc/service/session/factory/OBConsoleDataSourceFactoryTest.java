package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isBlank;
import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

/**
 * Test class for {@link OBConsoleDataSourceFactory}
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class OBConsoleDataSourceFactoryTest implements CloneableDataSourceFactory {

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 6103;
    private static final String TEST_DEFAULT_SCHEMA = "test_schema";
    private static final String TEST_JDBC_URL = "jdbc:oceanbase://localhost:6103/test_schema";
    private static final ConnectionConfig TEST_CONNECTION_CONFIG = new ConnectionConfig(ConnectType.OB_MYSQL, TEST_HOST, TEST_PORT,
            TEST_USERNAME, TEST_PASSWORD, null, null, null, null, null, null);
    private OBConsoleDataSourceFactory testSubject;

    @Before
    public void setUp() {
        testSubject = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true, true);
    }

    @After
    public void tearDown() throws Exception {
        DriverManager.getConnection(testSubject.getJdbcUrl()).close();
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = testSubject.getDataSource();
        assertNotNull(dataSource);
        assertEquals(testSubject.getDialectType(), dataSource.getConnection().getMetaData().getDatabaseProductName());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(TEST_CONNECTION_CONFIG.getDialectType(), testSubject.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        DataSourceFactory deepCopy = testSubject.deepCopy();
        assertEquals(testSubject.getDialectType(), deepCopy.getDialectType());
        assertEquals(testSubject.getUsername(), deepCopy.getUsername());
        assertEquals(testSubject.getPassword(), deepCopy.getPassword());
        assertEquals(testSubject.getHost(), deepCopy.getHost());
        assertEquals(testSubject.getPort(), deepCopy.getPort());
        assertEquals(testSubject.getDefaultSchema(), deepCopy.getDefaultSchema());
        assertEquals(testSubject.getParameters(), deepCopy.getParameters());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = s -> s.replaceAll("test_username", "new_username");
        testSubject.resetUsername(mapper);
        assertEquals("new_username", testSubject.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = s -> s.replaceAll("test_password", "new_password");
        testSubject.resetPassword(mapper);
        assertEquals("new_password", testSubject.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = s -> s.replaceAll("localhost", "new_host");
        testSubject.resetHost(mapper);
        assertEquals("new_host", testSubject.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = i -> i + 1;
        testSubject.resetPort(mapper);
        assertEquals(TEST_PORT + 1, testSubject.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = s -> "new_schema";
        testSubject.resetSchema(mapper);
        assertEquals("new_schema", testSubject.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newKey", "newValue");
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = m -> newParameters;
        testSubject.resetParameters(mapper);
        assertEquals(newParameters, testSubject.getParameters());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals(TEST_JDBC_URL, testSubject.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USERNAME, testSubject.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, testSubject.getPassword());
    }

    @Test
    public void testGetHost() {
        assertEquals(TEST_HOST, testSubject.getHost());
    }

    @Test
    public void testGetPort() {
        assertEquals(TEST_PORT, testSubject.getPort());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals(TEST_DEFAULT_SCHEMA, testSubject.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        assertEquals(testSubject.getParameters(), new HashMap<>());
    }

    @Test
    public void testGetConnectionConfig() {
        ConnectionConfig connectionConfig = testSubject.getConnectionConfig();
        assertEquals(TEST_CONNECTION_CONFIG, connectionConfig);
    }

    @Test
    public void testGetAutoCommit() {
        assertTrue(testSubject.isAutoCommit());
    }

    @Test
    public void testIsInitConnection() {
        assertTrue(testSubject.isInitConnection());
    }

    @Test
    public void testCreateDataSourceWithNoInitializers() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, false, false);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testCreateDataSourceWithInitializers() {
        List<ConnectionInitializer> initializers = ConnectionPluginUtil.createConnectionInitializers();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true, true);
        factory.addInitializer(new SessionCreatedInitializer(TEST_CONNECTION_CONFIG));
        factory.addInitializer(new ConnectionInitializer() {
            @Override
            public void initializeDataSource(DataSource dataSource) {
                BasicDataSource bds = (BasicDataSource) dataSource;
                bds.setInitialSize(1);
            }
        });
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof BasicDataSource);
        assertEquals(1, ((BasicDataSource) dataSource).getInitialSize());
    }

    @Test
    public void testGenerateJdbcUrlWithProxy() {
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("proxyHost");
        endpoint.setProxyPort(1234);
        ConnectionConfig connectionConfig = new ConnectionConfig(ConnectType.OB_MYSQL, TEST_HOST, TEST_PORT, TEST_USERNAME,
                TEST_PASSWORD, TEST_CONNECTION_CONFIG.getSslConfig(), null, endpoint, null, null, null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertEquals("jdbc:oceanbase://proxyHost:1234/test_schema", factory.getJdbcUrl());
    }

    @Test
    public void testGenerateJdbcUrlWithSsl() {
        ConnectionConfig connectionConfig = new ConnectionConfig(ConnectType.OB_ORACLE, TEST_HOST, TEST_PORT, TEST_USERNAME,
                TEST_PASSWORD, new SSLConfig(true), null, null, null, null, null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertEquals("jdbc:oceanbase://localhost:6103/test_schema;useSSL=true;trustServerCertificate=true",
                factory.getJdbcUrl());
    }

    @Test
    public void testGenerateJdbcUrlWithSslAndTrustStore() {
        ConnectionConfig connectionConfig = new ConnectionConfig(ConnectType.OB_ORACLE, TEST_HOST, TEST_PORT, TEST_USERNAME,
                TEST_PASSWORD, new SSLConfig(true), new SSLFileEntry("test_keystore.p12", "test_password"), null, null, null,
                null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertEquals("jdbc:oceanbase://localhost:6103/test_schema;useSSL=true;trustServerCertificate=false;trustStore=test_keystore.p12;"
                + "trustStorePassword=test_password", factory.getJdbcUrl());
    }

    @Test
    public void testGenerateJdbcUrlWithNoSslAndNoProxy() {
        ConnectionConfig connectionConfig = new ConnectionConfig(ConnectType.OB_MYSQL, TEST_HOST, TEST_PORT, TEST_USERNAME,
                TEST_PASSWORD, null, null, null, null, null, null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertEquals("jdbc:oceanbase://localhost:6103/test_schema", factory.getJdbcUrl());
    }

    @Test
    public void testGenerateJdbcUrlWithJdbcUrlParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig(ConnectType.OB_MYSQL, TEST_HOST, TEST_PORT, TEST_USERNAME,
                TEST_PASSWORD, null, null, null, null, new Properties() {{
                    put("property1", "value1");
                    put("property2", "value2");
                }}, null, null, null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertEquals("jdbc:oceanbase://localhost:6103/test_schema;property1=value1;property2=value2", factory.getJdbcUrl());
    }
}