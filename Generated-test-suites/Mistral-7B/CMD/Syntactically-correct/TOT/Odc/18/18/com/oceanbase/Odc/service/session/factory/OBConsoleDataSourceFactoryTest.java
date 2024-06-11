package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
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

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @InjectMocks
    private OBConsoleDataSourceFactory testObj;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6101);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(new SSLConfig());
        testObj = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        testObj.connectionExtensionPoint = connectionExtensionPoint;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        String jdbcUrl = testObj.getJdbcUrl();
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) testObj.getDataSource();

        assertNotNull(jdbcUrl);
        assertNotNull(dataSource);
        assertEquals("localhost", dataSource.getUrl().split(":")[1]);
        assertEquals("testuser", dataSource.getUsername());
        assertEquals("testpassword", dataSource.getPassword());
        assertEquals("test_schema", testObj.getDefaultSchema());

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = testObj.getJdbcUrl();
        String expectedUrl = "jdbc:oceanbase:observer://localhost:6101/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = testObj.getUsername();
        assertEquals("testuser", username);
    }

    @Test
    public void testGetPassword() {
        String password = testObj.getPassword();
        assertEquals("testpassword", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = testObj.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testGetDefaultSchema() {
        String schema = testObj.getDefaultSchema();
        assertEquals("test_schema", schema);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        testObj.resetUsername(newUsername::toString);
        assertEquals(newUsername, testObj.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        testObj.resetPassword(newPassword::toString);
        assertEquals(newPassword, testObj.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        testObj.resetHost(newHost::toString);
        assertEquals(newHost, testObj.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 6102;
        testObj.resetPort(newPort::valueOf);
        assertEquals(newPort, testObj.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        testObj.resetSchema(newSchema::toString);
        assertEquals(newSchema, testObj.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("testKey", "testValue");
        testObj.resetParameters(newParams::putAll);
        assertEquals(newParams, testObj.getParameters());
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6101);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        testObj = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        testObj.connectionExtensionPoint = connectionExtensionPoint;

        String jdbcUrl = testObj.getJdbcUrl();
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) testObj.getDataSource();

        assertNotNull(jdbcUrl);
        assertNotNull(dataSource);
        assertEquals("localhost", dataSource.getUrl().split(":")[1]);
        assertEquals("testuser", dataSource.getUsername());
        assertEquals("testpassword", dataSource.getPassword());
        assertEquals("test_schema", testObj.getDefaultSchema());

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();

        verify(connectionExtensionPoint, atLeastOnce()).generateJdbcUrl(eq("localhost"), eq(6101), eq("test_schema"), anyMap());
        verify(connectionExtensionPoint, atLeastOnce()).getDriverClassName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithInvalidParam() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6101);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        testObj = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        testObj.username = null;
        testObj.getDataSource();
    }
}