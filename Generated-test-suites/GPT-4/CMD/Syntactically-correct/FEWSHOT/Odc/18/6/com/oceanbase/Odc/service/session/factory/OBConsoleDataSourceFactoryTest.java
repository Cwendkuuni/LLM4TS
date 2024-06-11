package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("user");
        config.setDialectType(DialectType.ORACLE);
        config.setTenantName("tenant");
        config.setClusterName("cluster");
        String username = OBConsoleDataSourceFactory.getUsername(config);
        assertEquals("\"user\"@tenant#cluster", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPassword("password");
        String password = OBConsoleDataSourceFactory.getPassword(config);
        assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = new ConnectionConfig();
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("proxy");
        endpoint.setProxyPort(8888);
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        config.setSslConfig(sslConfig);
        config.setEndpoint(endpoint);
        config.setJdbcUrlParameters(new HashMap<String, String>());
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(config);
        assertNotNull(params.get("socksProxyHost"));
        assertNotNull(params.get("socksProxyPort")); 
    }
    
    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.ORACLE);
        config.setUsername("user");
        String schema = OBConsoleDataSourceFactory.getDefaultSchema(config);
        assertEquals("\"user\"", schema);
    }

    // Further unit-tests should be conducted for rest of the methods in this class.
    // This includes testing each method with different kinds of inputs and verifying
    // the output by the method.
}