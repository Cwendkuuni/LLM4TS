package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getUsername()).thenReturn("testUser");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = factory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getPassword()).thenReturn("testPassword");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = factory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    // Add more test cases as needed

}