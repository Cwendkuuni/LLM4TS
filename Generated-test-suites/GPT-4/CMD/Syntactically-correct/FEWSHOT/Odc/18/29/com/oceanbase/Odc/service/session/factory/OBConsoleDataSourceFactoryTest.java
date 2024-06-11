
package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;
    
    @Before
    public void setUp() {
        
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setOBTenantName("testTenant");
        connectionConfig.setClusterName("testCluster");
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetUsername(){
        assertEquals("testUser@testTenant#testCluster", obConsoleDataSourceFactory.getUsername());
    }
    
    @Test
    public void testGetPassword(){
        assertEquals("testPassword", obConsoleDataSourceFactory.getPassword());
    }
    
    @Test
    public void testGetDialectType(){
        assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testGetJdbcUrl() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }
    
    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }  
	
    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(params);
        assertEquals("false", params.get("useSSL"));
        assertEquals("64000000", params.get("maxAllowedPacket"));
        assertEquals("5000", params.get("connectTimeout"));
    }
}
