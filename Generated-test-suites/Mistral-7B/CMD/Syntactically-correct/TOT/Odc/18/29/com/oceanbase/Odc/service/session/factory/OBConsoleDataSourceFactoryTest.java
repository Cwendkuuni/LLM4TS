package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
    }

    @Test
    public void testCreateDataSource() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());
        dataSource.close();
    }

    @Test
    public void testGetJdbcUrl() {
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        String jdbcUrl = factory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?useSSL=false&zeroDateTimeBehavior=DEFAULT_ZERO_DATE_TIME_BEHAVIOR";
        assertTrue(jdbcUrl.equals(expectedJdbcUrl));
    }

    @Test(expected = RuntimeException.class)
    public void testNegativePortNumber() {
        connectionConfig.setPort(-1);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test(expected = RuntimeException.class)
    public void testNullUsername() {
        connectionConfig.setUsername(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test(expected = RuntimeException.class)
    public void testNullPassword() {
        connectionConfig.setPassword(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetDataSourceWithEventPublisher() throws SQLException {
        EventPublisher eventPublisher = new EventPublisher();
        ConnectionConfig connectionConfigWithEventPublisher = new ConnectionConfig();
        connectionConfigWithEventPublisher.setEventPublisher(eventPublisher);
        connectionConfigWithEventPublisher.setHost("localhost");
        connectionConfigWithEventPublisher.setPort(3306);
        connectionConfigWithEventPublisher.setUsername("testuser");
        connectionConfigWithEventPublisher.setPassword("testpassword");
        connectionConfigWithEventPublisher.setDefaultSchema("test_schema");
        connectionConfigWithEventPublisher.setType(ConnectType.MYSQL);
        connectionConfigWithEventPublisher.setDialectType(DialectType.MYSQL);
        connectionConfigWithEventPublisher.setJdbcUrlParameters(new HashMap<>());
        factory = new OBConsoleDataSourceFactory(connectionConfigWithEventPublisher, false, true);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());
        dataSource.close();
    }

    @Test
    public void testGetDataSourceWithSslConfig() throws SQLException {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());
        dataSource.close();
    }

    @Test(expected = RuntimeException.class)
    public void testNullConnectionConfig() {
        factory = new OBConsoleDataSourceFactory(null, false, true);
    }

    @Test
    public void testDeepCopy() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertNotNull(clonedFactory);
        SingleConnectionDataSource clonedDataSource = (SingleConnectionDataSource) clonedFactory.getDataSource();
        Connection clonedConnection = clonedDataSource.getConnection();
        assertNotNull(clonedConnection);
        assertTrue(clonedConnection.isClosed());
        clonedDataSource.close();
    }

    @Test
    public void testResetProperties() throws SQLException {
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        factory.resetUsername((value) -> "newUsername");
        assertEquals("newUsername", factory.getUsername());
        factory.resetPassword((value) -> "newPassword");
        assertEquals("newPassword", factory.getPassword());
        factory.resetHost((value) -> "newHost");
        assertEquals("newHost", factory.getHost());
        factory.resetPort((value) -> 12345);
        assertEquals(12345, factory.getPort());
        factory.resetSchema((value) -> "newSchema");
        assertEquals("newSchema", factory.getDefaultSchema());
        factory.resetParameters((value) -> new HashMap<>());
        assertTrue(factory.getParameters().isEmpty());
    }

    private void assertEquals(String expected, String actual) {
        assertTrue(expected.equals(actual));
    }
}