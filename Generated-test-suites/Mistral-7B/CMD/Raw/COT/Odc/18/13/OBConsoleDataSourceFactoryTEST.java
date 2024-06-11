package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
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

/**
 * Test class for OBConsoleDataSourceFactory
 */
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionMapper connectionMapper;

    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collection.singletonList(connectionInitializer));
        String username = "test_username";
        String password = "test_password";
        String host = "test_host";
        int port = 1234;
        String defaultSchema = "test_default_schema";
        Map<String, String> parameters = new HashMap<>();
        ConnectionConfig connectionConfigMock = new ConnectionConfig();
        connectionConfigMock.setHost(host);
        connectionConfigMock.setPort(port);
        connectionConfigMock.setUsername(username);
        connectionConfigMock.setPassword(password);
        connectionConfigMock.setDefaultSchema(defaultSchema);
        connectionConfigMock.setJdbcUrlParameters(parameters);
        connectionConfigMock.setDialectType(DialectType.OB_ORACLE);
        when(connectionConfigMock.getConnectionExtensionPoint()).thenReturn(connectionExtensionPoint);
        when(connectionConfigMock.getSslConfig()).thenReturn(new SSLConfig(true, null, null));
        when(connectionConfigMock.getSslFileEntry()).thenReturn(new SSLFileEntry("test_keyStoreFilePath", "test_keyStoreFilePassword"));
        when(connectionConfigMock.getType()).thenReturn(ConnectType.OB_ORACLE);
        when(connectionMapper.clone(connectionConfigMock)).thenReturn(connectionConfigMock);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true, true);
    }

    @Test
    public void testGetJdbcUrl_Defaults() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertTrue(StringUtils.isNotBlank(jdbcUrl));
    }

    @Test
    public void testGetDataSource_NoInitialization() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertFalse(dataSource instanceof ConnectionInitializer);
    }

    @Test
    public void testGetDataSource_WithInitialization() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true, true);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
        assertSame(connectionExtensionPoint, singleConnectionDataSource.getEventPublisher());
    }

    @Test
    public void testGetDataSource_WithConnectionInitialization() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true, true);
        when(connectionInitializer.initializeDatabase()).thenReturn(true);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
        verify(connectionInitializer).initializeDatabase();
    }

    @Test
    public void testGetDataSource_WithoutConnectionInitialization() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true, false);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertFalse(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDataSource_WithSessionCreatedInitializer() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true, true);
        when(sessionCreatedInitializer.initializeDatabase()).thenReturn(true);
        obConsoleDataSourceFactory.setSessionCreatedInitializer(sessionCreatedInitializer);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
        verify(sessionCreatedInitializer).initializeDatabase();
    }

    @Test
    public void testGetDataSource_WithAutoCommit() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource.isAutoCommit());
    }

    @Test
    public void testGetDataSource_WithoutAutoCommit() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, false, true);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertFalse(dataSource.isAutoCommit());
    }

    @Test
    public void testGetJdbcUrl_WithProxy() {
        OBTenantEndpoint endpoint = mock(OBTenantEndpoint.class);
        when(connectionConfigMock.getEndpoint()).thenReturn(endpoint);
        String proxyHost = "test_proxy_host";
        Integer proxyPort = 80;
        when(endpoint.getProxyHost()).thenReturn(proxyHost);
        when(endpoint.getProxyPort()).thenReturn(proxyPort);
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertTrue(jdbcUrl.contains("socksProxyHost=" + proxyHost));
        assertTrue(jdbcUrl.contains("socksProxyPort=" + proxyPort));
    }

    @Test
    public void testGetJdbcUrl_WithoutProxy() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertFalse(jdbcUrl.contains("socksProxyHost"));
        assertFalse(jdbcUrl.contains("socksProxyPort"));
    }

    @Test
    public void testGetDataSource_WithSsl() {
        SSLConfig sslConfig = new SSLConfig(true, null, null);
        when(connectionConfigMock.getSslConfig()).thenReturn(sslConfig);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource.getConnectionProperties().getProperty("useSSL").toString().equals("true"));
    }

    @Test
    public void testGetDataSource_WithoutSsl() {
        when(connectionConfigMock.getSslConfig()).thenReturn(null);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertFalse(dataSource.getConnectionProperties().getProperty("useSSL").toString().equals("true"));
    }

    @Test
    public void testGetDataSource_WithSslKeyStore() {
        SSLConfig sslConfig = new SSLConfig(true, null, null);
        SSLFileEntry sslFileEntry = new SSLFileEntry("test_keyStoreFilePath", "test_keyStoreFilePassword");
        when(connectionConfigMock.getSslConfig()).thenReturn(sslConfig);
        when(connectionConfigMock.getSslFileEntry()).thenReturn(sslFileEntry);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertTrue(dataSource.getConnectionProperties().getProperty("trustStore").equals(sslFileEntry.getKeyStoreFilePath()));
        assertTrue(dataSource.getConnectionProperties().getProperty("trustStorePassword").equals(sslFileEntry.getKeyStoreFilePassword()));
        assertTrue(dataSource.getConnectionProperties().getProperty("keyStore").equals(sslFileEntry.getKeyStoreFilePath()));
        assertTrue(dataSource.getConnectionProperties().getProperty("keyStorePassword").equals(sslFileEntry.getKeyStoreFilePassword()));
    }

    @Test
    public void testGetDataSource_WithDifferentDialects() {
        ConnectionConfig connectionConfigMySQL = new ConnectionConfig();
        connectionConfigMySQL.setHost("test_host_mysql");
        connectionConfigMySQL.setPort(1234);
        connectionConfigMySQL.setUsername("test_username_mysql");
        connectionConfigMySQL.setPassword("test_password_mysql");
        connectionConfigMySQL.setDefaultSchema("test_default_schema_mysql");
        connectionConfigMySQL.setDialectType(DialectType.MYSQL);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMySQL, true, true);
        DataSource dataSourceMySQL = obConsoleDataSourceFactory.getDataSource();
        assertFalse(dataSourceMySQL.getConnectionProperties().getProperty("allowLoadLocalInfile").equals("true"));
        DataSource dataSourceOracle = obConsoleDataSourceFactory.getDataSource(DialectType.OB_ORACLE);
        assertTrue(dataSourceOracle.getConnectionProperties().getProperty("allowLoadLocalInfile").equals("false"));
    }

}