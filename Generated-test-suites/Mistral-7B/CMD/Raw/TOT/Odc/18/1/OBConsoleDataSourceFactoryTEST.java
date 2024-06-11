package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.pool.PooledConnection;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(createConnectionConfig(), true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Connection connection = obConsoleDataSourceFactory.getDataSource().getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        assertNotNull(password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullAutoCommit() {
        new OBConsoleDataSourceFactory(createConnectionConfig(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullEventPublisher() {
        new OBConsoleDataSourceFactory(createConnectionConfig(), true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
        new OBConsoleDataSourceFactory(createConnectionConfig(), true, eventPublisher, null);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(createConnectionConfig(), true);
        OBConsoleDataSourceFactory copied = original.deepCopy();
        assertNotSame(original, copied);
        assertEquals(original.getUsername(), copied.getUsername());
        assertEquals(original.getPassword(), copied.getPassword());
        assertEquals(original.getHost(), copied.getHost());
        assertEquals(original.getPort(), copied.getPort());
        assertEquals(original.getDefaultSchema(), copied.getDefaultSchema());
        assertEquals(original.getParameters(), copied.getParameters());
        assertEquals(original.getJdbcUrl(), copied.getJdbcUrl());
        assertEquals(original.getDialectType(), copied.getDialectType());
        assertEquals(original.getConnectionConfig().getHost(), copied.getConnectionConfig().getHost());
        assertEquals(original.getConnectionConfig().getPort(), copied.getConnectionConfig().getPort());
        assertEquals(original.getConnectionConfig().getDefaultSchema(), copied.getConnectionConfig().getDefaultSchema());
        assertEquals(original.getConnectionConfig().getParameters(), copied.getConnectionConfig().getParameters());
        assertEquals(original.getConnectionConfig().getUsername(), copied.getConnectionConfig().getUsername());
        assertEquals(original.getConnectionConfig().getPassword(), copied.getConnectionConfig().getPassword());
        assertEquals(original.getConnectionConfig().getDialectType(), copied.getConnectionConfig().getDialectType());
    }

    @Test
    public void testResetUsername() {
        String originalUsername = obConsoleDataSourceFactory.getUsername();
        String newUsername = "new_username";
        obConsoleDataSourceFactory.resetUsername(value -> newUsername);
        assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
        assertEquals(originalUsername, obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String originalPassword = obConsoleDataSourceFactory.getPassword();
        String newPassword = "new_password";
        obConsoleDataSourceFactory.resetPassword(value -> newPassword);
        assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
        assertEquals(originalPassword, obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String originalHost = obConsoleDataSourceFactory.getHost();
        String newHost = "new_host";
        obConsoleDataSourceFactory.resetHost(value -> newHost);
        assertEquals(newHost, obConsoleDataSourceFactory.getHost());
        assertEquals(originalHost, obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int originalPort = obConsoleDataSourceFactory.getPort();
        int newPort = 1234;
        obConsoleDataSourceFactory.resetPort(value -> newPort);
        assertEquals(newPort, obConsoleDataSourceFactory.getPort());
        assertEquals(originalPort, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String originalSchema = obConsoleDataSourceFactory.getDefaultSchema();
        String newSchema = "new_schema";
        obConsoleDataSourceFactory.resetSchema(value -> newSchema);
        assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
        assertEquals(originalSchema, obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> originalParameters = obConsoleDataSourceFactory.getParameters();
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("testKey", "testValue");
        obConsoleDataSourceFactory.resetParameters(value -> newParameters);
        assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
        assertEquals(originalParameters, obConsoleDataSourceFactory.getParameters());
    }

    private ConnectionConfig createConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        return connectionConfig;
    }

    @After
    public void tearDown() throws Exception {
        Connection connection = null;
        try {
            connection = obConsoleDataSourceFactory.getDataSource().getConnection();
            connection.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        PooledConnection connection = (PooledConnection) obConsoleDataSourceFactory.getDataSource().getConnection();
        assertNotNull(connection);
        try (Connection realConnection = connection.getConnection()) {
            assertNotNull(realConnection);
        }
    }

    @Test
    public void testGetDataSourceWithInvalidParameters() {
        Map<String, String> invalidParameters = new HashMap<>();
        invalidParameters.put("testKey", "testValue");
        invalidParameters.put("testKey2", null);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        connectionConfig.setJdbcUrlParameters(invalidParameters);
        new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(-1);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        new OBConsoleDataSourceFactory(connectionConfig, true);
    }

}