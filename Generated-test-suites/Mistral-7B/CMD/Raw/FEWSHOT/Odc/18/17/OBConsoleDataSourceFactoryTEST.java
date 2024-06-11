package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.event.EventPublisher.emptyPublisher;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
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
 * Test class for {@link OBConsoleDataSourceFactory}
 *
 * @author yh263208
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionMapper connectionMapper;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        connectionMapper = ConnectionMapper.INSTANCE;
        eventPublisher = mock(EventPublisher.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenAnswer(invocation -> "jdbc:mock:url");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mock.driver.MockDriver");
        DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
        when(dataSourceFactory.getDataSource()).thenAnswer(invocation -> new BasicDataSource());
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_MYSQL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        this.dataSourceFactory = dataSourceFactory;
    }

    @After
    public void teardown() {
        this.dataSourceFactory = null;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof DataSource);
        assertEquals(this.dataSourceFactory.getUsername(), dataSource.getUsername());
        assertEquals(this.dataSourceFactory.getPassword(), dataSource.getPassword());
        assertEquals(this.dataSourceFactory.getDialectType(), dataSource.getConnection().getMetaData().getDatabaseProductName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true, true);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        new OBConsoleDataSourceFactory(connectionConfig, true, true, null);
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("jdbc:mock:url", this.dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("foo", this.dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("bar", this.dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, this.dataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        this.dataSourceFactory.resetUsername(connectionMapper);
        assertEquals("foo_new", this.dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        this.dataSourceFactory.resetPassword(connectionMapper);
        assertEquals("bar_new", this.dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        this.dataSourceFactory.resetHost(connectionMapper);
        assertEquals("new_host", this.dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        this.dataSourceFactory.resetPort(connectionMapper);
        assertEquals(123, this.dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        this.dataSourceFactory.resetSchema(connectionMapper);
        assertEquals("new_schema", this.dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        this.dataSourceFactory.resetParameters(connectionMapper);
        assertEquals(newParameters, this.dataSourceFactory.getParameters());
    }

    @Test
    public void testCloneableDataSourceFactory() throws SQLException {
        OBConsoleDataSourceFactory original = this.dataSourceFactory;
        OBConsoleDataSourceFactory cloned = original.deepCopy();

        ArgumentCaptor<ConnectionConfig> connectionConfigArgumentCaptor = ArgumentCaptor.forClass(ConnectionConfig.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());

        DataSource originalDataSource = original.getDataSource();
        DataSource clonedDataSource = cloned.getDataSource();

        assertEquals(originalDataSource, clonedDataSource);

        Connection originalConnection = originalDataSource.getConnection();
        Connection clonedConnection = clonedDataSource.getConnection();

        assertEquals(originalConnection, clonedConnection);

        when(connectionExtensionPoint.getConnectionInitializers()).thenAnswer(invocation -> {
            ConnectionInitializer initializer = invocation.getArgument(0, ConnectionInitializer.class);
            return new ArrayList<ConnectionInitializer>(){{add(initializer);}};
        });

        cloned.initDataSource();

        verify(connectionExtensionPoint, Mockito.times(1)).addConnectionInitializer(clonedDataSource);

        // Check if the cloned object has the same properties as the original object
        Map<String, Object> originalProperties = originalDataSource.getConnectionProperties();
        Map<String, Object> clonedProperties = clonedDataSource.getConnectionProperties();
        assertEquals(originalProperties, clonedProperties);

        // Check if the cloned object has the same username and password as the original object
        assertEquals(original.getUsername(), cloned.getUsername());
        assertEquals(original.getPassword(), cloned.getPassword());

        // Check if the cloned object has the same dialect type as the original object
        assertEquals(original.getDialectType(), cloned.getDialectType());

        // Check if the cloned object has the same JDBC URL as the original object
        assertEquals(original.getJdbcUrl(), cloned.getJdbcUrl());

        // Check if the cloned object has the same connection config as the original object
        ConnectionConfig originalConnectionConfig = original.getConnectionConfig();
        ConnectionConfig clonedConnectionConfig = cloned.getConnectionConfig();
        assertEquals(originalConnectionConfig, clonedConnectionConfig);
    }

    @Test
    public void testInitDataSource() throws SQLException {
        this.dataSourceFactory.initDataSource();

        List<ConnectionInitializer> initializers = this.connectionExtensionPoint.getConnectionInitializers();
        assertTrue(initializers.size() > 0);
        initializers.forEach(initializer -> {
            initializer.initializeDataSource(this.dataSourceFactory.getDataSource());
        });

        Connection connection = this.dataSourceFactory.getDataSource().getConnection();
        connection.createStatement().execute("create table test_table (id int primary key, name varchar(10))");
        connection.close();
    }

    @Test
    public void testInitDataSourceWithEventPublisher() throws SQLException {
        this.dataSourceFactory = new OBConsoleDataSourceFactory(this.connectionConfig, true, true, this.eventPublisher);
        this.dataSourceFactory.initDataSource();

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(this.eventPublisher, Mockito.times(1)).publishEvent("DataSourceInitializedEvent", connectionArgumentCaptor.capture());
        Connection connection = connectionArgumentCaptor.getValue();
        connection.createStatement().execute("create table test_table (id int primary key, name varchar(10))");
        connection.close();
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = this.dataSourceFactory.getJdbcParams(this.connectionConfig);
        assertEquals(22, params.size());
        assertTrue(params.containsKey("maxAllowedPacket")); // Integer
        assertTrue(params.containsKey("allowMultiQueries")); // Boolean
        assertTrue(params.containsKey("connectTimeout")); // Integer
        assertTrue(params.containsKey("zeroDateTimeBehavior")); // String
        assertTrue(params.containsKey("noDatetimeStringSync")); // Boolean
        assertTrue(params.containsKey("jdbcCompliantTruncation")); // Boolean
        assertTrue(params.containsKey("sendConnectionAttributes")); // Boolean
        assertTrue(params.containsKey("socksProxyHost")); // String
        assertTrue(params.containsKey("socksProxyPort")); // String
        assertTrue(params.containsKey("useSSL")); // Boolean
        assertTrue(params.containsKey("trustServerCertificate")); // Boolean
        assertTrue(params.containsKey("trustStore")); // String
        assertTrue(params.containsKey("trustStorePassword")); // String
        assertTrue(params.containsKey("keyStore")); // String
        assertTrue(params.containsKey("keyStorePassword")); // String
        assertFalse(params.containsKey("password")); // String
        assertTrue(params.containsKey("allowLoadLocalInfile")); // Boolean
        assertTrue(params.containsKey("allowUrlInLocalInfile")); // Boolean
        assertTrue(params.containsKey("allowLoadLocalInfileInPath")); // String
        assertTrue(params.containsKey("autoDeserialize")); // Boolean
    }

    @Test
    public void testGetJdbcParamsWithSslConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_MYSQL);
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePassword("password");
        sslFileEntry.setKeyStoreFilePath("path/to/keystore.p12");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);

        Map<String, String> params = this.dataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals(25, params.size());
        assertTrue(params.containsKey("maxAllowedPacket")); // Integer
        assertTrue(params.containsKey("allowMultiQueries")); // Boolean
        assertTrue(params.containsKey("connectTimeout")); // Integer
        assertTrue(params.containsKey("zeroDateTimeBehavior")); // String
        assertTrue(params.containsKey("noDatetimeStringSync")); // Boolean
        assertTrue(params.containsKey("jdbcCompliantTruncation")); // Boolean
        assertTrue(params.containsKey("sendConnectionAttributes")); // Boolean
        assertTrue(params.containsKey("socksProxyHost")); // String
        assertTrue(params.containsKey("socksProxyPort")); // String
        assertTrue(params.containsKey("useSSL")); // Boolean
        assertTrue(params.containsKey("trustServerCertificate")); // Boolean
        assertTrue(params.containsKey("trustStore")); // String
        assertTrue(params.containsKey("trustStorePassword")); // String
        assertTrue(params.containsKey("keyStore")); // String
        assertTrue(params.containsKey("keyStorePassword")); // String
        assertTrue(params.containsKey("allowLoadLocalInfile")); // Boolean
        assertTrue(params.containsKey("allowUrlInLocalInfile")); // Boolean
        assertTrue(params.containsKey("allowLoadLocalInfileInPath")); // String
        assertTrue(params.containsKey("autoDeserialize")); // Boolean
        assertTrue(params.containsKey("disableSslHostnameVerification")); // Boolean

        String expectedTrustStore = "path/to/keystore.p12";
        String expectedTrustStorePassword = "password";
        String expectedKeyStore = "path/to/keystore.p12";
        String expectedKeyStorePassword = "password";

        assertEquals(expectedTrustStore, params.get("trustStore"));
        assertEquals(expectedTrustStorePassword, params.get("trustStorePassword"));
        assertEquals(expectedKeyStore, params.get("keyStore"));
        assertEquals(expectedKeyStorePassword, params.get("keyStorePassword"));
    }
}