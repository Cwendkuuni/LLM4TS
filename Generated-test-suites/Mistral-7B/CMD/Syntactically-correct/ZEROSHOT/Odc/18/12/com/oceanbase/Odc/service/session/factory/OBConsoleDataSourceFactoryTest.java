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
import org.mockito.ArgumentMatchers;
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
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;

    private OBConsoleDataSourceFactory testObject;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = testObject.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        when(connectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyMap()))
                .thenReturn("testJdbcUrl");
        when(connectionExtensionPoint.getDriverClassName())
                .thenReturn("com.oceanbase.jdbc.Driver");

        DataSource dataSource = testObject.getDataSource();
        assertEquals("com.oceanbase.jdbc.Driver", dataSource.getConnectionProperties().get("driverClassName"));

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = testObject.getDialectType();
        assertEquals(testObject.connectionConfig.getDialectType(), dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        CloneableDataSourceFactory deepCopy = testObject.deepCopy();
        assertEquals(testObject, deepCopy);

        deepCopy.resetUsername(mapper);
        deepCopy.resetPassword(mapper);
        deepCopy.resetHost(mapper);
        deepCopy.resetPort(mapper);
        deepCopy.resetSchema(mapper);
        deepCopy.resetParameters(mapper);

        assertNotEquals(testObject.username, deepCopy.getUsername());
        assertNotEquals(testObject.password, deepCopy.getPassword());
        assertNotEquals(testObject.host, deepCopy.getHost());
        assertNotEquals(testObject.port, deepCopy.getPort());
        assertNotEquals(testObject.defaultSchema, deepCopy.getDefaultSchema());
        assertNotEquals(testObject.parameters, deepCopy.getParameters());
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "oldUsername";
        String newUsername = "newUsername";

        testObject.resetUsername(arg -> arg = newUsername);

        assertEquals(newUsername, testObject.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        testObject.resetPassword(arg -> arg = newPassword);

        assertEquals(newPassword, testObject.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = "oldHost";
        String newHost = "newHost";

        testObject.resetHost(arg -> arg = newHost);

        assertEquals(newHost, testObject.getHost());
    }

    @Test
    public void testResetPort() {
        int oldPort = 1234;
        int newPort = 5678;

        testObject.resetPort(arg -> arg = newPort);

        assertEquals(newPort, testObject.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "oldSchema";
        String newSchema = "newSchema";

        testObject.resetSchema(arg -> arg = newSchema);

        assertEquals(newSchema, testObject.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParams = new HashMap<>();
        Map<String, String> newParams = new HashMap<>();
        newParams.put("newKey", "newValue");

        testObject.resetParameters(arg -> arg.put("newKey", "newValue"));

        assertEquals(newParams, testObject.getParameters());
    }

    @Test
    public void testCloneableDataSourceFactoryMethods() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;

        testObject.resetUsername(mapper);
        testObject.resetPassword(mapper);
        testObject.resetHost(mapper);
        testObject.resetPort(mapper);
        testObject.resetSchema(mapper);
        testObject.resetParameters(mapper);

        CloneableDataSourceFactory deepCopy = testObject.deepCopy();

        assertEquals(testObject.username, deepCopy.getUsername());
        assertEquals(testObject.password, deepCopy.getPassword());
        assertEquals(testObject.host, deepCopy.getHost());
        assertEquals(testObject.port, deepCopy.getPort());
        assertEquals(testObject.defaultSchema, deepCopy.getDefaultSchema());
        assertEquals(testObject.parameters, deepCopy.getParameters());
    }

    @Test
    public void testInitializeDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        testObject.setEventPublisher(eventPublisher);
        testObject.setConnectionExtensionPoint(connectionExtensionPoint);
        testObject.setConnectionInitializer(connectionInitializer);
        testObject.setSessionCreatedInitializer(sessionCreatedInitializer);
        testObject.initializeDataSource(dataSource);

        verify(eventPublisher).publish(ArgumentMatchers.any(ConnectionCreatedEvent.class));
        verify(connectionInitializer).initializeDatabase(dataSource.getConnection());
        verify(sessionCreatedInitializer).initialize(dataSource.getConnection());
    }

    @Test
    public void testInitializeDataSourceWithConnectionExtensionPointNull() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        testObject.setEventPublisher(eventPublisher);
        testObject.setConnectionInitializer(connectionInitializer);
        testObject.setSessionCreatedInitializer(sessionCreatedInitializer);
        testObject.initializeDataSource(dataSource);

        // connectionExtensionPoint should not be used
        verifyNoInteractions(connectionExtensionPoint);
    }

    @Test(expected = NullPointerException.class)
    public void testInitializeDataSourceWithEventPublisherNull() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        testObject.setConnectionExtensionPoint(connectionExtensionPoint);
        testObject.setConnectionInitializer(connectionInitializer);
        testObject.setSessionCreatedInitializer(sessionCreatedInitializer);
        testObject.initializeDataSource(dataSource);
    }

    @Test(expected = NullPointerException.class)
    public void testInitializeDataSourceWithConnectionInitializerNull() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        testObject.setEventPublisher(eventPublisher);
        testObject.setConnectionExtensionPoint(connectionExtensionPoint);
        testObject.setSessionCreatedInitializer(sessionCreatedInitializer);
        testObject.initializeDataSource(dataSource);
    }

    @Test(expected = NullPointerException.class)
    public void testInitializeDataSourceWithSessionCreatedInitializerNull() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        testObject.setEventPublisher(eventPublisher);
        testObject.setConnectionExtensionPoint(connectionExtensionPoint);
        testObject.setConnectionInitializer(connectionInitializer);
        testObject.initializeDataSource(dataSource);
    }

    @After
    public void teardown() {
        connectionExtensionPoint = null;
        connectionSessionUtil = null;
        eventPublisher = null;
        connectionInitializer = null;
        sessionCreatedInitializer = null;
        connectionPluginUtil = null;
    }
}