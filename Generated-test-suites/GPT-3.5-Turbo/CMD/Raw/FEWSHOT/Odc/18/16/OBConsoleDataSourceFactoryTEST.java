/*
 * This class is responsible for testing the methods in OBConsoleDataSourceFactory class
 */

package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("admin");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test");

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:3306:test";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();

        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        DialectType actualDialectType = dataSourceFactory.getDialectType();

        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();

        assertNotNull(clonedFactory);
        assertEquals(dataSourceFactory.getJdbcUrl(), clonedFactory.getJdbcUrl());
        assertEquals(dataSourceFactory.getConnectType(), clonedFactory.getConnectType());
    }

    @Test
    public void testResetUsername() {
        ValueMapper<String> valueMapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return value.toUpperCase();
            }
        };

        dataSourceFactory.resetUsername(valueMapper);

        assertEquals("ADMIN", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ValueMapper<String> valueMapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return value + "123";
            }
        };

        dataSourceFactory.resetPassword(valueMapper);

        assertEquals("password123", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetPort() {
        ValueMapper<Integer> valueMapper = new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return value + 10;
            }
        };

        dataSourceFactory.resetPort(valueMapper);

        assertEquals(3406, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ValueMapper<String> valueMapper = new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return value + "_new";
            }
        };

        dataSourceFactory.resetSchema(valueMapper);

        assertEquals("test_new", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ValueMapper<Map<String, String>> valueMapper = new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                value.put("key", "new value");
                return value;
            }
        };

        dataSourceFactory.resetParameters(valueMapper);

        assertEquals("new value", dataSourceFactory.getParameters().get("key"));
    }

    interface ValueMapper<T> {
        T map(T value);
    }
}