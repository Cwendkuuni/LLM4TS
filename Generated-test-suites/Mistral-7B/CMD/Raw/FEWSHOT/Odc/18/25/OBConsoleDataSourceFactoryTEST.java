package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionMapper;
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
 * Test class for OBConsoleDataSourceFactory
 *
 * @author yh263208
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionMapper connectionMapper;
    private EventPublisher eventPublisher;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        connectionMapper = ConnectionMapper.INSTANCE;
        eventPublisher = mock(EventPublisher.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.READ_WRITE);
        connectionConfig.setAutoCommit(Boolean.TRUE);
        connectionConfig.setInitConnection(Boolean.TRUE);
        connectionConfig.setEndpoint(null);
        connectionConfig.setSslConfig(new SSLConfig(Boolean.TRUE));
        connectionConfig.setSslFileEntry(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, Boolean.TRUE);
    }

    @After
    public void teardown() {
        reset(connectionExtensionPoint, connectionMapper, eventPublisher);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getDriverClassName());
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("localhost:1521/testUser@testTenantName#testClusterName", dataSource.getConnectionProperties()
                .getProperty("url"));
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:oracle:thin:@localhost:1521:testUser@testTenantName#testClusterName", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory cloneableDataSourceFactory = dataSourceFactory.deepCopy();
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        cloneableDataSourceFactory.resetUsername(mapper);
        cloneableDataSourceFactory.resetPassword(mapper);
        cloneableDataSourceFactory.resetHost(mapper);
        cloneableDataSourceFactory.resetPort(mapper);
        cloneableDataSourceFactory.resetSchema(mapper);
        cloneableDataSourceFactory.resetParameters(mapper);
        assertEquals("testUser", cloneableDataSourceFactory.getUsername());
        assertEquals("testPassword", cloneableDataSourceFactory.getPassword());
        assertEquals("localhost:1521", cloneableDataSourceFactory.getHost());
        assertEquals(1521, cloneableDataSourceFactory.getPort());
        assertEquals(null, cloneableDataSourceFactory.getDefaultSchema());
        assertEquals(null, cloneableDataSourceFactory.getParameters());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetUsername(mapper);
        verify(mapper).map("testUser");
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPassword(mapper);
        verify(mapper).map("testPassword");
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetHost(mapper);
        verify(mapper).map("localhost");
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPort(mapper);
        verify(mapper).map(1521);
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetSchema(mapper);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetParameters(mapper);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("testUser", getUsername(connectionConfig));
        assertEquals("testSchema", getDefaultSchema(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = dataSourceFactory.getJdbcParams();
        assertEquals(10, params.size());
        assertEquals("64000000", params.get("maxAllowedPacket"));
        assertEquals("true", params.get("allowMultiQueries"));
        assertEquals("5000", params.get("connectTimeout"));
        assertEquals("0", params.get("zeroDateTimeBehavior"));
        assertEquals("true", params.get("noDatetimeStringSync"));
        assertEquals("false", params.get("jdbcCompliantTruncation"));
        assertEquals("false", params.get("sendConnectionAttributes"));
        assertNull(params.get("socksProxyHost"));
        assertNull(params.get("socksProxyPort"));
        assertNull(params.get("trustStore"));
        assertNull(params.get("keyStore"));
        assertNull(params.get("keyStorePassword"));
    }

    @Test
    public void testInitConnection() throws SQLException {
        List<ConnectionInitializer> initializers = mock(List.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(initializers);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSource.getConnection()).thenReturn(DriverManager.getConnection(dataSourceFactory.getJdbcUrl()));
        dataSourceFactory.setEventPublisher(eventPublisher);
        dataSourceFactory.initConnection();
        verify(dataSource).addInitializer(any());
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    public void testNoInitConnection() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNull(dataSource.getConnection());
    }

    private static String getUsername(@NonNull ConnectionConfig connectionConfig) {
        return OBConsoleDataSourceFactory.getUsername(connectionConfig);
    }

    private static String getDefaultSchema(@NonNull ConnectionConfig connectionConfig) {
        return OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
    }
}