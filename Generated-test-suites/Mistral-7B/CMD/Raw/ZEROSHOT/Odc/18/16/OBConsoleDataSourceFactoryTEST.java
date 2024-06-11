package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

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

import com.oceanbase.odc.core.datasource.DataSourceFactory;
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

    private DataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6030);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("TEST_SCHEMA");
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(connectionConfig.getDialectType());
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
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(StringUtils.isNotBlank(jdbcUrl));
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() throws CloneNotSupportedException {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(dataSourceFactory.getConnectType(), clonedDataSourceFactory.getConnectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        dataSourceFactory.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newUsername;
            }
        });
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newPassword;
            }
        });
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newHost;
            }
        });
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        dataSourceFactory.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return newPort;
            }
        });
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newSchema;
            }
        });
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters(new ValueMapper<Map<String,String>>() {
            @Override
            public Map<String,String> map(Map<String,String> value) {
                return newParameters;
            }
        });
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }
}

class ValueMapper<T> {
    public abstract T map(T value);
}