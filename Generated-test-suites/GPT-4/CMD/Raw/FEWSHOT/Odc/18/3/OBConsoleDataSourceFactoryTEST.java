package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();

        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("defaultSchema");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetJdbcUrl() {
        assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        
        // Check if cloned object is not null and not the same instance with the original object
        assertNotNull(clonedFactory);
        assertNotEquals(obConsoleDataSourceFactory, clonedFactory);
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("defaultSchema", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
    }

    @Test
    public void testGetUsernameFromConnectionConfig() {
        assertEquals("username", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPasswordFromConnectionConfig() {
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> parameters = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(parameters);
        assertEquals("64000000", parameters.get("maxAllowedPacket"));
        assertEquals("true", parameters.get("allowMultiQueries"));
        assertEquals("5000", parameters.get("connectTimeout"));
    }
}