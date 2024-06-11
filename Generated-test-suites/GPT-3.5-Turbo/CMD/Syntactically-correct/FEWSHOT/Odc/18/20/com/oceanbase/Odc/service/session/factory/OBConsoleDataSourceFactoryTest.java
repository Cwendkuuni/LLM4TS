package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:3306/testSchema";
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
        assertEquals(dataSourceFactory.getClass(), copy.getClass());
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 1234);
        assertEquals(1234, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        
        dataSourceFactory.resetParameters(value -> params);
        assertEquals(params, dataSourceFactory.getParameters());
    }
}