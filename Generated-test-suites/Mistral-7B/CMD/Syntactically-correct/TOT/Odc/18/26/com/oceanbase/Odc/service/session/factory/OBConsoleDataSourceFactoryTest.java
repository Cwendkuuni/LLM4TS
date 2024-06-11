package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getJdbcUrl(Mockito.eq(factory.getHost()), Mockito.eq(factory.getPort()),
                Mockito.eq(factory.getDefaultSchema()), Mockito.eq(factory.getParameters())))
                .thenReturn("jdbc:test:ds");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.test.driver.TestDriver");
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq(factory.getHost()), Mockito.eq(factory.getPort()),
                Mockito.eq(factory.getDefaultSchema()), Mockito.eq(factory.getParameters())))
                .thenAnswer(invocation -> invocation.callRealMethod());
    }

    @After
    public void tearDown() {
        reset(connectionExtensionPoint);
    }

    @Test
    public void testCreateDataSource() throws SQLException {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);

        DataSource ds = factory.getDataSource();
        assertEquals("jdbc:test:ds", ds.getConnection().getConnectionURL());
        assertEquals("testuser", ds.getConnection().getUserName());
        assertEquals("testpassword", ds.getConnection().getPassword());
        assertEquals("com.test.driver.TestDriver", ds.getConnection().getDriverClassName());
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = new ConnectionConfig();
        Map<String, String> params = factory.getJdbcParams(config);
        assertEquals(13, params.size());
        assertTrue(params.containsKey("maxAllowedPacket"));
        assertTrue(params.containsKey("allowMultiQueries"));
        assertTrue(params.containsKey("connectTimeout"));
        assertTrue(params.containsKey("zeroDateTimeBehavior"));
        assertTrue(params.containsKey("noDatetimeStringSync"));
        assertTrue(params.containsKey("jdbcCompliantTruncation"));
        assertTrue(params.containsKey("sendConnectionAttributes"));
        assertTrue(params.containsKey("socksProxyHost"));
        assertTrue(params.containsKey("socksProxyPort"));
        assertTrue(params.containsKey("useSSL"));
        assertTrue(params.containsKey("trustServerCertificate"));
        assertTrue(params.containsKey("trustStore"));
        assertTrue(params.containsKey("trustStorePassword"));
        assertTrue(params.containsKey("keyStore"));
        assertTrue(params.containsKey("keyStorePassword"));
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);
        config.setDefaultSchema("test_schema");
        config.setSslConfig(new SSLConfig(true, null, null));

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:test:ds@localhost:1234/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&socksProxyHost=&socksProxyPort=&useSSL=true&trustServerCertificate=true", jdbcUrl);
    }

    @Test
    public void testGetDataSourceWithInitConnectionTrue() throws SQLException {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);
        config.setDefaultSchema("test_schema");
        config.setSslConfig(new SSLConfig(true, null, null));
        config.setInitConnection(true);
        config.setEventPublisher(eventPublisher);

        DataSource ds = factory.getDataSource();
        verify(eventPublisher).publish(any(), eq(ConnectionCreatedEvent.class));
        verify(ds).addInitializer(any());

        Connection connection = ds.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername(null);
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("username is required", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword(null);
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("password is required", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidHost() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost(null);
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("host is required", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidPort() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(-1);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("port can not be negative", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidDefaultSchema() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);
        config.setDefaultSchema(null);

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("default schema is required", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidJdbcParams() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);
        config.setDefaultSchema("test_schema");
        config.setJdbcUrlParameters(new HashMap<String, String>() {{
            put("zeroDateTimeBehavior", null);
        }});

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("zeroDateTimeBehavior is required", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidSslConfig() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setType(ConnectType.OB_MYSQL);
        config.setDialectType(DialectType.OB_MYSQL);
        config.setDefaultSchema("test_schema");
        config.setSslConfig(new SSLConfig(null, null, null));

        try {
            factory.getDataSource();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("sslConfig is required", e.getMessage());
        }
    }

}