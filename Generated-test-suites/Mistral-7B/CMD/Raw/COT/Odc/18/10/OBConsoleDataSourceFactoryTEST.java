package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionMapper connectionMapper;

    @Before
    public void setup() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionMapper = ConnectionMapper.INSTANCE;
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setSslConfig(new SSLConfig(true, new SSLFileEntry()));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("jdbc:obnew:obclient:"));
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertSame(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        assertNotNull(deepCopy);
        assertFalse(deepCopy.equals(dataSourceFactory));
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        dataSourceFactory.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newUsername;
            }
        });
        assertSame(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        dataSourceFactory.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newPassword;
            }
        });
        assertSame(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        dataSourceFactory.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newHost;
            }
        });
        assertSame(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        dataSourceFactory.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer oldValue) {
                return newPort;
            }
        });
        assertTrue(dataSourceFactory.getPort() == newPort);
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        dataSourceFactory.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newSchema;
            }
        });
        assertSame(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters(new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> oldValue) {
                return newParameters;
            }
        });
        assertSame(newParameters, dataSourceFactory.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidConnectionConfig() {
        ConnectionConfig invalidConfig = new ConnectionConfig();
        new OBConsoleDataSourceFactory(invalidConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        new OBConsoleDataSourceFactory(connectionConfig, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyUsername() {
        connectionConfig.setUsername("");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
        } catch (Exception e) {
            throw new AssertionError("Expected IllegalArgumentException, but got: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyPassword() {
        connectionConfig.setPassword("");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
        } catch (Exception e) {
            throw new AssertionError("Expected IllegalArgumentException, but got: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        try {
            dataSourceFactory.getDataSource();
        } catch (Exception e) {
            throw new AssertionError("Expected IllegalArgumentException, but got: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testuser@test_tenant#test_cluster";
        connectionConfig.setTenantName("test_tenant");
        connectionConfig.setClusterName("test_cluster");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualUsername = dataSourceFactory.getUsername();
        assertSame(expectedUsername, actualUsername);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUsernameWithNullConnectionConfig() {
        String expectedUsername = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        String actualUsername = dataSourceFactory.getUsername();
        assertNull(actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testpassword";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualPassword = dataSourceFactory.getPassword();
        assertSame(expectedPassword, actualPassword);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPasswordWithNullDataSourceFactory() {
        String expectedPassword = null;
        String actualPassword = new OBConsoleDataSourceFactory(connectionConfig, true).getPassword();
        assertNull(actualPassword);
    }

    @Test
    public void testGetDefaultSchema() {
        String expectedDefaultSchema = "test_schema";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualDefaultSchema = dataSourceFactory.getDefaultSchema();
        assertSame(expectedDefaultSchema, actualDefaultSchema);
    }

    @Test
    public void testGetDefaultSchemaWithNoSchema() {
        connectionConfig.setDefaultSchema(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualDefaultSchema = dataSourceFactory.getDefaultSchema();
        assertNull(actualDefaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("testKey", "testValue");
        connectionConfig.setJdbcUrlParameters(expectedParameters);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> actualParameters = dataSourceFactory.getParameters();
        assertSame(expectedParameters, actualParameters);
    }

    @Test
    public void testGetParametersWithEmptyJdbcUrlParameters() {
        connectionConfig.setJdbcUrlParameters(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> actualParameters = dataSourceFactory.getParameters();
        assertNull(actualParameters);
    }

    @Test
    public void testGenerateJdbcUrlWithValidConfig() throws Exception {
        String expectedJdbcUrl = "jdbc:obnew:obclient:localhost:3306/testuser@test_tenant#test_cluster:testpassword@test_schema";
        connectionConfig.setTenantName("test_tenant");
        connectionConfig.setClusterName("test_cluster");
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.eq("localhost"), ArgumentMatchers.eq(3306),
                ArgumentMatchers.eq("testschema"), ArgumentMatchers.eq(Map.of("testKey", "testValue"))))
                .thenReturn(expectedJdbcUrl);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint);
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertSame(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGenerateJdbcUrlWithInvalidConfig() throws Exception {
        String expectedJdbcUrl = null;
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.any(Map.class)))
                .thenThrow(new SQLException("Test exception"));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint);
        try {
            dataSourceFactory.getJdbcUrl();
        } catch (Exception e) {
            expectedJdbcUrl = e.getMessage();
        }
        assertNull(expectedJdbcUrl);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidJdbcUrl() throws Exception {
        String jdbcUrl = "jdbc:obnew:obclient:localhost:3306/testuser@test_tenant#test_cluster:testpassword:test_schema";
        Properties properties = new Properties();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");
        dataSource.setDriverClassName(ConnectionSessionUtil.getDriverClassNameForDialectType(connectionConfig.getDialectType()));
        dataSource.setConnectionProperties(properties);
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(1000)).thenReturn(true);
        dataSource.setConnection(connection);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setDataSource(dataSource);
        dataSourceFactory.getDataSource();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidConnection() throws Exception {
        String jdbcUrl = "jdbc:obnew:obclient:localhost:3306/testuser@test_tenant#test_cluster:testpassword:test_schema";
        Properties properties = new Properties();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(ConnectionSessionUtil.getDriverClassNameForDialectType(connectionConfig.getDialectType()));
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");
        dataSource.setConnectionProperties(properties);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setDataSource(dataSource);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testDeepCopyWithNullDataSourceFactory() {
        OBConsoleDataSourceFactory nullDataSourceFactory = null;
        OBConsoleDataSourceFactory clonedDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true)
                .deepCopy();
        assertNotNull(clonedDataSourceFactory);
        assertFalse(clonedDataSourceFactory.equals(nullDataSourceFactory));
    }

}