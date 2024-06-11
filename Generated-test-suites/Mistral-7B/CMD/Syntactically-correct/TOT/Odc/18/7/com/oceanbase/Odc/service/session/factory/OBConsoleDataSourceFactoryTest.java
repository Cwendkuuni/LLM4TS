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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(63306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig());
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(connectionInitializer));
        when(connectionExtensionPoint.generateJdbcUrl(connectionConfig.getHost(), connectionConfig.getPort(),
                connectionConfig.getDefaultSchema(), connectionConfig.getJdbcUrlParameters())).thenReturn("jdbcUrl");
    }

    @After
    public void tearDown() throws Exception {
        DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        DriverManager.getConnection("jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1");
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource dataSource = spy(new BasicDataSource());
        when(dataSource.getConnection()).thenReturn(mock(Connection.class));
        when(dataSource.getEventPublisher()).thenReturn(eventPublisher);
        when(dataSource.setDriverClassName(anyString())).thenAnswer(i -> {
            try {
                Class.forName((String) i.getArguments()[0]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return null;
        });

        DataSource dataSourceFromFactory = factory.getDataSource();
        assertEquals("jdbcUrl", dataSourceFromFactory.getConnection().getConnectionURL());
        verify(dataSourceFromFactory).setUsername(connectionConfig.getUsername());
        verify(dataSourceFromFactory).setPassword(connectionConfig.getPassword());
        verify(dataSourceFromFactory).setDriverClassName(connectionExtensionPoint.getDriverClassName());
        verify(dataSourceFromFactory).addInitializer(connectionInitializer);
        verify(dataSourceFromFactory).addInitializer(new SessionCreatedInitializer(connectionConfig));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyUsername() {
        connectionConfig.setUsername(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyPassword() {
        connectionConfig.setPassword(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyHost() {
        connectionConfig.setHost(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        connectionConfig.setPort(-1);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidDialectType() {
        connectionConfig.setDialectType(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithInvalidConnectType() {
        connectionConfig.setType(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.getDataSource();
    }

    @Test
    public void testGetDataSourceWithEmptyJdbcUrlParameters() {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        Properties properties = dataSource.getConnectionProperties();
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testGetDataSourceWithNonEmptyJdbcUrlParameters() {
        Properties jdbcUrlParameters = new Properties();
        jdbcUrlParameters.setProperty("property1", "value1");
        connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        Properties properties = dataSource.getConnectionProperties();
        Map<String, String> map = new HashMap<>(properties);
        assertEquals("value1", map.get("property1"));
    }
}