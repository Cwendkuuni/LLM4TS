package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isNotBlank;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.plugin.ConnectionExtensionPoint;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPointImpl;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

/**
 * Test class for {@link OBConsoleDataSourceFactory}
 */
public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testNewOBConsoleDataSourceFactory() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenAnswer(i -> "jdbc:odps://localhost:1234/testSchema");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertEquals(connectionConfig, factory.getConnectionConfig());
        assertEquals(true, factory.isAutoCommit());
        assertTrue(factory.isInitConnection());
        assertEquals("testUser", factory.getUsername());
        assertEquals("localhost", factory.getHost());
        assertEquals(1234, factory.getPort());
        assertEquals("testSchema", factory.getDefaultSchema());
        assertSame(connectionExtensionPoint, factory.getConnectionExtensionPoint());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPointImpl();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenAnswer(i -> "jdbc:odps://localhost:1234/testSchema");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource dataSourceFromFactory = factory.getDataSource();
        assertEquals(dataSource, dataSourceFromFactory);
        assertNotNull(dataSourceFromFactory.getConnection());
        assertEquals("jdbc:odps://localhost:1234/testSchema", dataSourceFromFactory.getConnection().getConnectionURL());
        assertEquals("testUser", dataSourceFromFactory.getConnection().getUsername());
        assertEquals("testPassword", dataSourceFromFactory.getConnection().getPassword());
        assertEquals("com.oceanbase.jdbc.Driver", dataSourceFromFactory.getConnection().getDriverClass());
        assertEquals("testSchema", dataSourceFromFactory.getConnection().getSchema());
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPointImpl();

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:odps://localhost:1234/testSchema", jdbcUrl);

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("testUser@testSchema", username);
    }

    @Test
    public void testClone() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPointImpl();

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();

        assertEquals(connectionConfig, clonedFactory.getConnectionConfig());
        assertFalse(Objects.equals(factory, clonedFactory));
    }

    @Test
    public void testResetValues() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        ConnectionExtensionPoint connectionExtensionPoint = new ConnectionExtensionPointImpl();

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        factory.resetUsername((value) -> "testUser2");
        assertEquals("testUser2", factory.getUsername());

        factory.resetPassword((value) -> "testPassword2");
        assertEquals("testPassword2", factory.getPassword());

        factory.resetHost((value) -> "localhost2");
        assertEquals("localhost2", factory.getHost());

        factory.resetPort((value) -> 1235);
        assertEquals(1235, factory.getPort());

        factory.resetSchema((value) -> "testSchema2");
        assertEquals("testSchema2", factory.getDefaultSchema());

        factory.resetParameters((value) -> new HashMap<>());
        assertTrue(factory.getParameters().isEmpty());
    }
}