package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setDefaultSchema("test");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setTenantName("tenant");
        connectionConfig.setClusterName("cluster");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        assertEquals("\"user\"@tenant#cluster", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", factory.getPassword());
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:8080/test";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factoryCopy = factory.deepCopy();
        assertNotSame(factory, factoryCopy);
    }

    @Test
    public void testResetUsername() {
        factory.resetUsername(newValue -> "newuser");
        assertEquals("newuser", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        factory.resetPassword(newValue -> "newpassword");
        assertEquals("newpassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        factory.resetHost(newValue -> "newhost");
        assertEquals(factory.getJdbcUrl(), "jdbc:mysql://newhost:8080/test");
    }

    @Test
    public void testResetPort() {
        factory.resetPort(newValue -> 9000);
        assertEquals(factory.getJdbcUrl(), "jdbc:mysql://localhost:9000/test");
    }

    @Test
    public void testResetSchema() {
        factory.resetSchema(newValue -> "newschema");
        assertEquals(factory.getJdbcUrl(), "jdbc:mysql://localhost:8080/newschema");
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("newKey", "newValue");
        factory.resetParameters(newValue -> params);
        assertTrue(factory.getJdbcUrl().contains("newKey=newValue"));
    }
}
