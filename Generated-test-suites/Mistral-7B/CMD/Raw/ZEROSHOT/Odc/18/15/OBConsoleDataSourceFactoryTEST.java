package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

/**
 * Test class for OBConsoleDataSourceFactory.
 *
 * @author yh263208
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setup() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        connectionConfig = ConnectionMapper.INSTANCE.clone(ConnectionMapper.INSTANCE.newConnectionConfig("test",
                "test-password", "localhost", 1527, null, null, null, null, null, null, null,
                ConnectType.OB_MYSQL, DialectType.OB_MYSQL, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null));
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertNotNull(dialectType);
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedDataSourceFactory);
        assertNotNull(clonedDataSourceFactory.getUsername());
        assertNotNull(clonedDataSourceFactory.getPassword());
        assertNotNull(clonedDataSourceFactory.getHost());
        assertNotNull(clonedDataSourceFactory.getPort());
        assertNotNull(clonedDataSourceFactory.getDefaultSchema());
        assertNotNull(clonedDataSourceFactory.getParameters());
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        dataSourceFactory.resetUsername(Map::put);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(Map::put);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory.resetHost(Map::put);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3306;
        dataSourceFactory.resetPort(Map::put);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(Map::put);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("testKey", "testValue");
        dataSourceFactory.resetParameters(Map::put);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        String expectedJdbcUrl = "jdbc:obmysql://localhost:1527/test@localhost:1527?zeroDateTimeBehavior=0&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&trustServerCertificate=true&disableSslHostnameVerification=true&useSSL=true&socksProxyHost=&socksProxyPort=&keyStore=&keyStorePassword=&trustStore=&trustStorePassword=";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("test", username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("test-password", password);
    }

    @Test
    public void testGetHost() {
        String host = dataSourceFactory.getHost();
        assertEquals("localhost", host);
    }

    @Test
    public void testGetPort() {
        int port = dataSourceFactory.getPort();
        assertEquals(1527, port);
    }

    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertNull(defaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = dataSourceFactory.getParameters();
        assertNotNull(parameters);
        assertEquals(0, parameters.size());
    }

    @Test
    public void testGetConnectionInitializer() {
        List<ConnectionInitializer> connectionInitializers = Arrays.asList(Mockito.mock(ConnectionInitializer.class));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(connectionInitializers);
        List<ConnectionInitializer> initializers = dataSourceFactory.getConnectionInitializers();
        assertNotNull(initializers);
        assertEquals(connectionInitializers, initializers);
    }

    @Test
    public void testNoConnectionInitializer() {
        List<ConnectionInitializer> connectionInitializers = Arrays.asList();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        Mockito.when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(connectionInitializers);
        List<ConnectionInitializer> initializers = dataSourceFactory.getConnectionInitializers();
        assertNull(initializers);
    }

    @After
    public void cleanup() {
        dataSourceFactory = null;
        connectionConfig = null;
        connectionExtensionPoint = null;
    }
}