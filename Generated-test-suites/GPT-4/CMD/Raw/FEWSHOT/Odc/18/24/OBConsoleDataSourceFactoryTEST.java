package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setOBTenantName("tenant");
        connectionConfig.setClusterName("cluster");
        String expected = "\"test\"@tenant#cluster";
        
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        
        assertEquals(expected, username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("secret");
        
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        
        assertEquals("secret", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setEndpoint(new OBTenantEndpoint());
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("test", "value");
        connectionConfig.setJdbcUrlParameters(userParams);
        
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        
        assertTrue(params.containsKey("maxAllowedPacket"));
        assertTrue(params.containsKey("allowMultiQueries"));
        assertTrue(params.containsKey("connectTimeout"));
        assertTrue(params.containsKey("zeroDateTimeBehavior"));
        assertTrue(params.containsKey("noDatetimeStringSync"));
        assertTrue(params.containsKey("jdbcCompliantTruncation"));
        assertTrue(params.containsKey("sendConnectionAttributes"));
        assertTrue(params.containsKey("useSSL"));
        assertTrue(params.containsKey("allowLoadLocalInfile"));
        assertTrue(params.containsKey("allowUrlInLocalInfile"));
        assertTrue(params.containsKey("allowLoadLocalInfileInPath"));
        assertTrue(params.containsKey("autoDeserialize"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DataSource dataSource = factory.getDataSource();
        
        assertNotNull(dataSource);
    }
    
    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.DIRECT);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        CloneableDataSourceFactory clone = factory.deepCopy();
        
        assertEquals(factory.getDialectType(), clone.getDialectType());
        assertNotNull(clone);
    }
    
    // Similarly, test cases can be written for other public methods...
}