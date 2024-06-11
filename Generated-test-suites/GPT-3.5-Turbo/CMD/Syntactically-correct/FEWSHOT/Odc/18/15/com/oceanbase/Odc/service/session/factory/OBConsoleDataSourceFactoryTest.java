package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        Map<String, String> params = new HashMap<>();
        params.put("user", "root");
        connectionConfig.setJdbcUrlParameters(params);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_db?user=root";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        Map<String, String> params = new HashMap<>();
        params.put("user", "root");
        connectionConfig.setJdbcUrlParameters(params);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        Map<String, String> params = new HashMap<>();
        params.put("user", "root");
        connectionConfig.setJdbcUrlParameters(params);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals(factory.getJdbcUrl(), clonedFactory.getJdbcUrl());
        assertEquals(factory.getUsername(), clonedFactory.getUsername());
        assertEquals(factory.getPassword(), clonedFactory.getPassword());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "new_user";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("old_user");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(mapper);
        assertEquals("new_user", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "new_password";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("old_password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(mapper);
        assertEquals("new_password", factory.getPassword());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> value + 1;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(3306);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(mapper);
        assertEquals(Integer.valueOf(3307), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "new_schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("old_schema");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(mapper);
        assertEquals("new_schema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> {
            value.put("test_param", "test_value");
            return value;
        };
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> params = new HashMap<>();
        params.put("user", "root");
        connectionConfig.setJdbcUrlParameters(params);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetParameters(mapper);
        assertEquals("test_value", factory.getJdbcUrlParameters().get("test_param"));
    }

}