package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;

public class OBConsoleDataSourceFactoryTest {
  
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testGetUserName() {
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("user");
        connectionConfig.setOBTenantName("tenant");
        connectionConfig.setClusterName("cluster");
        assertEquals("\"user\"@tenant#cluster", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        connectionConfig.setPassword("password");
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }
  
    @Test
    public void testGetJdbcParams() {
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("proxyHost");
        endpoint.setProxyPort(1234);
        connectionConfig.setEndpoint(endpoint);
        
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);
        
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("filePath");
        sslFileEntry.setKeyStoreFilePassword("filePassword");
        connectionConfig.setSslFileEntry(sslFileEntry);

        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        connectionConfig.setJdbcUrlParameters(params);

        Map<String,String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("true", jdbcParams.get("useSSL"));
        assertEquals("false", jdbcParams.get("trustServerCertificate"));
        assertEquals("filePath", jdbcParams.get("trustStore"));
        assertEquals("filePassword", jdbcParams.get("trustStorePassword"));
        assertEquals("false", jdbcParams.get("allowLoadLocalInfile"));
        assertEquals("false", jdbcParams.get("allowUrlInLocalInfile"));
    }

    @Test
    public void testGetDefaultSchema() {
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("defaultSchema");
        assertEquals("\"defaultSchema\"", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
        assertNull(OBConsoleDataSourceFactory.getDefaultSchema(null));
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.setEventPublisher(new ApplicationEventPublisher() {});
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.deepCopy());
    }

    @Test
    public void testResetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(old -> "newUsername");
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(old -> "newPassword");
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetHost(old -> "newHost");
        assertEquals("newHost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(old -> old + 1);
        assertEquals(Integer.valueOf(1), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(old -> "newSchema");
        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> newParams = new HashMap<>();
        newParams.put("param1", "value1");
        factory.resetParameters(old -> newParams);
        assertTrue(factory.getParameters().containsKey("param1"));
    }
}