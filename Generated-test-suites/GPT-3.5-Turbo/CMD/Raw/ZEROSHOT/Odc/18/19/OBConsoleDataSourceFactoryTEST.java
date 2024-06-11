package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        // Initialize OBConsoleDataSourceFactory
        ConnectionConfig connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
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
        assertNotNull(dialectType);
        assertEquals(DialectType.OB_ORACLE, dialectType); // Change the expected DialectType as needed
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "newUsername");
        String username = obConsoleDataSourceFactory.getUsername();
        assertEquals("newUsername", username);
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> "newPassword");
        String password = obConsoleDataSourceFactory.getPassword();
        assertEquals("newPassword", password);
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "newHost");
        String host = obConsoleDataSourceFactory.getHost();
        assertEquals("newHost", host);
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 9999);
        int port = obConsoleDataSourceFactory.getPort();
        assertEquals(9999, port);
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
        String defaultSchema = obConsoleDataSourceFactory.getDefaultSchema();
        assertEquals("newSchema", defaultSchema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        obConsoleDataSourceFactory.resetParameters(value -> parameters);
        Map<String, String> updatedParams = obConsoleDataSourceFactory.getParameters();
        assertEquals(parameters, updatedParams);
    }
}

