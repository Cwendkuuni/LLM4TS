package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_ORACLE;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_MYSQL;
import static com.oceanbase.odc.service.connection.model.ConnectType.ORACLE;
import static com.oceanbase.odc.service.connection.model.DialectType.OB_ORACLE;
import static com.oceanbase.odc.service.connection.model.DialectType.OB_MYSQL;
import static com.oceanbase.odc.service.connection.model.DialectType.ORACLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionMapper;
import com.oceanbase.odc.core.datasource.ConnectionSessionUtil;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

/**
 * Test class for OBConsoleDataSourceFactory
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private OBConsoleDataSourceFactory underTest;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionPluginUtil.getConnectionExtension(OB_ORACLE)).thenReturn(connectionExtensionPoint);
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), OB_ORACLE)).thenReturn("user");
    }

    @Test
    public void testNewOBConsoleDataSourceFactory() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("username", underTest.getUsername());
        assertEquals("host", underTest.getHost());
        assertEquals(1234, underTest.getPort());
        assertEquals("schema", underTest.getDefaultSchema());
        assertEquals(OB_ORACLE, underTest.getDialectType());
    }

    @Test
    public void testNewOBConsoleDataSourceFactoryWithNullConnectionConfig() {
        underTest = new OBConsoleDataSourceFactory(null, true);
        assertNull(underTest.getUsername());
        assertNull(underTest.getHost());
        assertNull(underTest.getPort());
        assertNull(underTest.getDefaultSchema());
        assertNull(underTest.getDialectType());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);

        Properties properties = new Properties();
        properties.setProperty("allowLoadLocalInfile", "false");
        properties.setProperty("allowUrlInLocalInfile", "false");
        properties.setProperty("allowLoadLocalInfileInPath", "");
        properties.setProperty("autoDeserialize", "false");

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setUrl(underTest.getJdbcUrl());
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setDriverClassName(connectionExtensionPoint.getDriverClassName());
        dataSource.setConnectionProperties(properties);
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), OB_ORACLE)).thenReturn("user");
        when(connectionPluginUtil.getDriverClassName(OB_ORACLE)).thenReturn("driverClassName");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Arrays.asList(sessionCreatedInitializer));

        DataSource expectedDataSource = new DriverManagerDataSource("driverClassName", "username", "password");
        expectedDataSource.setUrl(underTest.getJdbcUrl());
        DataSource actualDataSource = underTest.getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;

        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory copy = original.deepCopy();

        assertSame(original, copy);
        assertEquals(original.getUsername(), copy.getUsername());
        assertEquals(original.getHost(), copy.getHost());
        assertEquals(original.getPort(), copy.getPort());
        assertEquals(original.getDefaultSchema(), copy.getDefaultSchema());
        assertEquals(original.getDialectType(), copy.getDialectType());
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        connectionConfig.getJdbcUrlParameters().put("key", "value");
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbcUrl";
        when(connectionExtensionPoint.generateJdbcUrl(eq("host"), anyInt(), eq("schema"), anyMap()))
                .thenReturn(expectedJdbcUrl);

        String actualJdbcUrl = underTest.getJdbcUrl();

        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);

        String expectedUsername = "username";
        String actualUsername = underTest.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);

        String expectedPassword = "password";
        String actualPassword = underTest.getPassword();

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(OB_ORACLE);
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setHost("host");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(1234);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setUsername("username");
        connectionConfig.setType(OB_ORACLE);
        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);

        DialectType expectedDialectType = OB_ORACLE;
        DialectType actualDialectType = underTest.getDialectType();

        assertEquals(expectedDialectType, actualDialectType);
    }
}