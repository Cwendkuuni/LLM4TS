package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:3306/schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testGetUsername() {
        assertEquals("user", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.DEFAULT, dataSourceFactory.getConnectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 8080);
        assertEquals(Integer.valueOf(8080), dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters(value -> {
            Map<String, String> params = new HashMap<>();
            params.put("newParam", "value");
            return params;
        });
        assertEquals("value", dataSourceFactory.getParameters().get("newParam"));
    }
    
}