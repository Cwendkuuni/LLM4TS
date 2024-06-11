package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
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

public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USER = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 63306;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final String TEST_JDBC_URL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/"
            + TEST_DEFAULT_Schema;
    private static final String TEST_JDBC_URL_SSL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/"
            + TEST_DEFAULT_SCHEMA + "?useSSL=true&trustServerCertificate=true";
    private static final Map<String, String> TEST_JDBC_PARAMS = new HashMap<>();
    static {
        TEST_JDBC_PARAMS.put("maxAllowedPacket", "64000000");
        TEST_JDBC_PARAMS.put("allowMultiQueries", "true");
        TEST_JDBC_PARAMS.put("connectTimeout", "5000");
        TEST_JDBC_PARAMS.put("zeroDateTimeBehavior", OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        TEST_JDBC_PARAMS.put("noDatetimeStringSync", "true");
        TEST_JDBC_PARAMS.put("jdbcCompliantTruncation", "false");
        TEST_JDBC_PARAMS.put("sendConnectionAttributes", "false");
    }
    private static final ConnectionExtensionPoint TEST_CONNECTION_EXTENSION_POINT = Mockito.mock(ConnectionExtensionPoint.class);
    private static final EventPublisher TEST_EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername(TEST_USER);
        testConnectionConfig.setPassword(TEST_PASSWORD);
        testConnectionConfig.setHost(TEST_HOST);
        testConnectionConfig.setPort(TEST_PORT);
        testConnectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        testConnectionConfig.setType(ConnectType.OB_MYSQL);
        testConnectionConfig.setDialectType(DialectType.OB_MYSQL);
        testConnectionConfig.setJdbcUrlParameters(TEST_JDBC_PARAMS);
        testConnectionConfig.setSslConfig(new SSLConfig(true, null, null));
        testConnectionConfig.setEndpoint(null);
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        dataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true, true);
        when(TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA,
                TEST_JDBC_PARAMS)).thenReturn(TEST_JDBC_URL);
        when(TEST_CONNECTION_EXTENSION_POINT.getConnectionInitializers()).thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));
        when(TEST_CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        doNothing().when(TEST_EVENT_PUBLISHER).publish(any());
        when(TEST_EVENT_PUBLISHER.getEventName()).thenReturn("TEST_EVENT_NAME");
        dataSourceFactory.setEventPublisher(TEST_EVENT_PUBLISHER);
    }

    @After
    public void tearDown() {
        dataSourceFactory = null;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertEquals(TEST_JDBC_URL, connection.getConnectionURL());
        assertEquals(TEST_USER, connection.getUserInfo().getUserName());
        assertEquals(TEST_PASSWORD, connection.getConnection().getCLOBs().getPassword());
        connection.close();
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException {
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername(TEST_USER);
        testConnectionConfig.setPassword(TEST_PASSWORD);
        testConnectionConfig.setHost(TEST_HOST);
        testConnectionConfig.setPort(TEST_PORT);
        testConnectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        testConnectionConfig.setType(ConnectType.OB_MYSQL);
        testConnectionConfig.setDialectType(DialectType.OB_MYSQL);
        testConnectionConfig.setJdbcUrlParameters(TEST_JDBC_PARAMS);
        testConnectionConfig.setSslConfig(new SSLConfig(true, "/path/to/truststore.p12", "/path/to/keystore.p12"));
        testConnectionConfig.setEndpoint(null);
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true, true);
        when(TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA,
                TEST_JDBC_PARAMS)).thenReturn(TEST_JDBC_URL_SSL);
        when(TEST_CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        doNothing().when(TEST_EVENT_PUBLISHER).publish(any());
        dataSourceFactory.setEventPublisher(TEST_EVENT_PUBLISHER);
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertEquals(TEST_JDBC_URL_SSL, connection.getConnectionURL());
        assertEquals(TEST_USER, connection.getUserInfo().getUserName());
        assertEquals(TEST_PASSWORD, connection.getConnection().getCLOBs().getPassword());
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidJdbcUrl() {
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername(TEST_USER);
        testConnectionConfig.setPassword(TEST_PASSWORD);
        testConnectionConfig.setHost(TEST_HOST);
        testConnectionConfig.setPort(TEST_PORT);
        testConnectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        testConnectionConfig.setType(ConnectType.OB_MYSQL);
        testConnectionConfig.setDialectType(DialectType.OB_MYSQL);
        testConnectionConfig.setJdbcUrlParameters(TEST_JDBC_PARAMS);
        testConnectionConfig.setSslConfig(new SSLConfig(true, null, null));
        testConnectionConfig.setEndpoint(null);
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true, true);
        when(TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA,
                TEST_JDBC_PARAMS)).thenThrow(new SQLException("Invalid JDBC URL"));
        when(TEST_CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        dataSourceFactory.setEventPublisher(TEST_EVENT_PUBLISHER);
        dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals(TEST_JDBC_URL, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetJdbcUrlWithSsl() {
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername(TEST_USER);
        testConnectionConfig.setPassword(TEST_PASSWORD);
        testConnectionConfig.setHost(TEST_HOST);
        testConnectionConfig.setPort(TEST_PORT);
        testConnectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        testConnectionConfig.setType(ConnectType.OB_MYSQL);
        testConnectionConfig.setDialectType(DialectType.OB_MYSQL);
        testConnectionConfig.setJdbcUrlParameters(TEST_JDBC_PARAMS);
        testConnectionConfig.setSslConfig(new SSLConfig(true, "/path/to/truststore.p12", "/path/to/keystore.p12"));
        testConnectionConfig.setEndpoint(null);
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true, true);
        assertEquals(TEST_JDBC_URL_SSL, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USER, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.OB_MYSQL, dataSourceFactory.getConnectType());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals(TEST_DEFAULT_SCHEMA, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        assertEquals(TEST_JDBC_PARAMS, dataSourceFactory.getParameters());
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getJdbcUrl(), clonedDataSourceFactory.getJdbcUrl());
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(dataSourceFactory.getEventPublisher(), clonedDataSourceFactory.getEventPublisher());
        assertEquals(dataSourceFactory.getConnectionExtensionPoint(), clonedDataSourceFactory.getConnectionExtensionPoint());
    }
}