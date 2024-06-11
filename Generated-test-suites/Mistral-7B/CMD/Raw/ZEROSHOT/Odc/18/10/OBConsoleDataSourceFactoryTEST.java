import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.test.util.TestConstants;
import com.oceanbase.odc.test.util.TestHelper;
import com.oceanbase.odc.test.util.TestUtils;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    @InjectMocks
    private OBConsoleDataSourceFactory testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TestHelper.initMocks(connectionExtensionPoint);
        TestHelper.initMocks(connectionInitializer);
        TestHelper.initMocks(sessionCreatedInitializer);
        TestUtils.initMocks(testObject);
    }

    @After
    public void tearDown() {
        reset(connectionExtensionPoint, eventPublisher, connectionInitializer, sessionCreatedInitializer);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(TestConstants.DIALECT_ORACLE);
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        String jdbcUrl = testObject.getJdbcUrl();
        assertEquals(TestConstants.ORACLE_JDBC_URL, jdbcUrl);

        DataSource dataSource = testObject.getDataSource();
        assertNotNull(dataSource);
        assertSame(SingleConnectionDataSource.class, dataSource.getClass());
        assertSame(testObject.getUsername(), dataSource.getUsername());
        assertSame(testObject.getPassword(), dataSource.getPassword());
        assertEquals(TestConstants.ORACLE_DRIVER_CLASS, dataSource.getDriverClassName());

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertSame(TestConstants.ORACLE_JDBC_URL, connection.getConnectionURL());
        assertSame(testObject.getUsername(), connection.getUserInfo().getUserName());
        assertSame(testObject.getPassword(), connection.getConnection().getPassword());
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(TestConstants.DIALECT_ORACLE);
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        String jdbcUrl = testObject.getJdbcUrl();
        assertEquals(TestConstants.ORACLE_JDBC_URL, jdbcUrl);

        ConnectionConfig connectionConfigWithTenant = new ConnectionConfig();
        connectionConfigWithTenant.setDialectType(TestConstants.DIALECT_ORACLE);
        connectionConfigWithTenant.setTenantName("testTenant");
        testObject = new OBConsoleDataSourceFactory(connectionConfigWithTenant, true, true);

        String jdbcUrlWithTenant = testObject.getJdbcUrl();
        String expectedJdbcUrlWithTenant = TestConstants.ORACLE_JDBC_URL_WITH_TENANT;
        assertEquals(expectedJdbcUrlWithTenant, jdbcUrlWithTenant);

        ConnectionConfig connectionConfigWithSchema = new ConnectionConfig();
        connectionConfigWithSchema.setDialectType(TestConstants.DIALECT_ORACLE);
        connectionConfigWithSchema.setSchema("testSchema");
        testObject = new OBConsoleDataSourceFactory(connectionConfigWithSchema, true, true);

        String jdbcUrlWithSchema = testObject.getJdbcUrl();
        String expectedJdbcUrlWithSchema = TestConstants.ORACLE_JDBC_URL_WITH_SCHEMA;
        assertEquals(expectedJdbcUrlWithSchema, jdbcUrlWithSchema);

        ConnectionConfig connectionConfigWithCluster = new ConnectionConfig();
        connectionConfigWithCluster.setDialectType(TestConstants.DIALECT_ORACLE);
        connectionConfigWithCluster.setClusterName("testCluster");
        testObject = new OBConsoleDataSourceFactory(connectionConfigWithCluster, true, true);

        String jdbcUrlWithCluster = testObject.getJdbcUrl();
        String expectedJdbcUrlWithCluster = TestConstants.ORACLE_JDBC_URL_WITH_CLUSTER;
        assertEquals(expectedJdbcUrlWithCluster, jdbcUrlWithCluster);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(TestConstants.DIALECT_ORACLE);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        Map<String, String> jdbcParams = testObject.getJdbcParams();
        assertEquals(TestConstants.EMPTY_MAP, jdbcParams);

        ConnectionConfig connectionConfigWithParams = new ConnectionConfig();
        connectionConfigWithParams.setDialectType(TestConstants.DIALECT_ORACLE);
        connectionConfigWithParams.setJdbcUrlParameters(new HashMap<>());
        connectionConfigWithParams.getJdbcUrlParameters().put("key", "value");
        testObject = new OBConsoleDataSourceFactory(connectionConfigWithParams, true, true);

        Map<String, String> jdbcParamsWithKeyValue = testObject.getJdbcParams();
        Map<String, String> expectedJdbcParamsWithKeyValue = new HashMap<>();
        expectedJdbcParamsWithKeyValue.put("key", "value");
        assertEquals(expectedJdbcParamsWithKeyValue, jdbcParamsWithKeyValue);

        ConnectionConfig connectionConfigWithSsl = new ConnectionConfig();
        connectionConfigWithSsl.setDialectType(TestConstants.DIALECT_ORACLE);
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfigWithSsl.setSslConfig(sslConfig);
        testObject = new OBConsoleDataSourceFactory(connectionConfigWithSsl, true, true);

        Map<String, String> jdbcParamsWithSsl = testObject.getJdbcParams();
        Map<String, String> expectedJdbcParamsWithSsl = new HashMap<>();
        expectedJdbcParamsWithSsl.put("useSSL", "true");
        expectedJdbcParamsWithSsl.put("trustServerCertificate", "true");
        expectedJdbcParamsWithSsl.put("trustStore", "test.p12");
        expectedJdbcParamsWithSsl.put("trustStorePassword", "password");
        assertEquals(expectedJdbcParamsWithSsl, jdbcParamsWithSsl);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(TestConstants.DIALECT_ORACLE);
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        OBConsoleDataSourceFactory cloned = testObject.deepCopy();
        assertEquals(testObject.getUsername(), cloned.getUsername());
        assertEquals(testObject.getPassword(), cloned.getPassword());
        assertEquals(testObject.getHost(), cloned.getHost());
        assertEquals(testObject.getPort(), cloned.getPort());
        assertEquals(testObject.getDefaultSchema(), cloned.getDefaultSchema());
        assertEquals(testObject.getParameters(), cloned.getParameters());
        assertEquals(testObject.getConnectionConfig(), cloned.getConnectionConfig());
        assertEquals(testObject.getAutoCommit(), cloned.getAutoCommit());
        assertEquals(testObject.getInitConnection(), cloned.getInitConnection());
        assertEquals(testObject.getEventPublisher(), cloned.getEventPublisher());
        assertEquals(testObject.getConnectionExtensionPoint(), cloned.getConnectionExtensionPoint());
    }
}