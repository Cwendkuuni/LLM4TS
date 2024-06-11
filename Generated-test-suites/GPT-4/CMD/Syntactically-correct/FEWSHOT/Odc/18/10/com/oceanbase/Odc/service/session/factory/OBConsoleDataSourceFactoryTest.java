package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import javax.sql.DataSource;

import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        assertEquals("testUser", factory.getUsername());
    }
    
    @Test
    public void testGetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        assertEquals("testPassword", factory.getPassword());
    }
    
    @Test
    public void testGetConnectType() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        config.setType(ConnectType.DIRECT);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        assertEquals(ConnectType.DIRECT, factory.getConnectType());
    }
    
    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        config.setType(ConnectType.DIRECT);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        assertNotNull(factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        config.setType(ConnectType.DIRECT);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        DataSource ds = factory.getDataSource();
        assertNotNull(ds);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testUser");
        config.setPassword("testPassword");
        config.setType(ConnectType.DIRECT);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config,true);
        OBConsoleDataSourceFactory copiedFactory = factory.deepCopy();
        assertTrue(factory != copiedFactory);
    }
}