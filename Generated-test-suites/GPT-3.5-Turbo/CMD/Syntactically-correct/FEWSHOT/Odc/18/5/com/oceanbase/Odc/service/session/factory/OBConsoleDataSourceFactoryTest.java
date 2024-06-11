package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("localhost"));
        assertTrue(jdbcUrl.contains("3306"));
        assertTrue(jdbcUrl.contains("test_schema"));
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getUsername()).thenReturn("admin");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        
        assertNotNull(username);
        assertEquals("admin", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getPassword()).thenReturn("password");
        
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        
        assertNotNull(password);
        assertEquals("password", password);
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        
        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
        
        assertNotNull(defaultSchema);
        assertEquals("test_schema", defaultSchema);
    }

    // Add more test cases for other methods in OBConsoleDataSourceFactory
    
}