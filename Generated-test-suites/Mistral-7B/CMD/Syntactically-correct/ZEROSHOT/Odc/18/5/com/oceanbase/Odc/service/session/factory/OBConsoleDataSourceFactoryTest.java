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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE)).thenReturn(connectionExtensionPoint);
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), DialectType.OB_ORACLE)).thenReturn("test_user");

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.OB_ORACLE);
        config.setHost("localhost");
        config.setPort(1527);
        config.setUsername("test_user");
        config.setPassword("test_password");
        config.setDefaultSchema("test_schema");
        config.setType(ConnectType.LONG);
        config.setJdbcUrlParameters(new HashMap<>());
        config.setAutoCommit(true);
        config.setSslConfig(new SSLConfig(true, null));
        when(connectionPluginUtil.getConnectionConfig(any())).thenReturn(config);

        this.factory = new OBConsoleDataSourceFactory(config, true, true);
    }

    @After
    public void tearDown() throws Exception {
        factory = null;
        Mockito.verifyNoMoreInteractions(connectionExtensionPoint, connectionInitializer, eventPublisher, connectionSessionUtil, sessionCreatedInitializer);
    }

    @Test
    public void testGetDataSource() throws Exception {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);

        assertEquals(factory.getUsername(), dataSource.getUsername());
        assertEquals(factory.getPassword(), dataSource.getPassword());
        assertEquals(factory.getConnectType(), dataSource.getConnection().createStatement().getConnection().getMetaData().getUserName());
        assertEquals(factory.getDialectType(), ((SingleConnectionDataSource) dataSource).getConnection().getMetaData().getDatabaseProductName());

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(factory.getUsername(), connection.createStatement().getUsername());
        assertEquals(factory.getPassword(), connection.createStatement().getPassword());

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals(jdbcUrl, dataSource.getUrl());

        Properties connectionProperties = dataSource.getConnectionProperties();
        assertNotNull(connectionProperties);
        assertEquals(factory.getAutoCommit(), connectionProperties.getProperty("autoCommit"));
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String expectedJdbcUrl = "jdbc:obnew:oracle:thin:@localhost:1527:test_schema@test_user:test_password";
        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "test_user";
        String actualUsername = factory.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "test_password";
        String actualPassword = factory.getPassword();
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDefaultSchema() {
        String expectedSchema = "test_schema";
        String actualSchema = factory.getDefaultSchema();
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("maxAllowedPacket", "64000000");
        expectedJdbcParams.put("allowMultiQueries", "true");
        expectedJdbcParams.put("connectTimeout", "5000");
        expectedJdbcParams.put("zeroDateTimeBehavior", "0");
        expectedJdbcParams.put("noDatetimeStringSync", "true");
        expectedJdbcParams.put("jdbcCompliantTruncation", "false");
        expectedJdbcParams.put("sendConnectionAttributes", "false");

        Map<String, String> actualJdbcParams = factory.getJdbcParams();
        assertEquals(expectedJdbcParams, actualJdbcParams);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory originalFactory = this.factory;
        OBConsoleDataSourceFactory clonedFactory = originalFactory.deepCopy();

        assertNotSame(originalFactory, clonedFactory);
        assertEquals(originalFactory.getUsername(), clonedFactory.getUsername());
        assertEquals(originalFactory.getPassword(), clonedFactory.getPassword());
        assertEquals(originalFactory.getHost(), clonedFactory.getHost());
        assertEquals(originalFactory.getPort(), clonedFactory.getPort());
        assertEquals(originalFactory.getDefaultSchema(), clonedFactory.getDefaultSchema());
        assertEquals(originalFactory.getParameters(), clonedFactory.getParameters());
        assertEquals(originalFactory.getConnectionConfig(), clonedFactory.getConnectionConfig());
        assertEquals(originalFactory.getAutoCommit(), clonedFactory.getAutoCommit());
        assertEquals(originalFactory.getInitConnection(), clonedFactory.getInitConnection());
        assertEquals(originalFactory.getEventPublisher(), clonedFactory.getEventPublisher());
        assertEquals(originalFactory.getConnectionExtensionPoint(), clonedFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() throws Exception {
        String newUsername = "new_username";
        factory.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newUsername;
            }
        });
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() throws Exception {
        String newPassword = "new_password";
        factory.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newPassword;
            }
        });
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        String newHost = "new_host";
        factory.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newHost;
            }
        });
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() throws Exception {
        int newPort = 1528;
        factory.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return newPort;
            }
        });
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() throws Exception {
        String newSchema = "new_schema";
        factory.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newSchema;
            }
        });
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("new_param1", "new_value1");
        newParameters.put("new_param2", "new_value2");

        factory.resetParameters(new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                return newParameters;
            }
        });

        assertEquals(newParameters, factory.getParameters());
    }
}

class ValueMapper<T> {
    abstract T map(T value);
}