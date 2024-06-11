package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory.ValueMapper;
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
 *
 * @author yh263208
 * @date 2023-03-28 13:00
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private PoolingDataSource<SingleConnectionDataSource> dataSource;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setSslConfig(new SSLConfig(true));
        connectionConfig.setSslFileEntry(new SSLFileEntry("cert.pem", "key.pem"));

        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = dataSourceFactory.getDataSource();
        eventPublisher = new EventPublisher();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertNotNull(password);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeepCopyNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        dataSourceFactory.deepCopy(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeepCopyNullValueMapper() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.deepCopy(null);
    }

    @Test
    public void testResetUsername() {
        String username = "test_username";
        ValueMapper<String> mapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return username;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetUsername(mapper);
        assertEquals(username, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String password = "test_password";
        ValueMapper<String> mapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return password;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetPassword(mapper);
        assertEquals(password, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String host = "test_host";
        ValueMapper<String> mapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return host;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetHost(mapper);
        assertEquals(host, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int port = 1234;
        ValueMapper<Integer> mapper = new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return port;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetPort(mapper);
        assertEquals(port, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String schema = "test_schema";
        ValueMapper<String> mapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return schema;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetSchema(mapper);
        assertEquals(schema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", "value");
        ValueMapper<Map<String, String>> mapper = new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                return parameters;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.resetParameters(mapper);
        assertEquals(parameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcUrlParams = dataSourceFactory.getJdbcParams();
        assertNotNull(jdbcUrlParams);
        assertEquals("true", jdbcUrlParams.get("allowMultiQueries"));
        assertEquals("5000", jdbcUrlParams.get("connectTimeout"));
        assertEquals("64000000", jdbcUrlParams.get("maxAllowedPacket"));
        assertNotNull(jdbcUrlParams.get("useSSL"));
        assertTrue(jdbcUrlParams.containsKey("noDatetimeStringSync"));
    }

    @Test
    public void testGenerateJdbcUrl() {
        ConnectionExtensionPoint connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        String jdbcUrl = connectionExtensionPoint.generateJdbcUrl("127.0.0.1", 3306, "test_schema", new HashMap<>());
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsernameWithOracleDialect() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("test_schema");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = dataSourceFactory.getUsername();
        assertEquals("\"test_user\"", username);
    }

    @Test
    public void testGetPasswordWithEmptyObTenantName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setObTenantName("");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = dataSourceFactory.getPassword();
        assertEquals("test_password", password);
    }

    @Test
    public void testGetPasswordWithTenantName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setTenantName("test_tenant");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = dataSourceFactory.getPassword();
        assertEquals("test_user@test_tenant#test_cluster:test_password", password);
    }

    @Test
    public void testGetPasswordWithClusterName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setClusterName("test_cluster");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = dataSourceFactory.getPassword();
        assertEquals("test_user@test_tenant#test_cluster:test_password", password);
    }

    @Test
    public void testGetPasswordWithDefaultDBUser() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setObTenantName("test_tenant");
        connectionConfig.setTenantName("test_tenant");
        connectionConfig.setClusterName("test_cluster");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = dataSourceFactory.getPassword();
        assertEquals("test_user", password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJdbcUrlWithNullConnectionExtensionPoint() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.getJdbcUrl();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullEventPublisher() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.setEventPublisher(null);
        dataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDriverClassName() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataSourceFactory.setDriverClassName(null);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDataSourceWithAutoCommit() throws SQLException {
        boolean autoCommit = true;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), autoCommit);
        PoolingDataSource<SingleConnectionDataSource> dataSource = dataSourceFactory.getDataSource();
        SingleConnectionDataSource connection = dataSource.getConnection();
        connection.setAutoCommit(autoCommit);
        assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    public void testGetDataSourceWithoutAutoCommit() throws SQLException {
        boolean autoCommit = false;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), autoCommit);
        PoolingDataSource<SingleConnectionDataSource> dataSource = dataSourceFactory.getDataSource();
        SingleConnectionDataSource connection = dataSource.getConnection();
        assertFalse(connection.isAutoCommit());
        connection.close();
    }

    @Test
    public void testDeepCopy() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setSslConfig(new SSLConfig(true));
        connectionConfig.setSslFileEntry(new SSLFileEntry("cert.pem", "key.pem"));

        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = originalDataSourceFactory.deepCopy();

        Map<String, String> originalJdbcUrlParams = originalDataSourceFactory.getParameters();
        Map<String, String> clonedJdbcUrlParams = clonedDataSourceFactory.getParameters();

        assertNotSame(originalJdbcUrlParams, clonedJdbcUrlParams);
        assertEquals(originalJdbcUrlParams.size(), clonedJdbcUrlParams.size());

        for (Map.Entry<String, String> entry : originalJdbcUrlParams.entrySet()) {
            String key = entry.getKey();
            String originalValue = entry.getValue();
            String clonedValue = clonedJdbcUrlParams.get(key);
            assertEquals(originalValue, clonedValue);
        }
    }

    @After
    public void tearDown() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}