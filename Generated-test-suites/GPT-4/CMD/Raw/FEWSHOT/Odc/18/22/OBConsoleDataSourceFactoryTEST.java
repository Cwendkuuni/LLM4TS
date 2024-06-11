package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBConsoleDataSourceFactoryTest {
    
    @Mock
    ConnectionConfig connectionConfigMock;

    OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigMock, true);
    }

    @Test
    public void getJdbcUrlTest() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void getUsernameTest() {
        when(connectionConfigMock.getUsername()).thenReturn("testuser");
        when(connectionConfigMock.getDialectType()).thenReturn(DialectType.MYSQL);
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfigMock);
        assertEquals("testuser", username);
    }

    @Test
    public void getPasswordTest() {
        when(connectionConfigMock.getPassword()).thenReturn("password");
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfigMock);
        assertEquals("password", password);
    }

    @Test
    public void getJdbcParamsTest() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfigMock);
        assertNotNull(params);
        assertEquals("64000000", params.get("maxAllowedPacket"));
        assertEquals("true", params.get("allowMultiQueries"));
        // add other assertions as necessary...
    }

    @Test
    public void getDataSourceTest() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void resetMethodsTest() {
        obConsoleDataSourceFactory.resetUsername(value -> value + "_new");
        obConsoleDataSourceFactory.resetPassword(value -> value + "_new");
        obConsoleDataSourceFactory.resetHost(value -> value + "_new");
        obConsoleDataSourceFactory.resetPort(value -> value * 2);
        obConsoleDataSourceFactory.resetSchema(value -> value + "_new");
        obConsoleDataSourceFactory.resetParameters(value -> {
           Map<String, String> newParams = new HashMap<>(value);
           newParams.put("test", "value");
           return newParams;
        });
        assertEquals("username_new", obConsoleDataSourceFactory.getUsername());
        assertEquals("password_new", obConsoleDataSourceFactory.getPassword());
        // add other assertions as necessary...
    }
    // Add more test cases as necessary...
}