package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isNotBlank;
import static org.junit.Assert.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private Properties jdbcUrlParameters;
    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        jdbcUrlParameters = new Properties();
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:ob:mysql://host:port/defaultSchema?parameters";
        Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq("host"), Mockito.eq(port), Mockito.eq("defaultSchema"), Mockito.eq(jdbcUrlParameters)))
                .thenReturn(expectedJdbcUrl);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();

        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "username";
        String dbUser = "dbUser";

        Mockito.when(connectionConfig.getUsername()).thenReturn(dbUser);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualUsername = dataSourceFactory.getUsername();

        assertEquals(getExpectedUsername(dbUser), actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";

        Mockito.when(connectionConfig.getPassword()).thenReturn(expectedPassword);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualPassword = dataSourceFactory.getPassword();

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        String expectedJdbcUrlParameterKey = "key";
        String expectedJdbcUrlParameterValue = "value";
        String expectedJdbcUrlParameterValue2 = "value2";
        Map<String, String> expectedJdbcUrlParameters = new HashMap<>();
        expectedJdbcUrlParameters.put(expectedJdbcUrlParameterKey, expectedJdbcUrlParameterValue);
        expectedJdbcUrlParameters.put(expectedJdbcUrlParameterKey, expectedJdbcUrlParameterValue2);

        Mockito.when(connectionConfig.getJdbcUrlParameters()).thenReturn(jdbcUrlParameters);
        jdbcUrlParameters.put(expectedJdbcUrlParameterKey, expectedJdbcUrlParameterValue);
        jdbcUrlParameters.put(expectedJdbcUrlParameterKey, expectedJdbcUrlParameterValue2);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> actualJdbcUrlParameters = dataSourceFactory.getJdbcParams();

        assertEquals(expectedJdbcUrlParameters, actualJdbcUrlParameters);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        String expectedJdbcUrl = "jdbc:ob:mysql://host:port/defaultSchema?parameters";
        SingleConnectionDataSource expectedDataSource = Mockito.mock(SingleConnectionDataSource.class);
        DataSource dataSource = Mockito.mock(DataSource.class);

        Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq("host"), Mockito.eq(port), Mockito.eq("defaultSchema"), Mockito.eq(jdbcUrlParameters)))
                .thenReturn(expectedJdbcUrl);
        Mockito.when(connectionExtensionPoint.getDriverClassName()).thenReturn("driverClassName");
        Mockito.when(dataSource.setEventPublisher(eventPublisher)).thenReturn(dataSource);
        Mockito.when(dataSource.setUrl(Mockito.eq(expectedJdbcUrl))).thenReturn(dataSource);
        Mockito.when(dataSource.setUsername(Mockito.eq("username"))).thenReturn(dataSource);
        Mockito.when(dataSource.setPassword(Mockito.eq("password"))).thenReturn(dataSource);
        Mockito.when(dataSource.setDriverClassName(Mockito.eq("driverClassName"))).thenReturn(dataSource);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource actualDataSource = dataSourceFactory.getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testCloneableDataSourceFactory() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        assertNotSame(dataSourceFactory, clonedDataSourceFactory);
        assertEquals(connectionConfig, clonedDataSourceFactory.getConnectionConfig());
        assertEquals(false, clonedDataSourceFactory.isInitConnection());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetUsername(mapper);
        String actualUsername = dataSourceFactory.getUsername();

        assertEquals(newUsername, actualUsername);
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetPassword(mapper);
        String actualPassword = dataSourceFactory.getPassword();

        assertEquals(newPassword, actualPassword);
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetHost(mapper);
        String actualHost = dataSourceFactory.getHost();

        assertEquals(newHost, actualHost);
    }

    @Test
    public void testResetPort() {
        int newPort = 123;
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetPort(mapper);
        int actualPort = dataSourceFactory.getPort();

        assertEquals(newPort, actualPort);
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetSchema(mapper);
        String actualSchema = dataSourceFactory.getDefaultSchema();

        assertEquals(newSchema, actualSchema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        dataSourceFactory.resetParameters(mapper);
        Map<String, String> actualParameters = dataSourceFactory.getParameters();

        assertEquals(newParameters, actualParameters);
    }

    @Test
    public void testInitializeConnection() {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        List<ConnectionInitializer> initializers = Mockito.mock(List.class);

        Mockito.when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(initializers);
        Mockito.when(initializers.add(Mockito.eq(connectionInitializer))).thenReturn(initializers);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSourceFactory.initializeConnection();

        Mockito.verify(initializers).add(connectionInitializer);
    }

    private String getExpectedUsername(String dbUser) {
        String username = dbUser;
        if (DialectType.OB_ORACLE.equals(connectionConfig.getDialectType())) {
            username = "\"" + username + "\"";
        }
        if (isNotBlank(connectionConfig.getOBTenantName())) {
            username += "@" + connectionConfig.getOBTenantName();
        } else if (isNotBlank(connectionConfig.getTenantName())) {
            username += "@" + connectionConfig.getTenantName();
        }
        if (isNotBlank(connectionConfig.getClusterName())) {
            username += "#" + connectionConfig.getClusterName();
        }
        return username;
    }
}