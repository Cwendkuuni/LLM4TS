package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

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

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("jdbc:obc://localhost:1527/test?useSSL=false&zeroDateTimeBehavior=0", factory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testuser", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNull(factory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals(factory.getUsername(), clonedFactory.getUsername());
        assertEquals(factory.getPassword(), clonedFactory.getPassword());
        assertEquals(factory.getHost(), clonedFactory.getHost());
        assertEquals(factory.getPort(), clonedFactory.getPort());
        assertEquals(factory.getDefaultSchema(), clonedFactory.getDefaultSchema());
        assertEquals(factory.getParameters(), clonedFactory.getParameters());
        assertEquals(factory.getDialectType(), clonedFactory.getDialectType());
        assertEquals(factory.getAutoCommit(), clonedFactory.getAutoCommit());
        assertEquals(factory.getInitConnection(), clonedFactory.getInitConnection());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newusername";
        factory.resetUsername(valueMapper -> newUsername);
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newpassword";
        factory.resetPassword(valueMapper -> newPassword);
        assertNull(factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newhost";
        factory.resetHost(valueMapper -> newHost);
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        factory.resetPort(valueMapper -> newPort);
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newschema";
        factory.resetSchema(valueMapper -> newSchema);
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key", "value");
        factory.resetParameters(valueMapper -> newParameters);
        assertEquals(newParameters, factory.getParameters());
    }

    @Test
    public void testInitializeConnection() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn("jdbc:obc://localhost:1527/test?useSSL=false&zeroDateTimeBehavior=0");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(new ConnectionInitializer() {
            @Override
            public void initializeDatabaseConnection(Connection connection) throws SQLException {
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
            }
        }));
        when(connectionExtensionPoint.isConnectionValidationRequired()).thenReturn(true);

        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        ds.setEventPublisher(mock(EventPublisher.class));
        ds.setUrl("jdbc:obc://localhost:1527/test?useSSL=false&zeroDateTimeBehavior=0");
        ds.setUsername("testuser");
        ds.setPassword("testpassword");
        ds.setDriverClassName("com.oceanbase.jdbc.Driver");

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSourceFromFactory = factory.getDataSource();

        assertEquals(ds, dataSourceFromFactory);

        dataSourceFromFactory.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
    }

    @Test
    public void testNoInitializationConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        ds.setEventPublisher(mock(EventPublisher.class));
        ds.setUrl("jdbc:obc://localhost:1527/test?useSSL=false&zeroDateTimeBehavior=0");
        ds.setUsername("testuser");
        ds.setPassword("testpassword");
        ds.setDriverClassName("com.oceanbase.jdbc.Driver");

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        DataSource dataSourceFromFactory = factory.getDataSource();

        assertEquals(ds, dataSourceFromFactory);

        assertFalse(dataSourceFromFactory.getConnection().isClosed());
    }

    @Test
    public void testGetConnectionWithSSL() throws SQLException {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("key.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn("jdbc:obc://localhost:1527/test?useSSL=true&trustServerCertificate=true"
                        + "&trustStore=" + connectionConfig.getSslFileEntry().getKeyStoreFilePath()
                        + "&trustStorePassword=" + connectionConfig.getSslFileEntry().getKeyStoreFilePassword());
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");

        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        ds.setEventPublisher(mock(EventPublisher.class));
        ds.setUrl("jdbc:obc://localhost:1527/test?useSSL=true&trustServerCertificate=true"
                + "&trustStore=" + connectionConfig.getSslFileEntry().getKeyStoreFilePath()
                + "&trustStorePassword=" + connectionConfig.getSslFileEntry().getKeyStoreFilePassword());
        ds.setUsername("testuser");
        ds.setPassword("testpassword");
        ds.setDriverClassName("com.oceanbase.jdbc.Driver");
        ds.setUseSSL(true);

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSourceFromFactory = factory.getDataSource();

        assertEquals(ds, dataSourceFromFactory);
    }

    @Test
    public void testGetConnectionWithoutSSL() throws SQLException {
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn("jdbc:obc://localhost:1527/test?useSSL=false");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");

        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        ds.setEventPublisher(mock(EventPublisher.class));
        ds.setUrl("jdbc:obc://localhost:1527/test?useSSL=false");
        ds.setUsername("testuser");
        ds.setPassword("testpassword");
        ds.setDriverClassName("com.oceanbase.jdbc.Driver");

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSourceFromFactory = factory.getDataSource();

        assertEquals(ds, dataSourceFromFactory);
    }
}