package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("test_host");
        connectionConfig.setPort(1234);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.CONNECT_BY_IP);
        connectionConfig.setTenantName("test_tenant");
        connectionConfig.setClusterName("test_cluster");

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);

        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("keyStoreFilePath");
        sslFileEntry.setKeyStoreFilePassword("keyStoreFilePassword");
        connectionConfig.setSslFileEntry(sslFileEntry);

        OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
        obTenantEndpoint.setProxyHost("proxyHost");
        obTenantEndpoint.setProxyPort(1234);
        connectionConfig.setEndpoint(obTenantEndpoint);

        Map<String, String> jdbcUrlParameters = new HashMap<>();
        jdbcUrlParameters.put("param1", "value1");
        jdbcUrlParameters.put("param2", "value2");
        connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(username -> username.toUpperCase());
        assertEquals("TEST_USER@TEST_TENANT#TEST_CLUSTER", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(password -> password.toUpperCase());
        assertEquals("TEST_PASSWORD", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(host -> host.toUpperCase());
        assertEquals("TEST_HOST", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(port -> port + 10);
        assertNotNull(obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(schema -> schema.toUpperCase());
        assertNotNull(obConsoleDataSourceFactory.getSchema());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(parameters -> {
            Map<String, String> newParameters = new HashMap<>();
            parameters.forEach((key, value) -> newParameters.put(key.toUpperCase(), value.toUpperCase()));
            return newParameters;
        });
        assertNotNull(obConsoleDataSourceFactory.getParameters());
    }

    @Test
    public void testGetConnectType() {
        assertNotNull(obConsoleDataSourceFactory.getConnectType());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
    }

    @After
    public void tearDown() {
        obConsoleDataSourceFactory = null;
        connectionConfig = null;
    }
}
