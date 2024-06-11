package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.constant.OdcConstants;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourcePoolManager;
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

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;
    private DataSourcePoolManager dataSourcePoolManager;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setAutoCommit(false);
        connectionConfig.setInitConnection(true);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.getSslConfig().setEnabled(true);
        connectionConfig.setSslFileEntry(new SSLFileEntry());

        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        dataSourcePoolManager = mock(DataSourcePoolManager.class);
        eventPublisher = mock(EventPublisher.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:mock:url");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getName()).thenReturn("testPlugin");
        when(connectionExtensionPoint.getConnectionInitializer(any(DialectType.class)))
                .thenReturn(connectionInitializer);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertTrue(StringUtils.isNotBlank(jdbcUrl));
        assertEquals("jdbc:mock:url", jdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyUsername() {
        connectionConfig.setUsername(StringUtils.EMPTY);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyPassword() {
        connectionConfig.setPassword(StringUtils.EMPTY);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullHost() {
        connectionConfig.setHost(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        connectionConfig.setPort(-1);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDefaultSchema() {
        connectionConfig.setDefaultSchema(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidDialectType() {
        connectionConfig.setDialectType(DialectType.INVALID);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = RuntimeException.class)
    public void testGetDataSourceWithNegativeAutoCommit() {
        connectionConfig.setAutoCommit(-1);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.getDataSource();
        fail("RuntimeException expected");
    }

    @Test
    public void testResetUsername() {
        String newUsername = "testUsername";
        dataSourceFactory.resetUsername((value) -> newUsername);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "testPassword";
        dataSourceFactory.resetPassword((value) -> newPassword);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "testHost";
        dataSourceFactory.resetHost((value) -> newHost);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        dataSourceFactory.resetPort((value) -> newPort);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "testSchema";
        dataSourceFactory.resetSchema((value) -> newSchema);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters((value) -> newParameters);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testDeepCopy() throws CloneNotSupportedException {
        CloneableDataSourceFactory cloneableDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotNull(cloneableDataSourceFactory);
        assertNotNull(cloneableDataSourceFactory.getDataSource());
        assertNotSame(dataSourceFactory, cloneableDataSourceFactory);
    }

    @Test
    public void testCloneableDataSourceFactoryMethodCalls() {
        GenericObjectPoolConfig objectPoolConfig = new GenericObjectPoolConfig();
        Properties connectionProperties = new Properties();

        dataSourceFactory.resetUsername((value) -> "newUsername");
        dataSourceFactory.resetPassword((value) -> "newPassword");
        dataSourceFactory.resetHost((value) -> "newHost");
        dataSourceFactory.resetPort((value) -> 1234);
        dataSourceFactory.resetSchema((value) -> "newSchema");
        dataSourceFactory.resetParameters((value) -> connectionProperties);

        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();

        VerifyClonedValues.verifyValues(dataSourceFactory, clonedDataSourceFactory);

        Connection connection = clonedDataSourceFactory.getDataSource().getConnection();
        connection.close();
    }

    private static class VerifyClonedValues {

        private static void verifyValues(CloneableDataSourceFactory dataSourceFactory1,
                CloneableDataSourceFactory dataSourceFactory2) {
            Validate.isTrue(Objects.equals(dataSourceFactory1.getUsername(),
                    dataSourceFactory2.getUsername()), "username not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getPassword(),
                    dataSourceFactory2.getPassword()), "password not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getHost(),
                    dataSourceFactory2.getHost()), "host not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getPort(),
                    dataSourceFactory2.getPort()), "port not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getDefaultSchema(),
                    dataSourceFactory2.getDefaultSchema()), "defaultSchema not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getParameters(),
                    dataSourceFactory2.getParameters()), "parameters not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getDialectType(),
                    dataSourceFactory2.getDialectType()), "dialectType not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getAutoCommit(),
                    dataSourceFactory2.getAutoCommit()), "autoCommit not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getInitConnection(),
                    dataSourceFactory2.getInitConnection()), "initConnection not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getEventPublisher(),
                    dataSourceFactory2.getEventPublisher()), "eventPublisher not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getConnectionExtensionPoint(),
                    dataSourceFactory2.getConnectionExtensionPoint()),
                    "connectionExtensionPoint not equal");
            Validate.isTrue(Objects.equals(dataSourceFactory1.getConnectionPoolManager(),
                    dataSourceFactory2.getConnectionPoolManager()),
                    "connectionPoolManager not equal");
        }

    }

}