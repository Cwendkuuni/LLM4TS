package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isNotBlank;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.ConnectionMapper;
import com.oceanbase.odc.common.util.ConnectionSessionUtil;
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
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        eventPublisher = new EventPublisher();
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.LONG_CONN);
    }

    @Test
    public void testGetDataSource() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals(connectionConfig.getDialectType(), dataSourceFactory.getDialectType());
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(connectionConfig.getHost(),
                connectionConfig.getPort(), connectionConfig.getDefaultSchema(), dataSourceFactory.getParameters());
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = dataSourceFactory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = dataSourceFactory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetDefaultSchema() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertEquals("", defaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = new HashMap<>();
        connectionConfig.setJdbcUrlParameters(parameters);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> dataSourceParameters = dataSourceFactory.getParameters();
        assertEquals(parameters, dataSourceParameters);
    }

    @Test
    public void testGetConnectionExtensionPoint() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        ConnectionExtensionPoint connectionExtensionPoint = dataSourceFactory.getConnectionExtensionPoint();
        assertSame(this.connectionExtensionPoint, connectionExtensionPoint);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newTestUser";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetUsername(ConnectionMapper.INSTANCE::map);
        assertEquals("newTestUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newTestPassword";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetPassword(ConnectionMapper.INSTANCE::map);
        assertEquals("newTestPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetHost(ConnectionMapper.INSTANCE::map);
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetPort(ConnectionMapper.INSTANCE::map);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetSchema(ConnectionMapper.INSTANCE::map);
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.resetParameters(ConnectionMapper.INSTANCE::map);
        Map<String, String> dataSourceParameters = dataSourceFactory.getParameters();
        assertEquals(newParameters, dataSourceParameters);
    }

    @Test
    public void testCreateCloneableDataSourceFactory() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clone = original.deepCopy();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
    }

    @Test
    public void testGetAutoCommit() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        assertFalse(dataSourceFactory.isAutoCommit());

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertTrue(dataSourceFactory.isAutoCommit());
    }

    @Test
    public void testSetEventPublisher() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        EventPublisher oldEventPublisher = dataSourceFactory.getEventPublisher();
        EventPublisher newEventPublisher = new EventPublisher();
        dataSourceFactory.setEventPublisher(newEventPublisher);
        assertNull(oldEventPublisher);
        assertEquals(newEventPublisher, dataSourceFactory.getEventPublisher());
    }

    @Test
    public void testGetInitConnection() {
        DataSourceFactory dataSourceFactory1 = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertTrue(dataSourceFactory1.isInitConnection());

        DataSourceFactory dataSourceFactory2 = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        assertFalse(dataSourceFactory2.isInitConnection());
    }

    @Test
    public void testCloneableDataSourceFactory() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        OBConsoleDataSourceFactory clone = original.deepCopy();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertFalse(clone.isInitConnection());
    }

    @Test
    public void testGetAutoCommitWithInitConnection() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertTrue(dataSourceFactory.isInitConnection());
        assertTrue(dataSourceFactory.isAutoCommit());
    }

    @Test
    public void testGetAutoCommitWithoutInitConnection() {
        DataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        assertFalse(dataSourceFactory.isInitConnection());
        assertNull(dataSourceFactory.getAutoCommit());
    }

    @Test
    public void testDeepCopyWithInitConnection() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        OBConsoleDataSourceFactory clone = original.deepCopy();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertTrue(clone.isInitConnection());
    }

    @Test
    public void testDeepCopyWithoutInitConnection() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        OBConsoleDataSourceFactory clone = original.deepCopy();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertFalse(clone.isInitConnection());
    }

    @Test
    public void testDeepCopyWithSslConfig() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("testKeyStoreFile.p12");
        sslFileEntry.setKeyStoreFilePassword("testPassword");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clone = original.deepCopy();

        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertEquals(original.getConnectionExtensionPoint(), clone.getConnectionExtensionPoint());
        assertEquals(original.getEventPublisher(), clone.getEventPublisher());
        assertEquals(original.isInitConnection(), clone.isInitConnection());
        assertEquals(original.isAutoCommit(), clone.isAutoCommit());
        assertEquals(sslConfig, clone.getConnectionConfig().getSslConfig());
        assertEquals(sslFileEntry, clone.getConnectionConfig().getSslFileEntry());
    }

    @Test
    public void testDeepCopyWithoutSslConfig() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clone = original.deepCopy();

        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertEquals(original.getConnectionExtensionPoint(), clone.getConnectionExtensionPoint());
        assertEquals(original.getEventPublisher(), clone.getEventPublisher());
        assertEquals(original.isInitConnection(), clone.isInitConnection());
        assertEquals(original.isAutoCommit(), clone.isAutoCommit());
        assertNull(clone.getConnectionConfig().getSslConfig());
        assertNull(clone.getConnectionConfig().getSslFileEntry());
    }

    @Test
    public void testGetDialectType() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(connectionConfig.getDialectType(), dataSourceFactory.getDialectType());
    }
}