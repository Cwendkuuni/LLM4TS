package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    
    private ConnectionConfig testConnectionConfig;
    private Boolean autoCommit = true;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() throws Exception {
        testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername("username");
        testConnectionConfig.setPassword("password");
        testConnectionConfig.setHost("localhost");
        testConnectionConfig.setPort(3306);
        testConnectionConfig.setDefaultSchema("defaultSchema");
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, autoCommit);
    }

    @After
    public void tearDown() throws Exception {
        obConsoleDataSourceFactory = null;
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(testConnectionConfig.getDialectType(), obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testGetJdbcUrl() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", OBConsoleDataSourceFactory.getUsername(testConnectionConfig));
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(testConnectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(testConnectionConfig);
        assertNotNull(params);
        assertEquals("64000000", params.get("maxAllowedPacket"));
        assertEquals("true", params.get("allowMultiQueries"));
        // continue checking for the rest of the parameters
    }
 
    // ... Add tests for other public and edge case methods here ...
}