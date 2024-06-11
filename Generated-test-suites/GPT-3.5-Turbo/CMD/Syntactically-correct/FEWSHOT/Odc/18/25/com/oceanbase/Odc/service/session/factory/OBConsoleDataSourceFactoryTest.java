package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("schema");
        
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:1521/schema";
        
        assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DataSource dataSource = dataSourceFactory.getDataSource();
        
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        CloneableDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        
        assertNotNull(deepCopy);
        assertEquals(dataSourceFactory.getDialectType(), deepCopy.getDialectType());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        dataSourceFactory.resetUsername(String::toUpperCase);
        
        assertEquals("USERNAME", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        dataSourceFactory.resetPassword(String::toUpperCase);
        
        assertEquals("PASSWORD", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        dataSourceFactory.resetPort(port -> port * 2);
        
        assertEquals(Integer.valueOf(8080), dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        dataSourceFactory.resetSchema(String::toUpperCase);
        
        assertEquals("SCHEMA", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        parameters.put("key2", "value2");
        
        dataSourceFactory.resetParameters(param -> {
            param.put("key1", "updatedValue1");
            return param;
        });
        
        assertEquals("updatedValue1", dataSourceFactory.getParameters().get("key1"));
        assertEquals("value2", dataSourceFactory.getParameters().get("key2"));
    }
}