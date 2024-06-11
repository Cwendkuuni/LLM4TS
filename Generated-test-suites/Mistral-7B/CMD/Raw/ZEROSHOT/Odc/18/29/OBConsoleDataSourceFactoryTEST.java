package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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

/**
 * Unit test for {@link OBConsoleDataSourceFactory} class
 */
public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory testObject;

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionMapper connectionMapper;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil sessionUtil = mock(ConnectionSessionUtil.class);
        when(connectionPluginUtil.getSessionUtil(any())).thenReturn(sessionUtil);
        when(connectionPluginUtil.getConnectionExtension(any())).thenReturn(connectionExtensionPoint);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        when(connectionExtensionPoint.generateJdbcUrl(any(), anyInt(), anyString(), anyMap()))
                .thenAnswer(invocationOnMock -> "jdbc:obnew:");
        testObject = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        DataSource dataSource = testObject.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        when(connectionPluginUtil.getSessionUtil(connection.getConnection()).getDialectType())
                .thenReturn(DialectType.OB_ORACLE);
        connection.createStatement().execute("select 1 from dual");
        connection.close();
    }

    @Test
    public void testGetDataSourceWithInitializer() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ArrayList<ConnectionInitializer>() {{
                    add(connectionInitializer);
                }});
        DataSource dataSource = testObject.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        when(connectionPluginUtil.getSessionUtil(connection.getConnection()).getDialectType())
                .thenReturn(DialectType.OB_ORACLE);
        connection.createStatement().execute("select 1 from dual");
        connection.close();
        verify(connectionInitializer).initialize(connection);
    }

    @Test
    public void testGetDataSourceWithSessionCreatedInitializer() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionPluginUtil.getSessionUtil(any())).thenReturn(mock(ConnectionSessionUtil.class));
        when(connectionPluginUtil.getConnectionExtension(any()))
                .thenReturn(connectionExtensionPoint);
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ArrayList<ConnectionInitializer>() {{
                    add(connectionInitializer);
                }});
        when(connectionExtensionPoint.generateJdbcUrl(any(), anyInt(), anyString(), anyMap()))
                .thenAnswer(invocationOnMock -> "jdbc:obnew:");
        when(connectionPluginUtil.getConnectionExtension(any()))
                .thenReturn(connectionExtensionPoint);

        OBConsoleDataSourceFactory testObject = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = testObject.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        when(connectionPluginUtil.getSessionUtil(connection.getConnection()).getDialectType())
                .thenReturn(DialectType.OB_ORACLE);
        connection.createStatement().execute("select 1 from dual");
        connection.close();
        verify(sessionCreatedInitializer).initialize(connection);
    }

    @Test
    public void testGetDataSourceWithInvalidConnection() throws SQLException {
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ArrayList<ConnectionInitializer>() {{
                    add(connectionInitializer);
                }});
        DataSource dataSource = testObject.getDataSource();
        assertNotNull(dataSource);

        try {
            testObject.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testGetJdbcUrl() {
        String host = "localhost";
        int port = 1527;
        String schema = "test_user";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("maxAllowedPacket", "64000000");

        String jdbcUrl = testObject.getJdbcUrl();
        String expectedUrl = "jdbc:obnew://localhost:1527/test_user?maxAllowedPacket=64000000";
        assertEquals(expectedUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");

        String username = testObject.getUsername();
        assertEquals("test_user", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("test_password");

        String password = testObject.getPassword();
        assertEquals("test_password", password);
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("test_schema");

        String schema = testObject.getDefaultSchema();
        assertEquals("test_schema", schema);
    }

    @Test
    public void testClone() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil sessionUtil = mock(ConnectionSessionUtil.class);
        when(connectionPluginUtil.getSessionUtil(any())).thenReturn(sessionUtil);
        when(connectionPluginUtil.getConnectionExtension(any()))
                .thenReturn(connectionExtensionPoint);
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ArrayList<ConnectionInitializer>() {{
                    add(connectionInitializer);
                }});
        when(connectionExtensionPoint.generateJdbcUrl(any(), anyInt(), anyString(), anyMap()))
                .thenAnswer(invocationOnMock -> "jdbc:obnew:");
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clone = original.deepCopy(connectionMapper);

        assertNotSame(original, clone);
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertEquals(original.getDialectType(), clone.getDialectType());
        assertEquals(original.getAutoCommit(), clone.getAutoCommit());
        assertEquals(original.getInitConnection(), clone.getInitConnection());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String username = "test_user";

        testObject.resetUsername(connectionMapper);

        assertEquals(username, testObject.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String password = "test_password";

        testObject.resetPassword(connectionMapper);

        assertEquals(password, testObject.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String host = "test_host";

        testObject.resetHost(connectionMapper);

        assertEquals(host, testObject.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        int port = 1234;

        testObject.resetPort(connectionMapper);

        assertEquals(port, testObject.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        testObject.resetSchema(connectionMapper);

        assertEquals(schema, testObject.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("test", "test_value");

        testObject.resetParameters(connectionMapper);

        assertEquals(parameters, testObject.getParameters());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        DialectType dialectType = testObject.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }
}