package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isNotBlank;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.ConnectionMapper;
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
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for the OBConsoleDataSourceFactory class
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setup() {
        String dialectType = DialectType.OB_MYSQL.name();
        SSLConfig sslConfig = new SSLConfig().setEnabled(true).setKeyStoreFilePath("test.p12")
                .setKeyStoreFilePassword("testPassword");
        SSLFileEntry sslFileEntry = new SSLFileEntry().setKeyStoreFilePath("test.p12").setKeyStoreFilePassword(
                "testPassword");
        ConnectionConfig connectionConfig = new ConnectionConfig().setUsername("testUser")
                .setPassword("testPassword").setHost("localhost").setPort(3306).setDialectType(DialectType.OB_MYSQL)
                .setDefaultSchema("schema_name").setSslConfig(sslConfig).setSslFileEntry(sslFileEntry)
                .setTenantName("test_tenant").setClusterName("test_cluster").setJdbcUrlParameters(
                        Map.of("testKey", "testValue"));
        this.connectionConfig = connectionConfig;
        this.eventPublisher = new EventPublisher();
        this.connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(dialectType);
        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(
                connectionConfig.getHost(), connectionConfig.getPort(),
                getDefaultSchema(connectionConfig), getJdbcParams(connectionConfig));
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser@test_tenant";
        String actualUsername = dataSourceFactory.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String actualPassword = dataSourceFactory.getPassword();
        assertNull(actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("maxAllowedPacket", "64000000");
        expectedJdbcParams.put("allowMultiQueries", "true");
        expectedJdbcParams.put("connectTimeout", "5000");
        expectedJdbcParams.put("zeroDateTimeBehavior", "0");
        expectedJdbcParams.put("noDatetimeStringSync", "true");
        expectedJdbcParams.put("jdbcCompliantTruncation", "false");
        expectedJdbcParams.put("socksProxyHost", "testProxyHost");
        expectedJdbcParams.put("socksProxyPort", "8080");
        expectedJdbcParams.put("useSSL", "true");
        expectedJdbcParams.put("trustServerCertificate", "true");
        expectedJdbcParams.put("trustStore", "test.p12");
        expectedJdbcParams.put("trustStorePassword", "testPassword");
        expectedJdbcParams.put("keyStore", "test.p12");
        expectedJdbcParams.put("keyStorePassword", "testPassword");
        expectedJdbcParams.put("allowLoadLocalInfile", "false");
        expectedJdbcParams.put("allowUrlInLocalInfile", "false");
        expectedJdbcParams.put("allowLoadLocalInfileInPath", "");
        expectedJdbcParams.put("autoDeserialize", "false");
        expectedJdbcParams.put("testKey", "testValue");

        Map<String, String> actualJdbcParams = dataSourceFactory.getJdbcParams();
        assertEquals(expectedJdbcParams, actualJdbcParams);
    }

    @Test
    public void testGetDataSource() {
        DataSource actualDataSource = dataSourceFactory.getDataSource();
        assertNotNull(actualDataSource);
        assertTrue(actualDataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_MYSQL;
        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        Map<String, String> clonedJdbcParams = clonedDataSourceFactory.getJdbcParams();
        Map<String, String> originalJdbcParams = dataSourceFactory.getJdbcParams();
        Map<String, String> mappedJdbcParams = new HashMap<>();

        for (Map.Entry<String, String> entry : originalJdbcParams.entrySet()) {
            mappedJdbcParams.put(entry.getKey(), mapper.map(entry.getValue()));
        }

        assertEquals(mappedJdbcParams, clonedJdbcParams);
    }

    @Test
    public void testResetUsername() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        String newUsername = "newUsername";
        clonedDataSourceFactory.resetUsername(mapper);

        String clonedUsername = clonedDataSourceFactory.getUsername();
        String originalUsername = dataSourceFactory.getUsername();

        assertEquals(newUsername, clonedUsername);
        assertEquals(originalUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        String newPassword = "newPassword";
        clonedDataSourceFactory.resetPassword(mapper);

        assertNull(clonedDataSourceFactory.getPassword());
        assertNotNull(dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        String newHost = "newHost";
        clonedDataSourceFactory.resetHost(mapper);

        String clonedHost = clonedDataSourceFactory.getHost();
        String originalHost = dataSourceFactory.getHost();

        assertEquals(newHost, clonedHost);
        assertEquals(originalHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        int newPort = 12345;
        clonedDataSourceFactory.resetPort(mapper);

        int clonedPort = clonedDataSourceFactory.getPort();
        int originalPort = dataSourceFactory.getPort();

        assertEquals(newPort, clonedPort);
        assertEquals(originalPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        String newSchema = "newSchema";
        clonedDataSourceFactory.resetSchema(mapper);

        String clonedSchema = clonedDataSourceFactory.getDefaultSchema();
        String originalSchema = dataSourceFactory.getDefaultSchema();

        assertEquals(newSchema, clonedSchema);
        assertEquals(originalSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        Map<String, String> newParams = new HashMap<>();
        newParams.put("newKey", "newValue");
        clonedDataSourceFactory.resetParameters(mapper);

        Map<String, String> clonedParams = clonedDataSourceFactory.getParameters();
        Map<String, String> originalParams = dataSourceFactory.getParameters();

        assertEquals(newParams, clonedParams);
        assertNotEquals(newParams, originalParams);
    }
}