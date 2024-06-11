package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
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

/**
 * Test class for {@link OBConsoleDataSourceFactory}
 */
public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 63306;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final Map<String, String> TEST_PARAMETERS = new HashMap<>();
    static {
        TEST_PARAMETERS.put("maxAllowedPacket", "64000000");
        TEST_PARAMETERS.put("allowMultiQueries", "true");
        TEST_PARAMETERS.put("connectTimeout", "5000");
        TEST_PARAMETERS.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        TEST_PARAMETERS.put("noDatetimeStringSync", "true");
        TEST_PARAMETERS.put("jdbcCompliantTruncation", "false");
    }

    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Test
    public void testOBConsoleDataSourceFactory() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setParameters(TEST_PARAMETERS);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(new SSLConfig(true, null));

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        assertNotNull(dataSourceFactory.getDataSource());
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setTenantName("testTenant");
        connectionConfig.setClusterName("testCluster");

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("testUser@testTenant#testCluster", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword(TEST_PASSWORD);

        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals(TEST_PASSWORD, password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint("localhost", 8001, "localhost", 8080));
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setSslFileEntry(new SSLFileEntry("test.p12", "testPassword"));

        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

        assertEquals("true", jdbcParams.get("allowMultiQueries"));
        assertEquals("5000", jdbcParams.get("connectTimeout"));
        assertEquals("false", jdbcParams.get("jdbcCompliantTruncation"));
        assertEquals("localhost:8080", jdbcParams.get("socksProxyHost"));
        assertEquals("8001", jdbcParams.get("socksProxyPort"));
        assertEquals("test.p12", jdbcParams.get("trustStore"));
        assertEquals("testPassword", jdbcParams.get("trustStorePassword"));
        assertEquals("test.p12", jdbcParams.get("keyStore"));
        assertEquals("testPassword", jdbcParams.get("keyStorePassword"));
        assertEquals("true", jdbcParams.get("useSSL"));
        assertEquals("true", jdbcParams.get("trustServerCertificate"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setParameters(TEST_PARAMETERS);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(new SSLConfig(true, null));

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);

        Properties connectionProperties = dataSource.getConnectionProperties();
        assertEquals("false", connectionProperties.getProperty("allowLoadLocalInfile"));
        assertEquals("false", connectionProperties.getProperty("allowUrlInLocalInfile"));
        assertEquals("", connectionProperties.getProperty("allowLoadLocalInfileInPath"));
        assertEquals("false", connectionProperties.getProperty("autoDeserialize"));
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setParameters(TEST_PARAMETERS);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(new SSLConfig(true, null));

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        assertNotNull(clonedDataSourceFactory.getDataSource());
        assertEquals(dataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldUsername = dataSourceFactory.getUsername();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public String map(String s) {
                return "newUsername";
            }
        };
        dataSourceFactory.resetUsername(mapper);

        String newUsername = dataSourceFactory.getUsername();
        assertEquals("newUsername", newUsername);
        assertEquals(oldUsername, TEST_USERNAME);
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldPassword = dataSourceFactory.getPassword();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public String map(String s) {
                return "newPassword";
            }
        };
        dataSourceFactory.resetPassword(mapper);

        String newPassword = dataSourceFactory.getPassword();
        assertEquals("newPassword", newPassword);
        assertEquals(oldPassword, TEST_PASSWORD);
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldHost = dataSourceFactory.getHost();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public String map(String s) {
                return "newHost";
            }
        };
        dataSourceFactory.resetHost(mapper);

        String newHost = dataSourceFactory.getHost();
        assertEquals("newHost", newHost);
        assertEquals(oldHost, TEST_HOST);
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        int oldPort = dataSourceFactory.getPort();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public int map(int i) {
                return 12345;
            }
        };
        dataSourceFactory.resetPort(mapper);

        int newPort = dataSourceFactory.getPort();
        assertEquals(12345, newPort);
        assertEquals(oldPort, TEST_PORT);
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldSchema = dataSourceFactory.getDefaultSchema();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public String map(String s) {
                return "newSchema";
            }
        };
        dataSourceFactory.resetSchema(mapper);

        String newSchema = dataSourceFactory.getDefaultSchema();
        assertEquals("newSchema", newSchema);
        assertEquals(oldSchema, TEST_DEFAULT_SCHEMA);
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setParameters(TEST_PARAMETERS);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> oldParameters = dataSourceFactory.getParameters();

        ConnectionMapper mapper = new ConnectionMapper() {
            @Override
            public Map<String, String> map(Map<String, String> map) {
                Map<String, String> newMap = new HashMap<>();
                newMap.put("maxAllowedPacket", "78900000");
                newMap.putAll(map);
                return newMap;
            }
        };
        dataSourceFactory.resetParameters(mapper);

        Map<String, String> newParameters = dataSourceFactory.getParameters();
        assertNotNull(newParameters);
        assertEquals("78900000", newParameters.get("maxAllowedPacket"));
        assertEquals(oldParameters.get("maxAllowedPacket"), "64000000");
    }

    @Before
    public void setUp() {
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE);
        eventPublisher = new EventPublisher();
    }
}