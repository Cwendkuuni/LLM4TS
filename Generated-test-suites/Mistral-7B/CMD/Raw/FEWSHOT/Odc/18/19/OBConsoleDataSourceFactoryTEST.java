package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DelegatingDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionMapper;
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
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.ValueMapper;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private OBConsoleDataSourceFactory clonedDataSourceFactory;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setJdbcUrlParameters(Map.of("key", "value"));
        connectionConfig.setType(ConnectType.OB_MYSQL);
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setEndpoint(new OBTenantEndpoint("proxyHost", 123));

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
        clonedDataSourceFactory = dataSourceFactory.deepCopy();
        ConnectionExtensionPoint connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals(dataSourceFactory.getJdbcUrl(), dataSource.getConnectionProperties().getProperty("url"));
        assertEquals(dataSourceFactory.getUsername(), dataSource.getUsername());
        assertEquals(dataSourceFactory.getPassword(), dataSource.getPassword());
        assertEquals(dataSourceFactory.getDialectType(), dataSource.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        assertEquals(dataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        assertNotEquals(dataSourceFactory, clonedDataSourceFactory);
        assertNotNull(clonedDataSourceFactory.getEventPublisher());
        assertNull(clonedDataSourceFactory.getConnectionExtensionPoint());
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
    }

    @Test
    public void testResetUsername() {
        ValueMapper<String> valueMapper = (from, to) -> to + "_modified";
        dataSourceFactory.resetUsername(valueMapper);
        assertEquals("username_modified", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ValueMapper<String> valueMapper = (from, to) -> to + "_modified";
        dataSourceFactory.resetPassword(valueMapper);
        assertEquals("password_modified", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        ValueMapper<String> valueMapper = (from, to) -> to + "_modified";
        dataSourceFactory.resetHost(valueMapper);
        assertEquals("localhost_modified", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        ValueMapper<Integer> valueMapper = (from, to) -> to + 1;
        dataSourceFactory.resetPort(valueMapper);
        assertEquals(1235, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ValueMapper<String> valueMapper = (from, to) -> to + "_modified";
        dataSourceFactory.resetSchema(valueMapper);
        assertEquals("schema_modified", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ValueMapper<Map<String, String>> valueMapper = (from, to) -> {
            Map<String, String> newMap = new HashMap<>(from);
            newMap.put("new_key", "new_value");
            return newMap;
        };
        dataSourceFactory.resetParameters(valueMapper);
        assertEquals("new_value", dataSourceFactory.getParameters().get("key"));
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals(dataSourceFactory.getConnectionExtensionPoint().generateJdbcUrl(
                dataSourceFactory.getHost(), dataSourceFactory.getPort(),
                dataSourceFactory.getDefaultSchema(), dataSourceFactory.getParameters()),
                dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(getUsername(dataSourceFactory.getConnectionConfig()), dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(dataSourceFactory.getConnectionConfig().getPassword(), dataSourceFactory.getPassword());
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcUrlParams = dataSourceFactory.getJdbcParams(dataSourceFactory.getConnectionConfig());
        assertTrue(jdbcUrlParams.containsKey("maxAllowedPacket"));
        assertTrue(jdbcUrlParams.containsKey("zeroDateTimeBehavior"));
        assertTrue(jdbcUrlParams.containsKey("useSSL"));
        assertTrue(jdbcUrlParams.containsKey("trustStore"));
        assertTrue(jdbcUrlParams.containsKey("keyStore"));
        assertFalse(jdbcUrlParams.containsKey("password"));
    }

    @Test
    public void testInitializeDataSource() throws Exception {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        try {
            assertEquals(dataSourceFactory.getUsername(), connection.createStatement().getConnection().getUserInfo().getUserName());
            assertEquals(dataSourceFactory.getHost(), connection.createStatement().getConnection().getHost());
            assertEquals(dataSourceFactory.getPort(), connection.createStatement().getConnection().getPort());
        } finally {
            connection.close();
        }

        List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
        if (!initializers.isEmpty()) {
            for (ConnectionInitializer initializer : initializers) {
                initializer.initializeDatabase(dataSource);
            }
        }
        dataSource.getConnection().createStatement().execute("DROP TABLE IF EXISTS test_table");
        dataSource.getConnection().createStatement().execute("CREATE TABLE test_table (id INT)");
        dataSource.getConnection().createStatement().execute("INSERT INTO test_table VALUES (1)");
        Connection connection2 = dataSource.getConnection();
        try {
            assertEquals(1, connection2.createStatement().executeQuery("SELECT COUNT(*) FROM test_table").getInt(1));
        } finally {
            connection2.close();
        }
    }

    @After
    public void tearDown() {
        dataSourceFactory.getConnection().createStatement().execute("DROP TABLE IF EXISTS test_table");
    }
}