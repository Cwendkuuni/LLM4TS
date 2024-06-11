package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory.ValueMapper;
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
    private static final int TEST_PORT = 6306;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final DialectType TEST_DIALECT_TYPE = DialectType.OB_MYSQL;
    private static final Boolean TEST_AUTOCOMMIT = false;
    private static final boolean TEST_INIT_CONNECTION = true;
    private static final EventPublisher TEST_EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
    private static final ConnectionExtensionPoint TEST_CONNECTION_EXTENSION_POINT = Mockito.mock(ConnectionExtensionPoint.class);
    private static final ConnectionConfig TEST_CONNECTION_CONFIG = new ConnectionConfig("testTenant", "testCluster", TEST_USER, TEST_PASSWORD, TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, TEST_DIALECT_TYPE, TEST_AUTOCOMMIT, null, null, null);

    private OBConsoleDataSourceFactory testedObject;
    private BasicDataSource dataSource;

    @Before
    public void setUp() {
        Mockito.when(TEST_CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        Mockito.when(TEST_CONNECTION_EXTENSION_POINT.getConnectionInitializers()).thenReturn(Mockito.mockList(ConnectionInitializer.class));
        Mockito.when(TEST_CONNECTION_EXTENSION_POINT.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, Mockito.anyMap())).thenAnswer(invocation -> {
            Properties jdbcUrlParams = (Properties) invocation.getArguments()[3];
            jdbcUrlParams.setProperty("testKey", "testValue");
            return "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_DEFAULT_SCHEMA;
        });
        testedObject = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, TEST_AUTOCOMMIT, TEST_INIT_CONNECTION);
        dataSource = (BasicDataSource) testedObject.getDataSource();
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertEquals(TEST_HOST, DBUtils.getConnectionProperty(connection, "serverName"));
        assertEquals(TEST_PORT, DBUtils.getConnectionPropertyInt(connection, "portNumber"));
        assertEquals(TEST_DEFAULT_SCHEMA, DBUtils.getConnectionProperty(connection, "databaseName"));
        assertEquals(TEST_AUTOCOMMIT, connection.getAutoCommit());
        assertTrue(connection.isClosed());
        dataSource.setAutoCommit(false);
        connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("jdbc:test://localhost:6306/testSchema", testedObject.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USER, testedObject.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, testedObject.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(TEST_DIALECT_TYPE, testedObject.getDialectType());
    }

    @Test
    public void testResetUsername() {
        ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetUsername(mapper);
        assertEquals(TEST_USER, mapper.getMappedObject());
    }

    @Test
    public void testResetPassword() {
        ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetPassword(mapper);
        assertEquals(TEST_PASSWORD, mapper.getMappedObject());
    }

    @Test
    public void testResetHost() {
        ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetHost(mapper);
        assertEquals(TEST_HOST, mapper.getMappedObject());
    }

    @Test
    public void testResetPort() {
        ValueMapper<Integer> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetPort(mapper);
        assertEquals(TEST_PORT, mapper.getMappedObject());
    }

    @Test
    public void testResetSchema() {
        ValueMapper<String> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetSchema(mapper);
        assertEquals(TEST_DEFAULT_SCHEMA, mapper.getMappedObject());
    }

    @Test
    public void testResetParameters() {
        ValueMapper<Map<String, String>> mapper = Mockito.mock(ValueMapper.class);
        testedObject.resetParameters(mapper);
        Map<String, String> parameters = mapper.getMappedObject();
        assertEquals("testValue", parameters.get("testKey"));
    }

    @Test
    public void testCloneableDataSourceFactory() {
        OBConsoleDataSourceFactory cloned = (OBConsoleDataSourceFactory) testedObject.deepCopy();
        assertEquals(TEST_USER, cloned.getUsername());
        assertEquals(TEST_PASSWORD, cloned.getPassword());
        assertEquals(TEST_HOST, cloned.getHost());
        assertEquals(TEST_PORT, cloned.getPort());
        assertEquals(TEST_DEFAULT_SCHEMA, cloned.getDefaultSchema());
        assertEquals(TEST_DIALECT_TYPE, cloned.getDialectType());
        assertEquals(TEST_AUTOCOMMIT, cloned.isAutoCommit());
        assertEquals(TEST_INIT_CONNECTION, cloned.isInitConnection());
        assertEquals(TEST_EVENT_PUBLISHER, cloned.getEventPublisher());
        assertEquals(TEST_CONNECTION_EXTENSION_POINT, cloned.getConnectionExtensionPoint());
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConnectionWithoutInitialization() {
        OBConsoleDataSourceFactory uninitializedFactory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, TEST_AUTOCOMMIT, false);
        uninitializedFactory.getDataSource().getConnection();
    }
}