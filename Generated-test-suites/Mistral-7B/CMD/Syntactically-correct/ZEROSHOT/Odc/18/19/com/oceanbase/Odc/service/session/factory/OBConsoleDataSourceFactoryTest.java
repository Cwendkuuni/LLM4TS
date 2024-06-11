package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceFactory.ValueMapper;
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
 * @date 2023-03-31 10:09
 * @since ODC_release_3.2.2
 */
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private ConnectionMapper connectionMapper;

    private DataSourceFactory factory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.factory = new OBConsoleDataSourceFactory(createConnectionConfig(), false, true);
        stubConnectionExtensionPoint();
    }

    /**
     * Test getDataSource method
     */
    @Test
    public void testGetDataSource() {
        SingleConnectionDataSource dataSource = this.factory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals(this.factory.getUsername(), dataSource.getUsername());
        assertEquals(this.factory.getPassword(), dataSource.getPassword());
        assertEquals(this.factory.getConnectType(), dataSource.getConnectType());
        assertEquals(this.factory.getDialectType(), dataSource.getDialectType());
        assertEquals(this.factory.getJdbcUrl(), dataSource.getUrl());
        assertEquals(this.factory.getAutoCommit(), dataSource.getAutoCommit());
        verify(this.connectionExtensionPoint, times(1)).generateJdbcUrl(this.factory.getHost(), this.factory.getPort(),
                this.factory.getDefaultSchema(), this.factory.getParameters());
    }

    /**
     * Test deepCopy method
     */
    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clonedFactory = this.factory.deepCopy();
        assertEquals(this.factory.getUsername(), clonedFactory.getUsername());
        assertEquals(this.factory.getPassword(), clonedFactory.getPassword());
        assertEquals(this.factory.getHost(), clonedFactory.getHost());
        assertEquals(this.factory.getPort(), clonedFactory.getPort());
        assertEquals(this.factory.getDefaultSchema(), clonedFactory.getDefaultSchema());
        assertEquals(this.factory.getParameters(), clonedFactory.getParameters());
        assertEquals(this.factory.getConnectionConfig(), clonedFactory.getConnectionConfig());
        assertEquals(this.factory.getAutoCommit(), clonedFactory.getAutoCommit());
        assertEquals(this.factory.getInitConnection(), clonedFactory.getInitConnection());
        assertEquals(this.factory.getEventPublisher(), clonedFactory.getEventPublisher());
        assertNotSame(this.factory, clonedFactory);
    }

    /**
     * Test resetUsername method
     */
    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        ValueMapper<String> mapper = mock(ValueMapper.class);
        this.factory.resetUsername(mapper);
        assertEquals(newUsername, this.factory.getUsername());
        verify(mapper, times(1)).map(this.factory.getUsername());
    }

    /**
     * Test resetPassword method
     */
    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        ValueMapper<String> mapper = mock(ValueMapper.class);
        this.factory.resetPassword(mapper);
        assertEquals(newPassword, this.factory.getPassword());
        verify(mapper, times(1)).map(this.factory.getPassword());
    }

    /**
     * Test resetHost method
     */
    @Test
    public void testResetHost() {
        String newHost = "newHost";
        ValueMapper<String> mapper = mock(ValueMapper.class);
        this.factory.resetHost(mapper);
        assertEquals(newHost, this.factory.getHost());
        verify(mapper, times(1)).map(this.factory.getHost());
    }

    /**
     * Test resetPort method
     */
    @Test
    public void testResetPort() {
        int newPort = 1234;
        ValueMapper<Integer> mapper = mock(ValueMapper.class);
        this.factory.resetPort(mapper);
        assertEquals(newPort, this.factory.getPort());
        verify(mapper, times(1)).map(this.factory.getPort());
    }

    /**
     * Test resetSchema method
     */
    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        ValueMapper<String> mapper = mock(ValueMapper.class);
        this.factory.resetSchema(mapper);
        assertEquals(newSchema, this.factory.getDefaultSchema());
        verify(mapper, times(1)).map(this.factory.getDefaultSchema());
    }

    /**
     * Test resetParameters method
     */
    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key1", "value1");
        ValueMapper<Map<String, String>> mapper = mock(ValueMapper.class);
        this.factory.resetParameters(mapper);
        assertEquals(newParameters, this.factory.getParameters());
        verify(mapper, times(1)).map(this.factory.getParameters());
    }

    /**
     * Test getJdbcUrl method
     */
    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:"));
        assertTrue(jdbcUrl.endsWith(";"));
    }

    /**
     * Test getUsername method
     */
    @Test
    public void testGetUsername() {
        String username = this.factory.getUsername();
        assertNotNull(username);
        if (connectionSessionUtil != null) {
            String dbUser = ConnectionSessionUtil.getUserOrSchemaString(username, DialectType.OB_ORACLE);
            assertEquals(dbUser, this.factory.getUsername());
        }
    }

    /**
     * Test getPassword method
     */
    @Test
    public void testGetPassword() {
        String password = this.factory.getPassword();
        assertNotNull(password);
    }

    /**
     * Test getDefaultSchema method
     */
    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = this.factory.getDefaultSchema();
        assertNotNull(defaultSchema);
    }

    /**
     * Test getParameters method
     */
    @Test
    public void testGetParameters() {
        Map<String, String> parameters = this.factory.getParameters();
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
        for (String key : parameters.keySet()) {
            String value = parameters.get(key);
            assertNotNull(value);
        }
    }

    /**
     * Test getConnectType method
     */
    @Test
    public void testGetConnectType() {
        ConnectType connectType = this.factory.getConnectType();
        assertNotNull(connectType);
    }

    /**
     * Test getDialectType method
     */
    @Test
    public void testGetDialectType() {
        DialectType dialectType = this.factory.getDialectType();
        assertNotNull(dialectType);
    }

    /**
     * Test stub connectionExtensionPoint
     */
    private void stubConnectionExtensionPoint() {
        when(this.connectionExtensionPoint.generateJdbcUrl(eq(this.factory.getHost()), eq(this.factory.getPort()),
                eq(this.factory.getDefaultSchema()), eq(this.factory.getParameters())))
                .thenReturn(this.factory.getJdbcUrl());
        when(this.connectionExtensionPoint.getDriverClassName()).thenReturn("com.obclient.jdbc.OceanBaseDriver");
        when(this.connectionExtensionPoint.getConnectionInitializers()).thenReturn(asList(this.connectionInitializer));
        when(this.connectionExtensionPoint.getConnectionInitializers().size()).thenReturn(1);
        when(this.connectionInitializer.initializeDatabase()).thenReturn(true);

        when(this.connectionExtensionPoint.getConnectionInitializers().get(0)).thenReturn(this.connectionInitializer);
        when(this.connectionExtensionPoint.getConnectionInitializers().get(1)).thenReturn(null);
        when(this.connectionExtensionPoint.getConnectionMapper()).thenReturn(this.connectionMapper);
        when(this.connectionPluginUtil.getConnectionExtension(eq(DialectType.OB_ORACLE)))
                .thenReturn(this.connectionExtensionPoint);
    }

    /**
     * Test createConnectionConfig
     *
     * @return ConnectionConfig
     */
    private ConnectionConfig createConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1527);
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        return connectionConfig;
    }
}