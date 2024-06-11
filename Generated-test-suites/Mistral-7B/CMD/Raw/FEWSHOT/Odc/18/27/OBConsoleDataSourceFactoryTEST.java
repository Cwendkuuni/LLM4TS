package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.SingleConnectionDataSource;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setEventPublisher(new EventPublisher());

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        DataSource dataSource = factory.getDataSource();
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("localhost", dataSource.getConnectionProperties().getProperty("url"));
        assertEquals("3306", dataSource.getConnectionProperties().getProperty("portNumber"));
        assertEquals("testSchema", factory.getUsername());
        assertEquals(DialectType.MYSQL, factory.getDialectType());

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testSchema", jdbcUrl);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setEventPublisher(new EventPublisher());

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testSchema", jdbcUrl);

        ConnectionConfig connectionConfigWithTenant = new ConnectionConfig();
        connectionConfigWithTenant.setUsername("testUser");
        connectionConfigWithTenant.setPassword("testPassword");
        connectionConfigWithTenant.setHost("localhost");
        connectionConfigWithTenant.setPort(3306);
        connectionConfigWithTenant.setDefaultSchema("testSchema");
        connectionConfigWithTenant.setDialectType(DialectType.MYSQL);
        connectionConfigWithTenant.setType(ConnectType.LONG);
        connectionConfigWithTenant.setEventPublisher(new EventPublisher());
        connectionConfigWithTenant.setTenantName("testTenant");

        OBConsoleDataSourceFactory factoryWithTenant = new OBConsoleDataSourceFactory(connectionConfigWithTenant, false, true);

        String jdbcUrlWithTenant = factoryWithTenant.getJdbcUrl();
        assertEquals("testUser@testTenant:testSchema@localhost:3306", jdbcUrlWithTenant);
    }

    @Test
    public void testGetConnectionUrlWithProxy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setEventPublisher(new EventPublisher());
        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        tenantEndpoint.setProxyHost("proxy.host");
        tenantEndpoint.setProxyPort(8080);
        connectionConfig.setEndpoint(tenantEndpoint);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://proxy.host:8080/testSchema", jdbcUrl);
    }

    @Test
    public void testGetDataSourceWithSsl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setSslConfig(new SSLConfig(true, null));
        connectionConfig.setEventPublisher(new EventPublisher());

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        DataSource dataSource = factory.getDataSource();
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("localhost", dataSource.getConnectionProperties().getProperty("url"));
        assertEquals("3306", dataSource.getConnectionProperties().getProperty("portNumber"));
        assertEquals("testSchema", factory.getUsername());
        assertEquals(DialectType.MYSQL, factory.getDialectType());
        assertTrue(dataSource.getConnectionProperties().getProperty("useSSL").equals("true"));

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testSchema", jdbcUrl);

        ConnectionConfig connectionConfigWithSslFile = new ConnectionConfig();
        connectionConfigWithSslFile.setUsername("testUser");
        connectionConfigWithSslFile.setPassword("testPassword");
        connectionConfigWithSslFile.setHost("localhost");
        connectionConfigWithSslFile.setPort(3306);
        connectionConfigWithSslFile.setDefaultSchema("testSchema");
        connectionConfigWithSslFile.setDialectType(DialectType.MYSQL);
        connectionConfigWithSslFile.setType(ConnectType.LONG);
        connectionConfigWithSslFile.setSslConfig(new SSLConfig(true, "test.p12", "testPassword"));
        connectionConfigWithSslFile.setEventPublisher(new EventPublisher());

        OBConsoleDataSourceFactory factoryWithSslFile = new OBConsoleDataSourceFactory(connectionConfigWithSslFile, false, true);

        DataSource dataSourceWithSslFile = factoryWithSslFile.getDataSource();
        assertEquals("testUser", dataSourceWithSslFile.getUsername());
        assertEquals("testPassword", dataSourceWithSslFile.getPassword());
        assertEquals("localhost", dataSourceWithSslFile.getConnectionProperties().getProperty("url"));
        assertEquals("3306", dataSourceWithSslFile.getConnectionProperties().getProperty("portNumber"));
        assertEquals("testSchema", factoryWithSslFile.getUsername());
        assertEquals(DialectType.MYSQL, factoryWithSslFile.getDialectType());
        assertTrue(dataSourceWithSslFile.getConnectionProperties().getProperty("useSSL").equals("true"));
        assertTrue(dataSourceWithSslFile.getConnectionProperties().getProperty("trustStore").equals("test.p12"));
        assertTrue(dataSourceWithSslFile.getConnectionProperties().getProperty("trustStorePassword").equals("testPassword"));
    }

    @Test
    public void testGetDataSourceWithNoSsl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setSslConfig(new SSLConfig(false, null));
        connectionConfig.setEventPublisher(new EventPublisher());

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        DataSource dataSource = factory.getDataSource();
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("localhost", dataSource.getConnectionProperties().getProperty("url"));
        assertEquals("3306", dataSource.getConnectionProperties().getProperty("portNumber"));
        assertEquals("testSchema", factory.getUsername());
        assertEquals(DialectType.MYSQL, factory.getDialectType());
        assertFalse(dataSource.getConnectionProperties().getProperty("useSSL").equals("true"));

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testSchema", jdbcUrl);
    }

    @Test
    public void testGetDataSourceWithInitializers() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setEventPublisher(new EventPublisher());
        connectionConfig.addJdbcUrlParameters(Map.of("parameter1", "value1"));

        List<ConnectionInitializer> initializers = List.of(new ConnectionInitializer() {
            @Override
            public void initializeDatabase(DataSource dataSource) throws Exception {
                // Initializer logic
            }
        });

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true, null, initializers);

        DataSource dataSource = factory.getDataSource();
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("localhost", dataSource.getConnectionProperties().getProperty("url"));
        assertEquals("3306", dataSource.getConnectionProperties().getProperty("portNumber"));
        assertEquals("testSchema", factory.getUsername());
        assertEquals(DialectType.MYSQL, factory.getDialectType());
        assertFalse(dataSource.getConnectionProperties().getProperty("useSSL").equals("true"));
        assertEquals("value1", dataSource.getConnectionProperties().getProperty("parameter1"));
        assertTrue(dataSource.getConnectionProperties().getProperty("allowLoadLocalInfile").equals("false"));
        assertTrue(dataSource.getConnectionProperties().getProperty("autoDeserialize").equals("false"));

        for (ConnectionInitializer initializer : initializers) {
            initializer.initializeDatabase(dataSource);
        }
    }
}