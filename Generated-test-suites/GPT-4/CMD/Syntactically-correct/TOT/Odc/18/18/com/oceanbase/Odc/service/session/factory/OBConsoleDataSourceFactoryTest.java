package com.oceanbase.odc.service.session.factory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;

    @Before
    public void setUp() {
        config = Mockito.mock(ConnectionConfig.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_ShouldThrowException_ForInvalidPort() {
        factory = new OBConsoleDataSourceFactory(config, true);
        factory.resetPort(port -> -1);  // set negative port, expect exception
    }

    
    @Test
    public void testGetUsername_ShouldReturnExpectedString() {
        Mockito.when(config.getUsername()).thenReturn("testUser");
        factory = new OBConsoleDataSourceFactory(config, true);
        assertEquals("testUser", factory.getUsername());
    }

    @Test
    public void testGetPassword_ShouldReturnPassword() {
        Mockito.when(config.getPassword()).thenReturn("testPassword");
        factory = new OBConsoleDataSourceFactory(config, true);
        assertEquals("testPassword", factory.getPassword());
    }


    @Test
    public void testGetJdbcUrl_ShouldReturnExpectedUrl() {
        Mockito.when(config.getHost()).thenReturn("localhost");
        Mockito.when(config.getPort()).thenReturn(8080);
        Mockito.when(config.getDefaultSchema()).thenReturn("testSchema");
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        Mockito.when(config.getJdbcUrlParameters()).thenReturn(params);
        factory = new OBConsoleDataSourceFactory(config, true);
        assertEquals("jdbc:obconsole://localhost:8080/testSchema?param1=value1&param2=value2",
                     factory.getJdbcUrl());  // assuming ConnectionExtensionPoint generates such url
    }

    @Test
    public void testDeepCopy_ReturnsDifferentInstance() {
        factory = new OBConsoleDataSourceFactory(config, true);
        OBConsoleDataSourceFactory copy = factory.deepCopy();
        assertNotSame(copy, factory);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_ShouldThrowException_ForNullConfig() {
        new OBConsoleDataSourceFactory(null, true);  // should throw exception
    }
}