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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private BasicDataSource mockedDataSource;
    private ConnectionConfig mockedConnectionConfig;
    private EventPublisher mockedEventPublisher;
    private ConnectionExtensionPoint mockedConnectionExtensionPoint;
    private ConnectionInitializer mockedConnectionInitializer;

    @Before
    public void setUp() {
        mockedEventPublisher = mock(EventPublisher.class);
        mockedConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        mockedConnectionInitializer = mock(ConnectionInitializer.class);
        mockedConnectionConfig = new ConnectionConfig();
        mockedConnectionConfig.setHost("localhost");
        mockedConnectionConfig.setPort(3306);
        mockedConnectionConfig.setUsername("user");
        mockedConnectionConfig.setPassword("password");
        mockedConnectionConfig.setDefaultSchema("schema");
        mockedConnectionConfig.setType(ConnectType.MYSQL);
        mockedConnectionConfig.setSslConfig(new SSLConfig(true, new SSLFileEntry("path", "password")));
        mockedConnectionConfig.setJdbcUrlParameters(new HashMap<>());

        when(mockedConnectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(mockedConnectionInitializer));
        when(mockedConnectionExtensionPoint.getConnectionInitializers())
                .thenReturn(Collections.singletonList(mockedConnectionInitializer));
        when(mockedConnectionExtensionPoint.generateDriverClassName())
                .thenReturn("com.mysql.jdbc.Driver");
        when(mockedConnectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyMap()))
                .thenAnswer(arg -> "jdbc:mysql://localhost:3306/schema?useSSL=true&trustServerCertificate=true&"
                        + "trustStore=path&trustStorePassword=password");

        dataSourceFactory = new OBConsoleDataSourceFactory(mockedConnectionConfig, true, true);
        mockedDataSource = new BasicDataSource();
        dataSourceFactory.setDataSource(mockedDataSource);
    }

    @After
    public void tearDown() {
        dataSourceFactory = null;
        mockedDataSource = null;
        mockedConnectionConfig = null;
        mockedEventPublisher = null;
        mockedConnectionExtensionPoint = null;
        mockedConnectionInitializer = null;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        when(mockedDataSource.getConnection()).thenReturn(DriverManager.getConnection("jdbc:mysql://localhost:3306/schema",
                "user", "password"));
        Connection connection = dataSourceFactory.getDataSource().getConnection();
        assertNotNull(connection);
        assertEquals("user", connection.getUserInfo().toString());
        assertEquals("schema", connection.getCatalog());
        assertTrue(connection.isClosed());
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/schema?useSSL=true&trustServerCertificate=true&trustStore=path&trustStorePassword=password",
                jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals("user", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testCloneableDataSourceFactory() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotSame(dataSourceFactory, clonedDataSourceFactory);
        assertEquals("user", clonedDataSourceFactory.getUsername());
        assertEquals("password", clonedDataSourceFactory.getPassword());
        assertEquals("localhost", clonedDataSourceFactory.getHost());
        assertEquals(3306, clonedDataSourceFactory.getPort());
        assertEquals("schema", clonedDataSourceFactory.getDefaultSchema());
        assertEquals(Collections.emptyMap(), clonedDataSourceFactory.getParameters());
        assertEquals(DialectType.MYSQL, clonedDataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUser";
        dataSourceFactory.resetUsername(arg -> newUsername);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(arg -> newPassword);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataSourceFactory.resetHost(arg -> newHost);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory.resetPort(arg -> newPort);
        assertEquals(3307, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(arg -> newSchema);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newKey", "newValue");
        dataSourceFactory.resetParameters(arg -> newParameters);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetUsernameFromConnectionConfig() {
        String username = OBConsoleDataSourceFactory.getUsername(mockedConnectionConfig);
        assertEquals("user", username);
    }

    @Test
    public void testGetPasswordFromConnectionConfig() {
        String password = OBConsoleDataSourceFactory.getPassword(mockedConnectionConfig);
        assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParamsFromConnectionConfig() {
        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(mockedConnectionConfig);
        assertEquals("trustStore=path&trustStorePassword=password", jdbcUrlParams.toString());
    }

}