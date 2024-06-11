package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceUtils;
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

    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_HOST = "test_host";
    private static final int TEST_PORT = 1234;
    private static final String TEST_SCHEMA = "test_schema";
    private static final String TEST_JDBC_URL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_SCHEMA;

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.LONG_CONNECTION_POOL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @After
    public void tearDown() throws Exception {
        closeDataSource();
    }

    @Test
    public void testGetDataSource() throws Exception {
        dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertSame(dataSourceFactory.getDialectType(), dataSource.getConnection().createStatement().getConnection().getMetaData().getDatabaseProductName());
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(TEST_JDBC_URL, jdbcUrl);
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals(TEST_USERNAME, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() throws Exception {
        assertEquals(TEST_PASSWORD, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDefaultSchema() throws Exception {
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertEquals(TEST_SCHEMA, defaultSchema);
    }

    @Test
    public void testGetJdbcParams() throws Exception {
        Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
        assertNotNull(jdbcParams);
        assertEquals(jdbcParams.size(), 13);
        assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
        assertTrue(jdbcParams.containsKey("allowMultiQueries"));
        assertTrue(jdbcParams.containsKey("connectTimeout"));
        assertTrue(jdbcParams.containsKey("zeroDateTimeBehavior"));
        assertTrue(jdbcParams.containsKey("noDatetimeStringSync"));
        assertTrue(jdbcParams.containsKey("jdbcCompliantTruncation"));
        assertTrue(jdbcParams.containsKey("sendConnectionAttributes"));
        assertTrue(jdbcParams.containsKey("socksProxyHost"));
        assertTrue(jdbcParams.containsKey("socksProxyPort"));
        assertTrue(jdbcParams.containsKey("trustServerCertificate"));
        assertTrue(jdbcParams.containsKey("useSSL"));
    }

    @Test
    public void testGetDialectType() throws Exception {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory original = dataSourceFactory;
        OBConsoleDataSourceFactory copied = original.deepCopy();
        assertNotSame(original, copied);
        assertEquals(original.getUsername(), copied.getUsername());
        assertEquals(original.getPassword(), copied.getPassword());
        assertEquals(original.getHost(), copied.getHost());
        assertEquals(original.getPort(), copied.getPort());
        assertEquals(original.getDefaultSchema(), copied.getDefaultSchema());
        assertEquals(original.getJdbcParams(), copied.getJdbcParams());
        assertEquals(original.getDialectType(), copied.getDialectType());
        assertEquals(original.getAutoCommit(), copied.getAutoCommit());
        assertEquals(original.getInitConnection(), copied.getInitConnection());
        assertEquals(original.getEventPublisher(), copied.getEventPublisher());
        assertEquals(original.getConnectionExtensionPoint(), copied.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() throws Exception {
        String newUsername = "new_username";
        String oldUsername = TEST_USERNAME;
        dataSourceFactory.resetUsername(s -> s.replace(oldUsername, newUsername));
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() throws Exception {
        String newPassword = "new_password";
        String oldPassword = TEST_PASSWORD;
        dataSourceFactory.resetPassword(s -> s.replace(oldPassword, newPassword));
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        String newHost = "new_host";
        String oldHost = TEST_HOST;
        dataSourceFactory.resetHost(s -> s.replace(oldHost, newHost));
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() throws Exception {
        int newPort = 5678;
        int oldPort = TEST_PORT;
        dataSourceFactory.resetPort(i -> i + 1);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() throws Exception {
        String newSchema = "new_schema";
        String oldSchema = TEST_SCHEMA;
        dataSourceFactory.resetSchema(s -> s.replace(oldSchema, newSchema));
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("new_key", "new_value");
        Map<String, String> oldParams = dataSourceFactory.getJdbcParams();
        dataSourceFactory.resetParameters(m -> {
            m.putAll(newParams);
            m.remove("sendConnectionAttributes");
            return m;
        });
        Map<String, String> params = dataSourceFactory.getJdbcParams();
        assertEquals(newParams, params);
    }

    @Test
    public void testInitConnection() throws Exception {
        ConnectionInitializer initializer = connectionExtensionPoint.getConnectionInitializers().get(0);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertNull(dataSource.getConnection());
        dataSourceFactory.initConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        initializer.initialize(connection);
        closeDataSource();
        dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAutoCommit() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            connection.createStatement().execute("DROP TABLE IF EXISTS test_table");
            connection.createStatement().execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(10))");
            connection.createStatement().execute("INSERT INTO test_table VALUES (1, 'test')");
            connection.setAutoCommit(true);
            assertEquals(1, DataSourceUtils.countRows(connection, "SELECT COUNT(*) FROM test_table"));
            connection.createStatement().execute("DROP TABLE test_table");
        } finally {
            connection.close();
        }
        dataSource.close();
    }

    private void closeDataSource() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}