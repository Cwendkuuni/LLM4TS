package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig config;
    private OBConsoleDataSourceFactory factory;
    private static final Boolean AUTO_COMMIT = true;
    
    @Before
    public void setUp() {
        config = mock(ConnectionConfig.class);
        when(config.getHost()).thenReturn("localhost");
        when(config.getPort()).thenReturn(3306);
        when(config.getPassword()).thenReturn("password");
        when(config.getUsername()).thenReturn("username");
        when(config.getDefaultSchema()).thenReturn("defaultSchema");
        
        factory = new OBConsoleDataSourceFactory(config, AUTO_COMMIT);
    }
    
    @Test
    public void testGetUsername() {
        assertEquals("username", factory.getUsername());
    }
    
    @Test
    public void testGetPassword() {
        assertEquals("password", factory.getPassword());
    }
    
    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = newValue -> "mapped" + newValue;
        factory.resetUsername(mapper);
        assertEquals("mappedusername", factory.getUsername());
    }
    
    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = newValue -> "mapped" + newValue;
        factory.resetPassword(mapper);
        assertEquals("mappedpassword", factory.getPassword());
    }
    
    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = newValue -> "mapped" + newValue;
        factory.resetHost(mapper);
        assertEquals("localhost", factory.getHost());
    }
    
    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = newValue -> 1234;
        factory.resetHost(mapper);
        assertEquals(3306, factory.getPort().intValue());
    }
    
    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = newValue -> "mapped" + newValue;
        factory.resetSchema(mapper);
        assertEquals("defaultSchema", factory.getSchema());
    }
     
}