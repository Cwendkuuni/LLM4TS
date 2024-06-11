package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
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

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.JDBC);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = dataSourceFactory.getDataSource();
        eventPublisher = new EventPublisher();
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.MYSQL);
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            ((BasicDataSource) dataSource).close();
        }
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(dataSource);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testuser@test_tenant";
        connectionConfig.setTenantName("test_tenant");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualUsername = dataSourceFactory.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testpassword";
        String actualPassword = dataSourceFactory.getPassword();
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.MYSQL;
        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        connectionExtensionPoint = null;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSourceWithInitialization() throws SQLException {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
        List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
        assertFalse(initializers.isEmpty());
        initializers.forEach(initializer -> {
            try {
                initializer.initializeDataSource(dataSource);
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        });
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithAutoCommitFalse() throws Exception {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSource = dataSourceFactory.getDataSource();
        try (SingleConnectionDataSource singleDataSource = (SingleConnectionDataSource) dataSource) {
            singleDataSource.setAutoCommit(false);
            singleDataSource.getConnection().setAutoCommit(false);
            try (java.sql.Connection connection = singleDataSource.getConnection()) {
                connection.setAutoCommit(false);
                assertFalse(connection.isAutoCommit());
                connection.rollback();
                assertFalse(connection.isAutoCommit());
            }
        }
        try (java.sql.Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            assertFalse(connection.isAutoCommit());
            connection.rollback();
            assertFalse(connection.isAutoCommit());
        }
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithAutoCommitTrue() throws Exception {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
        try (SingleConnectionDataSource singleDataSource = (SingleConnectionDataSource) dataSource) {
            singleDataSource.setAutoCommit(false);
            singleDataSource.getConnection().setAutoCommit(false);
            try (java.sql.Connection connection = singleDataSource.getConnection()) {
                connection.setAutoCommit(true);
                assertTrue(connection.isAutoCommit());
                connection.commit();
                assertTrue(connection.isAutoCommit());
            }
        }
        try (java.sql.Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            assertTrue(connection.isAutoCommit());
            connection.commit();
            assertTrue(connection.isAutoCommit());
        }
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidUsername() throws Exception {
        connectionConfig.setUsername(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidPassword() throws Exception {
        connectionConfig.setPassword(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidHost() throws Exception {
        connectionConfig.setHost(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidPort() throws Exception {
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDataSourceWithJdbcParams() throws Exception {
        Map<String, String> jdbcUrlParams = new HashMap<>();
        jdbcUrlParams.put("maxAllowedPacket", "67108864");
        connectionConfig.setJdbcUrlParameters(jdbcUrlParams);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
        Properties connectionProperties = dataSource.getConnectionProperties();
        String maxAllowedPacket = connectionProperties.getProperty("maxAllowedPacket");
        assertEquals("67108864", maxAllowedPacket);
    }

    @Test
    public void testGetDataSourceWithSslConfig() throws Exception {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test.p12");
        connectionConfig.setSslFileEntry(sslFileEntry);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
        String useSSL = dataSource.getConnectionProperties().getProperty("useSSL");
        assertTrue(useSSL.equals("true"));
        String trustServerCertificate = dataSource.getConnectionProperties().getProperty("trustServerCertificate");
        assertTrue(trustServerCertificate.equals("true"));
        String trustStore = dataSource.getConnectionProperties().getProperty("trustStore");
        String keyStore = dataSource.getConnectionProperties().getProperty("keyStore");
        String keyStorePassword = dataSource.getConnectionProperties().getProperty("keyStorePassword");
        assertEquals("test.p12", trustStore);
        assertEquals("test.p12", keyStore);
        assertEquals("testpassword", keyStorePassword);
    }
}