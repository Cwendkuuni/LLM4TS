package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;

    @Before
    public void setup() {
        config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(1234);
        factory = new OBConsoleDataSourceFactory(config, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = factory.getJdbcUrl();

        assertNotNull(jdbcUrl);
        assertEquals("Expected jdbc url", "jdbc:localhost:1234", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        config.setUsername("user");
        String username = OBConsoleDataSourceFactory.getUsername(config);

        assertNotNull(username);
        assertEquals("Expected username", "user", username);
    }

    @Test
    public void testGetPassword() {
        config.setPassword("password");
        String password = OBConsoleDataSourceFactory.getPassword(config);

        assertNotNull(password);
        assertEquals("Expected password", "password", password);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(config);

        assertNotNull(params);
        assertEquals("Expected number of params", 12, params.size());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        config.setDialectType(DialectType.OB_MYSQL);
        assertEquals("Expected dialect type", DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = factory.deepCopy();

        assertNotNull(copy);
        assertEquals("Expected matching config", factory.connectionConfig, copy.connectionConfig);
    }

    @Test
    public void testResetUsername() {
        factory.resetUsername(s -> s.toUpperCase());
        assertEquals("Expected username to be upper case", "USER", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        factory.resetPassword(s -> s.toUpperCase());
        assertEquals("Expected password to be upper case", "PASSWORD", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        factory.resetHost(s -> "newHost");
        assertEquals("Expected host to be reset", "newHost", factory.host);
    }

    @Test
    public void testResetPort() {
        factory.resetPort(p -> p + 1);
        assertEquals("Expected port to be increased by 1", Integer.valueOf(1235), factory.port);
    }
}