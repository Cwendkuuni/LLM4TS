package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private SingleConnectionDataSource dataSource;

    private OBConsoleDataSourceFactory testObject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(6306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(connectionConfig.getUsername()).thenReturn("test_username");
        when(connectionConfig.getPassword()).thenReturn("test_password");
        when(connectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getSslConfig()).thenReturn(new SSLConfig());
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<ConnectionInitializer>() {{
            add(connectionInitializer);
        }});
        testObject = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        testObject.setEventPublisher(eventPublisher);
    }

    @After
    public void teardown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource expectedDataSource = new SingleConnectionDataSource(true);
        when(eventPublisher.publishEvent(any())).thenReturn(null);
        when(connectionInitializer.initializeDatabase()).thenReturn(true);

        DataSource actualDataSource = testObject.getDataSource();

        assertEquals("Expected and actual data sources to be the same", expectedDataSource, actualDataSource);
        verify(eventPublisher).publishEvent(any());
        verify(connectionInitializer).initializeDatabase();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(null, false);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullEventPublisher() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        factory.setEventPublisher(null);
        factory.getDataSource();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithNullDriverClassName() {
        when(connectionExtensionPoint.getDriverClassName()).thenReturn(null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        factory.getDataSource();
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:6306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR
                + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        String actualJdbcUrl = testObject.getJdbcUrl();

        assertEquals("Expected and actual JDBC URLs to be the same", expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "test_username@test_tenant";
        String actualUsername = testObject.getUsername();

        assertEquals("Expected and actual usernames to be the same", expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "test_password";
        String actualPassword = testObject.getPassword();

        assertEquals("Expected and actual passwords to be the same", expectedPassword, actualPassword);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_MYSQL;
        DialectType actualDialectType = testObject.getDialectType();

        assertEquals("Expected and actual dialect types to be the same", expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() throws CloneNotSupportedException {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, false);
        OBConsoleDataSourceFactory cloned = (OBConsoleDataSourceFactory) original.deepCopy();

        assertNotSame("Expected original and cloned objects to be different", original, cloned);

        assertEquals("Expected original and cloned objects to have the same connectionConfig", connectionConfig,
                cloned.connectionConfig);
        assertEquals("Expected original and cloned objects to have the same username", testObject.getUsername(),
                cloned.getUsername());
        assertEquals("Expected original and cloned objects to have the same password", testObject.getPassword(),
                cloned.getPassword());
        assertEquals("Expected original and cloned objects to have the same host", testObject.getHost(),
                cloned.getHost());
        assertEquals("Expected original and cloned objects to have the same port", testObject.getPort(),
                cloned.getPort());
        assertEquals("Expected original and cloned objects to have the same defaultSchema", testObject.getDefaultSchema(),
                cloned.getDefaultSchema());
        assertEquals("Expected original and cloned objects to have the same parameters", testObject.getParameters(),
                cloned.getParameters());
        assertEquals("Expected original and cloned objects to have the same dialectType", testObject.getDialectType(),
                cloned.getDialectType());
        assertEquals("Expected original and cloned objects to have the same autoCommit", testObject.isAutoCommit(),
                cloned.isAutoCommit());
        assertEquals("Expected original and cloned objects to have the same initConnection", testObject.isInitConnection(),
                cloned.isInitConnection());
        assertEquals("Expected original and cloned objects to have the same connectionExtensionPoint",
                testObject.connectionExtensionPoint, cloned.connectionExtensionPoint);

        assertNotEquals("Expected original and cloned objects to have different URLs", testObject.getJdbcUrl(),
                cloned.getJdbcUrl());

        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory deepCopied = cloned.deepCopy(mapper);

        assertEquals("Expected original and deep-cloned objects to have the same connectionConfig", connectionConfig,
                deepCopied.connectionConfig);
        assertEquals("Expected original and deep-cloned objects to have the same username", testObject.getUsername(),
                deepCopied.getUsername());
        assertEquals("Expected original and deep-cloned objects to have the same password", testObject.getPassword(),
                deepCopied.getPassword());
        assertEquals("Expected original and deep-cloned objects to have the same host", testObject.getHost(),
                deepCopied.getHost());
        assertEquals("Expected original and deep-cloned objects to have the same port", testObject.getPort(),
                deepCopied.getPort());
        assertEquals("Expected original and deep-cloned objects to have the same defaultSchema", testObject.getDefaultSchema(),
                deepCopied.getDefaultSchema());
        assertEquals("Expected original and deep-cloned objects to have the same parameters", testObject.getParameters(),
                deepCopied.getParameters());
        assertEquals("Expected original and deep-cloned objects to have the same dialectType", testObject.getDialectType(),
                deepCopied.getDialectType());
        assertEquals("Expected original and deep-cloned objects to have the same autoCommit", testObject.isAutoCommit(),
                deepCopied.isAutoCommit());
        assertEquals("Expected original and deep-cloned objects to have the same initConnection", testObject.isInitConnection(),
                deepCopied.isInitConnection());
        assertEquals("Expected original and deep-cloned objects to have the same connectionExtensionPoint",
                testObject.connectionExtensionPoint, deepCopied.connectionExtensionPoint);
        assertNotEquals("Expected original and deep-cloned objects to have different URLs", testObject.getJdbcUrl(),
                deepCopied.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetUsername(mapper);

        assertEquals("Expected new username to be set", newUsername, testObject.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetPassword(mapper);

        assertEquals("Expected new password to be set", newPassword, testObject.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetHost(mapper);

        assertEquals("Expected new host to be set", newHost, testObject.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 7306;
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetPort(mapper);

        assertEquals("Expected new port to be set", newPort, testObject.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetSchema(mapper);

        assertEquals("Expected new schema to be set", newSchema, testObject.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("new_key", "new_value");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetParameters(mapper);

        Map<String, String> oldParameters = testObject.getParameters();
        Map<String, String> newParameters2 = new HashMap<>(oldParameters);
        newParameters2.putAll(newParameters);

        assertEquals("Expected new parameters to be set", newParameters2, testObject.getParameters());
    }
}