package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(s -> s.toUpperCase());
        assertEquals("USER", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(s -> s.toUpperCase());
        assertEquals("PASSWORD", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(s -> s.toUpperCase());
        assertEquals("LOCALHOST", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(s -> s * 2);
        assertEquals(6606, obConsoleDataSourceFactory.getPort().intValue());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(s -> s.toUpperCase());
        assertEquals("SCHEMA", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(s -> {
            Map<String, String> map = new HashMap<>();
            map.put("key1", s.get("key1").toUpperCase());
            return map;
        });
        assertEquals("VALUE1", obConsoleDataSourceFactory.getParameters().get("key1"));
    }
}