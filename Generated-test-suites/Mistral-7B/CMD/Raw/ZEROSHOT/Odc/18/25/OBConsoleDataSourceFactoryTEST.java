package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
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

    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
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
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() throws SQLException {
        OBConsoleDataSourceFactory original = obConsoleDataSourceFactory;
        OBConsoleDataSourceFactory clone = original.deepCopy();
        assertNotSame(original, clone);
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertEquals(original.getDialectType(), clone.getDialectType());
        assertEquals(original.getAutoCommit(), clone.getAutoCommit());
        assertEquals(original.initConnection, clone.initConnection);
        assertEquals(original.eventPublisher, clone.eventPublisher);
        assertEquals(original.connectionExtensionPoint, clone.connectionExtensionPoint);
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "oldUsername";
        String newUsername = "newUsername";
        obConsoleDataSourceFactory.resetUsername(mapper -> newUsername);
        assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        obConsoleDataSourceFactory.resetPassword(mapper -> newPassword);
        assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = "oldHost";
        String newHost = "newHost";
        obConsoleDataSourceFactory.resetHost(mapper -> newHost);
        assertEquals(newHost, obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer oldPort = 1234;
        Integer newPort = 5678;
        obConsoleDataSourceFactory.resetPort(mapper -> newPort);
        assertEquals(newPort, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "oldSchema";
        String newSchema = "newSchema";
        obConsoleDataSourceFactory.resetSchema(mapper -> newSchema);
        assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParameters = new HashMap<>();
        Map<String, String> newParameters = new HashMap<>();
        obConsoleDataSourceFactory.resetParameters(mapper -> newParameters);
        assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
    }

    @Test
    public void testConstructorWithConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(connectionConfig.getUsername(), obConsoleDataSourceFactory.getUsername());
        assertEquals(connectionConfig.getPassword(), obConsoleDataSourceFactory.getPassword());
        assertEquals(connectionConfig.getHost(), obConsoleDataSourceFactory.getHost());
        assertEquals(connectionConfig.getPort(), obConsoleDataSourceFactory.getPort());
        assertEquals(connectionConfig.getDefaultSchema(), obConsoleDataSourceFactory.getDefaultSchema());
        assertEquals(connectionConfig.getParameters(), obConsoleDataSourceFactory.getParameters());
        assertEquals(connectionConfig.getDialectType(), obConsoleDataSourceFactory.getDialectType());
        assertEquals(connectionConfig.getType(), obConsoleDataSourceFactory.getConnectType());
    }

    @Test
    public void testConstructorWithConnectionConfigBoolean() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertEquals(connectionConfig.getUsername(), obConsoleDataSourceFactory.getUsername());
        assertEquals(connectionConfig.getPassword(), obConsoleDataSourceFactory.getPassword());
        assertEquals(connectionConfig.getHost(), obConsoleDataSourceFactory.getHost());
        assertEquals(connectionConfig.getPort(), obConsoleDataSourceFactory.getPort());
        assertEquals(connectionConfig.getDefaultSchema(), obConsoleDataSourceFactory.getDefaultSchema());
        assertEquals(connectionConfig.getParameters(), obConsoleDataSourceFactory.getParameters());
        assertEquals(connectionConfig.getDialectType(), obConsoleDataSourceFactory.getDialectType());
        assertEquals(connectionConfig.getType(), obConsoleDataSourceFactory.getConnectType());
        assertTrue(obConsoleDataSourceFactory.initConnection);
    }

    @Test
    public void testConstructorWithConnectionConfigBooleanBoolean() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, null);
        assertEquals(connectionConfig.getUsername(), obConsoleDataSourceFactory.getUsername());
        assertEquals(connectionConfig.getPassword(), obConsoleDataSourceFactory.getPassword());
        assertEquals(connectionConfig.getHost(), obConsoleDataSourceFactory.getHost());
        assertEquals(connectionConfig.getPort(), obConsoleDataSourceFactory.getPort());
        assertEquals(connectionConfig.getDefaultSchema(), obConsoleDataSourceFactory.getDefaultSchema());
        assertEquals(connectionConfig.getParameters(), obConsoleDataSourceFactory.getParameters());
        assertEquals(connectionConfig.getDialectType(), obConsoleDataSourceFactory.getDialectType());
        assertEquals(connectionConfig.getType(), obConsoleDataSourceFactory.getConnectType());
        assertTrue(obConsoleDataSourceFactory.initConnection);
        assertEquals(eventPublisher, obConsoleDataSourceFactory.eventPublisher);
        assertEquals(connectionExtensionPoint, obConsoleDataSourceFactory.connectionExtensionPoint);
    }

    @After
    public void tearDown() {
        obConsoleDataSourceFactory = null;
        connectionExtensionPoint = null;
        eventPublisher = null;
        connectionConfig = null;
    }
}