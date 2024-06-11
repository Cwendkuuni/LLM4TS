package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.sql.DataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        connectionConfig.setEndpoint(new OBTenantEndpoint("localhost", 8080, null, null));
        connectionConfig.setJdbcUrlParameters(new HashMap<String, String>() {{
            put("maxAllowedPacket", "64000000");
            put("allowMultiQueries", "true");
            put("connectTimeout", "5000");
            put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
            put("noDatetimeStringSync", "true");
            put("jdbcCompliantTruncation", "false");
            put("sendConnectionAttributes", "false");
        }});

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testCreateDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        PooledConnection pooledConnection = (PooledConnection) connection;
        Connection originalConnection = pooledConnection.getParentConnection();

        assertNotNull(dataSource);
        assertNotNull(connection);
        assertNotNull(originalConnection);
        assertEquals(connectionConfig.getUsername(), originalConnection.getUserName());
        assertEquals(connectionConfig.getPassword(), originalConnection.getCatalog());
        assertTrue(originalConnection.isClosed());
        assertEquals("localhost:3306/testSchema", originalConnection.getSchema());
        assertFalse(originalConnection.isClosed());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?"
                + "maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000"
                + "&zeroDateTimeBehavior=" + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR
                + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false"
                + "&sendConnectionAttributes=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetConnectionWithSsl() throws SQLException {
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.generateJdbcUrl(eq("localhost"), anyInt(), eq("testSchema"), anyMap()))
                .thenReturn("jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=true");
        ConnectionPluginUtil connectionPluginUtil = new ConnectionPluginUtil(connectionExtensionPoint);
        when(connectionPluginUtil.getConnectionExtension(eq(connectionConfig.getDialectType()))).thenReturn(connectionExtensionPoint);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = dataSourceFactory.getDataSource();

        Connection connection = dataSource.getConnection();
        PooledConnection pooledConnection = (PooledConnection) connection;
        Connection originalConnection = pooledConnection.getParentConnection();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(connectionExtensionPoint).generateJdbcUrl(eq("localhost"), anyInt(), eq("testSchema"), anyMap());
        assertEquals("localhost:3306/testSchema", originalConnection.getSchema());
        assertEquals("testUser", originalConnection.getUserName());
        assertEquals("testPassword", originalConnection.getCatalog());
        assertTrue(originalConnection.isClosed());
        assertEquals("jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=true", originalConnection.getURL());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithInvalidPort() {
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithInvalidUsername() {
        connectionConfig.setUsername(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithInvalidHost() {
        connectionConfig.setHost(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithInvalidUsernameAndPassword() {
        connectionConfig.setUsername(null);
        connectionConfig.setPassword(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullConnectionConfig() {
        dataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newTestUser";
        dataSourceFactory.resetUsername(arg -> arg.map(str -> newUsername));
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newTestPassword";
        dataSourceFactory.resetPassword(arg -> arg.map(str -> newPassword));
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory.resetHost(arg -> arg.map(str -> newHost));
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory.resetPort(arg -> arg.map(intVal -> newPort));
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(arg -> arg.map(str -> newSchema));
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newParameter1", "newValue1");
        dataSourceFactory.resetParameters(arg -> arg.map(map -> newParameters));
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @After
    public void tearDown() {
        Mockito.reset(connectionExtensionPoint);
    }
}