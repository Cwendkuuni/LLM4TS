package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {
    
    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setOBTenantName("testTenant");
        connectionConfig.setClusterName("testCluster");

        String expected = "\"testUser\"@testTenant#testCluster";
        String actual = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("testPassword");

        String expected = "testPassword";
        String actual = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        Map<String, String> actualParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("64000000", actualParams.get("maxAllowedPacket"));
        assertEquals("true", actualParams.get("allowMultiQueries"));
        assertEquals("5000", actualParams.get("connectTimeout"));
        assertEquals(OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR, actualParams.get("zeroDateTimeBehavior"));
        assertEquals("true", actualParams.get("noDatetimeStringSync"));
        assertEquals("false", actualParams.get("jdbcCompliantTruncation"));
        assertEquals("false", actualParams.get("sendConnectionAttributes"));
        assertEquals("false", actualParams.get("useSSL"));
        assertEquals("false", actualParams.get("allowLoadLocalInfile"));
        assertEquals("false", actualParams.get("allowUrlInLocalInfile"));
        assertEquals("false", actualParams.get("autoDeserialize"));
        assertEquals("", actualParams.get("allowLoadLocalInfileInPath"));
    }
    
    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory copiedFactory = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
        
        assertNotNull(copiedFactory);
        assertNotSame(dataSourceFactory, copiedFactory);
    }
    
    // Please note that it is quite tricky to test methods such as `resetUsername`, `resetPassword`, `resetHost`, `resetPort`, 
    // `resetSchema`, `resetParameters` without a real instance of `CloneableDataSourceFactory.ValueMapper<T>`. You can create mock 
    // instances of `ValueMapper` interface using a library like Mockito to simulate its behavior and test these methods accordingly.
}
