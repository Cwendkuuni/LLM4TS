package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", "value2");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306/test?param1=value1&param2=value2";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();

        assertNotNull(copiedFactory);
        assertEquals(factory.getJdbcUrl(), copiedFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        factory.resetUsername(value -> "newUser");

        assertEquals("newUser", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        factory.resetPassword(value -> "newPassword");

        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetPort() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        factory.resetPort(value -> 1234);

        assertEquals(1234, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        factory.resetSchema(value -> "newSchema");

        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        Map<String, String> newParams = new HashMap<>();
        newParams.put("key1", "value1");
        newParams.put("key2", "value2");
        factory.resetParameters(value -> newParams);

        assertEquals(newParams, factory.getParameters());
    }
}