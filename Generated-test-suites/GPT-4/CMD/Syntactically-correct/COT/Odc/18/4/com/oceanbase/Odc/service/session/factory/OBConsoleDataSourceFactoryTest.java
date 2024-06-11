package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        //TODO: Initialize the connectionConfig
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUsername";
        assertEquals(expectedUsername, factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        assertEquals(expectedPassword, factory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        //TODO: set the connection type in connectionConfig
        assertEquals(connectionConfig.getType(), factory.getConnectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newTestUserName";
        factory.resetUsername(value -> newUsername);
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newTestPassword";
        factory.resetPassword(value -> newPassword);
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newTestHost";
        factory.resetHost(value -> newHost);
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer newPort = 8080;
        factory.resetPort(value -> newPort);
        assertEquals(newPort, factory.getPort());    
    }
    
    @Test
    public void testGetJdbcUrl(){
        // assume generateJdbcUrl returns expectedUrl
        String expectedUrl = "testUrl";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePort() {
        Integer negativePort = -8080;
        factory.resetPort(value -> negativePort);
    }

    //more tests for getDataSource, getDialectType, deepCopy, resetSchema, and resetParameters

}
