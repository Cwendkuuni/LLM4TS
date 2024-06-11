package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testOBConsoleDataSourceFactory(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getUsername()).thenReturn("username");
        when(config.getPassword()).thenReturn("password");
        when(config.getHost()).thenReturn("localhost");
        when(config.getPort()).thenReturn(3306);
        when(config.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, autoCommit);
                
        assertEquals("username", factory.getUsername());
        assertEquals("password", factory.getPassword());
        assertEquals(ConnectType.LONG_CONNECTION, factory.getConnectType());
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testGetJdbcUrl(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getHost()).thenReturn("localhost");
        when(config.getPort()).thenReturn(3306);
        when(config.getDefaultSchema()).thenReturn("");
        when(config.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, autoCommit);

        String expectedUrl = "jdbc:mysql://localhost:3306/";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testResetUsername(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getUsername()).thenReturn("username");
        when(config.getPassword()).thenReturn("password");
        when(config.getHost()).thenReturn("localhost");
        when(config.getPort()).thenReturn(3306);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, autoCommit);

        factory.resetUsername(value-> "newUsername");
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getUsername()).thenReturn("username");
        when(config.getPassword()).thenReturn("password");
        when(config.getHost()).thenReturn("localhost");
        when(config.getPort()).thenReturn(3306);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, autoCommit);

        factory.resetPassword(value-> "newPassword");
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testGetUsername(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getUsername()).thenReturn("username");
        when(config.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(config.getOBTenantName()).thenReturn("sampleTenant");
        when(config.getClusterName()).thenReturn("sampleCluster");

        String output = OBConsoleDataSourceFactory.getUsername(config);
        assertEquals("username@sampleTenant#sampleCluster", output);
    }

    @Test
    public void testGetPassword(){
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getPassword()).thenReturn("password");

        String output = OBConsoleDataSourceFactory.getPassword(config);
        assertEquals("password", output);
    } 
}