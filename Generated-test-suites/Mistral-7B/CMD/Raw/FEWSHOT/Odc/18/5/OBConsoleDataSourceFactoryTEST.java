package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_ORACLE;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_MYSQL;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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
 * Test class for {@link OBConsoleDataSourceFactory}
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class OBConsoleDataSourceFactoryTest {

    private static final String DIALECT_TYPE = OB_ORACLE.name();
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String HOST = "test_host";
    private static final int PORT = 1234;
    private static final String DEFAULT_SCHEMA = "test_schema";
    private static final Map<String, String> PARAMETERS = new HashMap<>();
    private static final ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig(new OBTenantEndpoint(), DIALECT_TYPE, null, null, null, null, null, USERNAME, PASSWORD, PORT, DEFAULT_SCHEMA, PARAMETERS, null, null);
    private static final ConnectionExtensionPoint CONNECTION_EXTENSION_POINT = mock(ConnectionExtensionPoint.class);
    private static final EventPublisher EVENT_PUBLISHER = mock(EventPublisher.class);

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        when(CONNECTION_EXTENSION_POINT.getDriverClassName()).thenReturn("com.oceanbase.jdbc.driver.OceanBaseDriver");
        when(CONNECTION_EXTENSION_POINT.generateJdbcUrl(HOST, PORT, DEFAULT_SCHEMA, PARAMETERS)).thenReturn("jdbc:ob://localhost:1234/test_schema");
        when(CONNECTION_EXTENSION_POINT.getConnectionInitializers()).thenReturn(Collections.emptyList());
        when(CONNECTION_EXTENSION_POINT.getConnectionInitializer(Mockito.eq(OBConsoleDataSourceFactory.class))).thenReturn(null);
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructor() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        assertEquals(CONNECTION_CONFIG, dataSourceFactory.getConnectionConfig());
        assertEquals(true, dataSourceFactory.isAutoCommit());
        assertEquals(true, dataSourceFactory.isInitConnection());
        assertNull(dataSourceFactory.getEventPublisher());
        assertNull(dataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructorWithEventPublisher() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true, true);
        assertEquals(CONNECTION_CONFIG, dataSourceFactory.getConnectionConfig());
        assertEquals(true, dataSourceFactory.isAutoCommit());
        assertEquals(true, dataSourceFactory.isInitConnection());
        assertEquals(EVENT_PUBLISHER, dataSourceFactory.getEventPublisher());
        assertEquals(CONNECTION_EXTENSION_POINT, dataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testGetJdbcUrl() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        assertEquals("jdbc:ob://localhost:1234/test_schema", dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        assertEquals(USERNAME, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        assertEquals(PASSWORD, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        assertEquals(DIALECT_TYPE, dataSourceFactory.getDialectType().name());
    }

    @Test
    public void testGetDataSource() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertSame(SingleConnectionDataSource.class, dataSource.getClass());
        assertEquals("com.oceanbase.jdbc.driver.OceanBaseDriver", dataSource.getDriverClassName());
        assertEquals("jdbc:ob://localhost:1234/test_schema", dataSource.getUrl());
        assertEquals(USERNAME, dataSource.getUsername());
        assertEquals(PASSWORD, dataSource.getPassword());
        assertEquals("true", dataSource.getProperties().getProperty("useSSL"));
        assertEquals("false", dataSource.getProperties().getProperty("zeroDateTimeBehavior"));
        assertNull(dataSource.getConnectionProperties().getProperty("maxAllowedPacket"));
        assertNull(dataSource.getConnectionProperties().getProperty("allowLoadLocalInfile"));
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        String newUsername = "new_username";
        dataSourceFactory.resetUsername((valueMapper) -> newUsername);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        String newPassword = "new_password";
        dataSourceFactory.resetPassword((valueMapper) -> newPassword);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        String newHost = "new_host";
        dataSourceFactory.resetHost((valueMapper) -> newHost);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        int newPort = 5678;
        dataSourceFactory.resetPort((valueMapper) -> newPort);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        String newSchema = "new_schema";
        dataSourceFactory.resetSchema((valueMapper) -> newSchema);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetParameters((valueMapper) -> newParameters);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetDataSourceWithInitConnectionTrue() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
        assertEquals("com.oceanbase.jdbc.driver.OceanBaseDriver", dataSource.getDriverClassName());
        assertEquals("jdbc:ob://localhost:1234/test_schema", dataSource.getUrl());
        assertEquals(USERNAME, dataSource.getUsername());
        assertEquals(PASSWORD, dataSource.getPassword());
        DataSource initializedDataSource = dataSourceFactory.getDataSource();
        assertEquals(SingleConnectionDataSource.class, initializedDataSource.getClass());
        assertEquals("com.oceanbase.jdbc.driver.OceanBaseDriver", initializedDataSource.getDriverClassName());
        assertEquals("jdbc:ob://localhost:1234/test_schema", initializedDataSource.getUrl());
        assertEquals(USERNAME, initializedDataSource.getUsername());
        assertEquals(PASSWORD, initializedDataSource.getPassword());
    }

    @Test
    public void testGetDataSourceWithInitConnectionFalse() {
        dataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true, false);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
        assertEquals("com.oceanbase.jdbc.driver.OceanBaseDriver", dataSource.getDriverClassName());
        assertEquals("jdbc:ob://localhost:1234/test_schema", dataSource.getUrl());
        assertEquals(USERNAME, dataSource.getUsername());
        assertEquals(PASSWORD, dataSource.getPassword());
        assertFalse(dataSource.isAutoCommit());
        assertNull(dataSource.getConnectionProperties());
    }

    @Test
    public void testDeepCopyWithSameValues() {
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = originalDataSourceFactory.deepCopy(ConnectionMapper.INSTANCE);
        assertEquals(originalDataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        assertEquals(originalDataSourceFactory.isAutoCommit(), clonedDataSourceFactory.isAutoCommit());
        assertEquals(originalDataSourceFactory.isInitConnection(), clonedDataSourceFactory.isInitConnection());
        assertEquals(originalDataSourceFactory.getEventPublisher(), clonedDataSourceFactory.getEventPublisher());
        assertEquals(originalDataSourceFactory.getConnectionExtensionPoint(), clonedDataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testDeepCopyWithDifferentValues() {
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(CONNECTION_CONFIG, true);
        ConnectionConfig clonedConnectionConfig = new ConnectionConfig(new OBTenantEndpoint(), DIALECT_TYPE, null, null, null, null, null, USERNAME, PASSWORD, PORT, DEFAULT_SCHEMA, PARAMETERS, null, null);
        OBConsoleDataSourceFactory clonedDataSourceFactory = new OBConsoleDataSourceFactory(clonedConnectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactoryWithDeepCopy = originalDataSourceFactory.deepCopy(ConnectionMapper.INSTANCE);
        assertEquals(originalDataSourceFactory.getConnectionConfig(), clonedDataSourceFactoryWithDeepCopy.getConnectionConfig());
        assertEquals(originalDataSourceFactory.isAutoCommit(), clonedDataSourceFactoryWithDeepCopy.isAutoCommit());
        assertEquals(originalDataSourceFactory.isInitConnection(), clonedDataSourceFactoryWithDeepCopy.isInitConnection());
        assertNull(clonedDataSourceFactoryWithDeepCopy.getEventPublisher());
        assertNull(clonedDataSourceFactoryWithDeepCopy.getConnectionExtensionPoint());
        assertSame(clonedDataSourceFactory.getConnectionConfig(), clonedDataSourceFactoryWithDeepCopy.getConnectionConfig());
        assertNotSame(clonedDataSourceFactory, clonedDataSourceFactoryWithDeepCopy);
    }

}