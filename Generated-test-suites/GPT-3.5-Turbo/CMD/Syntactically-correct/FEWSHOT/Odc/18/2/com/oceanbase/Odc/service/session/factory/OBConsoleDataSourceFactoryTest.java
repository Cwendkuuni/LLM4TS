package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.DB2);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");

        // Add some sample parameters
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        connectionConfig.setJdbcUrlParameters(params);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertNotNull(jdbcUrl);
        assertEquals("jdbc:db2://localhost:3306/test_schema?param1=value1&param2=value2", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testDeepCopy() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.POSTGRES);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(5432);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        CloneableDataSourceFactory copy = factory.deepCopy();

        assertNotNull(copy);
        assertEquals(factory.getJdbcUrl(), ((OBConsoleDataSourceFactory)copy).getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.SQL_SERVER);
        connectionConfig.setUsername("user");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        factory.resetUsername(username -> "new_user");

        assertEquals("new_user", factory.getUsername());
    }

    @Test
    public void testResetPort() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.ORACLE);
        connectionConfig.setPort(1521);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        factory.resetPort(port -> port + 1);

        assertEquals(Integer.valueOf(1522), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        // Create a sample ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setDefaultSchema("test");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        factory.resetSchema(schema -> "new_test");

        assertEquals("new_test", factory.getDefaultSchema());
    }
}