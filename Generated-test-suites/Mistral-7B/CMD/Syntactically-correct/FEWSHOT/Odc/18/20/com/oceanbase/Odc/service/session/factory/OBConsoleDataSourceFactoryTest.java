package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
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

public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_SCHEMA = "testSchema";
    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final int TEST_PORT = 3306;
    private static final String TEST_HOST = "localhost";
    private static final String TEST_JDBC_URL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_SCHEMA;
    private static final Map<String, String> TEST_PARAMS = new HashMap<>();
    private static final ConnectionConfig TEST_CONNECTION_CONFIG = new ConnectionConfig();
    private static final ConnectionExtensionPoint TEST_CONNECTION_EXTENSION_POINT = ConnectionPluginUtil.getConnectionExtension(DialectType.MYSQL);

    private OBConsoleDataSourceFactory testDataSourceFactory;
    private DataSource testDataSource;

    @Before
    public void setUp() {
        TEST_CONNECTION_CONFIG.setType(ConnectType.OB_MYSQL);
        TEST_CONNECTION_CONFIG.setHost(TEST_HOST);
        TEST_CONNECTION_CONFIG.setPort(TEST_PORT);
        TEST_CONNECTION_CONFIG.setUsername(TEST_USERNAME);
        TEST_CONNECTION_CONFIG.setPassword(TEST_PASSWORD);
        TEST_CONNECTION_CONFIG.setDefaultSchema(TEST_SCHEMA);
        TEST_CONNECTION_CONFIG.setJdbcUrlParameters(TEST_PARAMS);
        TEST_CONNECTION_CONFIG.setSslConfig(new SSLConfig(true, null, null));

        testDataSourceFactory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, true, true);
        testDataSource = testDataSourceFactory.getDataSource();
    }

    @After
    public void tearDown() {
        if (testDataSource != null) {
            ((SingleConnectionDataSource) testDataSource).destroy();
            testDataSource = null;
        }
        if (testDataSourceFactory != null) {
            testDataSourceFactory = null;
        }
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(testDataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, testDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = testDataSourceFactory.deepCopy();
        assertEquals(testDataSourceFactory.getJdbcUrl(), clonedDataSourceFactory.getJdbcUrl());
        assertEquals(testDataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(testDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(testDataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(testDataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(testDataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(testDataSourceFactory.getAutoCommit(), clonedDataSourceFactory.getAutoCommit());
        assertEquals(testDataSourceFactory.getInitConnection(), clonedDataSourceFactory.getInitConnection());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals(TEST_JDBC_URL, testDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USERNAME, testDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNull(testDataSourceFactory.getPassword());
    }

    @Test
    public void testGetPort() {
        assertEquals(TEST_PORT, testDataSourceFactory.getPort());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals(TEST_SCHEMA, testDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        assertEquals(TEST_PARAMS, testDataSourceFactory.getParameters());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        testDataSourceFactory.resetUsername(str -> str.replace(TEST_USERNAME, newUsername));
        assertEquals(newUsername, testDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        testDataSourceFactory.resetPassword(str -> str.replace(TEST_PASSWORD, newPassword));
        assertNull(testDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        testDataSourceFactory.resetHost(str -> str.replace(TEST_HOST, newHost));
        assertEquals(newHost, testDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        testDataSourceFactory.resetPort(intMapper -> intMapper + 1);
        assertEquals(newPort, testDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        testDataSourceFactory.resetSchema(str -> str.replace(TEST_SCHEMA, newSchema));
        assertEquals(newSchema, testDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        String newKey = "newKey";
        String newValue = "newValue";
        Map<String, String> newParams = new HashMap<>(TEST_PARAMS);
        newParams.put(newKey, newValue);
        testDataSourceFactory.resetParameters(mapper -> mapper.put(newKey, newValue));
        assertEquals(newParams, testDataSourceFactory.getParameters());
    }
}