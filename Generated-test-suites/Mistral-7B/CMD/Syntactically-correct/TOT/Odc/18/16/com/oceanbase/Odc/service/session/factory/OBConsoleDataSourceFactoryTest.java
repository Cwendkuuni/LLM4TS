package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.constant.ConnectType;
import com.oceanbase.odc.common.constant.DialectType;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("test_schema");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSourceFactory != null) {
            ((SingleConnectionDataSource) dataSourceFactory.getDataSource()).close();
        }
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
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
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testCloneableDataSourceFactory() throws CloneNotSupportedException {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedDataSourceFactory);
        assertNotNull(clonedDataSourceFactory.getDataSource());
    }

    @Test
    public void testResetUsername() {
        String oldUsername = dataSourceFactory.getUsername();
        String newUsername = "test_username";
        dataSourceFactory.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newUsername;
            }
        });
        assertNotNull(dataSourceFactory.getUsername());
        assertNotEquals(oldUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = dataSourceFactory.getPassword();
        String newPassword = "test_password";
        dataSourceFactory.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newPassword;
            }
        });
        assertNotNull(dataSourceFactory.getPassword());
        assertNotEquals(oldPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = dataSourceFactory.getHost();
        String newHost = "test_host";
        dataSourceFactory.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newHost;
            }
        });
        assertNotNull(dataSourceFactory.getHost());
        assertNotEquals(oldHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int oldPort = dataSourceFactory.getPort();
        int newPort = 1234;
        dataSourceFactory.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return newPort;
            }
        });
        assertNotNull(dataSourceFactory.getPort());
        assertNotEquals(oldPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = dataSourceFactory.getDefaultSchema();
        String newSchema = "test_schema";
        dataSourceFactory.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newSchema;
            }
        });
        assertNotNull(dataSourceFactory.getDefaultSchema());
        assertNotEquals(oldSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParameters = dataSourceFactory.getParameters();
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("test_key", "test_value");
        dataSourceFactory.resetParameters(new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                return newParameters;
            }
        });
        assertNotNull(dataSourceFactory.getParameters());
        assertNotEquals(oldParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testEmptyConnectionConfig() {
        ConnectionConfig emptyConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory emptyDataSourceFactory = new OBConsoleDataSourceFactory(emptyConfig, true);
        assertNull(emptyDataSourceFactory.getDataSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConnectionConfig() {
        OBConsoleDataSourceFactory nullDataSourceFactory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test
    public void testConstructWithoutConnectionConfig() {
        OBConsoleDataSourceFactory nullDataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        assertNull(nullDataSourceFactory.getDataSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDialectType() {
        ConnectionConfig invalidConfig = new ConnectionConfig();
        invalidConfig.setDialectType("invalid_type");
        OBConsoleDataSourceFactory invalidDataSourceFactory = new OBConsoleDataSourceFactory(invalidConfig, true);
    }

    @Test
    public void testConnectionInitializers() throws SQLException {
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void initializeDatabase(DataSource dataSource) throws SQLException {
                BasicDataSource basicDataSource = (BasicDataSource) dataSource;
                basicDataSource.addConnectionTestQuery("SELECT 1");
            }
        };
        ConnectionExtensionPoint connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
        initializers.add(initializer);
        connectionConfig.setConnectionInitializers(initializers);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testSessionCreatedInitializer() throws SQLException {
        ConnectionConfig sessionCreatedConfig = new ConnectionConfig();
        ConnectionInitializer sessionCreatedInitializer = new SessionCreatedInitializer(sessionCreatedConfig);
        List<ConnectionInitializer> initializers = new ArrayList<>();
        initializers.add(sessionCreatedInitializer);
        connectionConfig.setConnectionInitializers(initializers);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testSslConfig() {
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testNoSslConfig() {
        connectionConfig.setSslConfig(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(dataSourceFactory.getDataSource());
    }

}