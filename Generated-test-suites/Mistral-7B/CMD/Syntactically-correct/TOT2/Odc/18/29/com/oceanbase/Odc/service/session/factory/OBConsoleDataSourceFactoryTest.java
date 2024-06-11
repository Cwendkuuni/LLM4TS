package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("test");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(false, null, null));
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        this.dataSourceFactory = (OBConsoleDataSourceFactory) dataSourceFactory;
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = this.dataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testCloneableDataSourceFactory() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = this.dataSourceFactory.deepCopy();
        assertEquals(this.dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(this.dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(this.dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(this.dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(this.dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(this.dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(this.dataSourceFactory.getConnectionConfig().getHost(), clonedDataSourceFactory.getConnectionConfig().getHost());
        assertEquals(this.dataSourceFactory.getConnectionConfig().getPort(), clonedDataSourceFactory.getConnectionConfig().getPort());
        assertEquals(this.dataSourceFactory.getConnectionConfig().getUsername(), clonedDataSourceFactory.getConnectionConfig().getUsername());
        assertEquals(this.dataSourceFactory.getConnectType(), clonedDataSourceFactory.getConnectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newusername";
        this.dataSourceFactory.resetUsername(value -> newUsername);
        assertEquals(newUsername, this.dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newpassword";
        this.dataSourceFactory.resetPassword(value -> newPassword);
        assertEquals(newPassword, this.dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newhost";
        this.dataSourceFactory.resetHost(value -> newHost);
        assertEquals(newHost, this.dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        this.dataSourceFactory.resetPort(value -> newPort);
        assertEquals(newPort, this.dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newschema";
        this.dataSourceFactory.resetSchema(value -> newSchema);
        assertEquals(newSchema, this.dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("testparam", "testvalue");
        this.dataSourceFactory.resetParameters(value -> newParameters);
        assertEquals(newParameters, this.dataSourceFactory.getParameters());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        Properties jdbcUrlParams = new Properties();
        jdbcUrlParams.setProperty("testparam", "testvalue");
        this.dataSourceFactory.getConnectionConfig().setJdbcUrlParameters(jdbcUrlParams);
        String newJdbcUrl = this.dataSourceFactory.getJdbcUrl();
        assertEquals(newJdbcUrl, jdbcUrl + "&testparam=testvalue");
    }

    @Test
    public void testGetConnectionConfig() {
        ConnectionConfig connectionConfig = this.dataSourceFactory.getConnectionConfig();
        assertEquals(this.dataSourceFactory.getHost(), connectionConfig.getHost());
        assertEquals(this.dataSourceFactory.getPort(), connectionConfig.getPort());
        assertEquals(this.dataSourceFactory.getUsername(), connectionConfig.getUsername());
        assertEquals(this.dataSourceFactory.getDefaultSchema(), connectionConfig.getDefaultSchema());
        assertEquals(this.dataSourceFactory.getDialectType(), connectionConfig.getDialectType());
        assertEquals(this.dataSourceFactory.getConnectType(), connectionConfig.getType());
        assertEquals(this.dataSourceFactory.getSslConfig(), connectionConfig.getSslConfig());
    }

    @Test
    public void testGetConnectionInitializers() {
        List<ConnectionInitializer> connectionInitializers = this.dataSourceFactory.getConnectionInitializers();
        assertNotNull(connectionInitializers);
        connectionInitializers.forEach(initializer -> {
            assertNotNull(initializer);
        });
    }

    @Test
    public void testGetConnectionExtensionPoint() {
        ConnectionExtensionPoint connectionExtensionPoint = this.dataSourceFactory.getConnectionExtensionPoint();
        assertNotNull(connectionExtensionPoint);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, false);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetDataSourceWithInvalidPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort("-1");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(-1);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        ConnectionPluginUtil connectionPluginUtil = new ConnectionPluginUtil() {
            @Override
            public ConnectionExtensionPoint getConnectionExtension(DialectType dialectType) {
                return null;
            }
        };
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithEmptyConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
        assertNull(dataSourceFactory.getDataSource());
    }

}