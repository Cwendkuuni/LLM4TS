package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test");
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        
        assertNotNull(jdbcUrl);
        assertEquals("jdbc:mysql://localhost:3306/test", jdbcUrl);
    }
    
    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        
        assertNotNull(username);
        assertEquals("user", username);
    }
    
    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");
        
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        
        assertNotNull(password);
        assertEquals("password", password);
    }
    
    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        
        assertNotNull(jdbcParams);
        assertEquals(9, jdbcParams.size());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test");
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        DataSource dataSource = factory.getDataSource();
        
        assertNotNull(dataSource);
    }
    
    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();
        
        assertNotNull(dialectType);
        assertEquals(DialectType.MYSQL, dialectType);
    }
    
    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();
        
        assertNotNull(copiedFactory);
        assertEquals(factory.getClass(), copiedFactory.getClass());
    }
    
    // Additional test cases can be added here to further cover all methods in OBConsoleDataSourceFactory class
    
}