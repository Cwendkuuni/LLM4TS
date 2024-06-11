package com.oceanbase.odc.service.session.factory;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("test");
        config.setDialectType(DialectType.OB_ORACLE);
        config.setOBTenantName("tenant");
        config.setClusterName("cluster");
        
        String expectedUsername = "test@tenant#cluster";
        String actualUsername = OBConsoleDataSourceFactory.getUsername(config);
        
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPassword("password123");
        
        String expectedPassword = "password123";
        String actualPassword = OBConsoleDataSourceFactory.getPassword(config);
        
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = new ConnectionConfig();
        SSLConfig ssl = new SSLConfig();
        ssl.setEnabled(false);
        config.setSslConfig(ssl);

        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("useSSL", "false");

        Map<String, String> actualParams = OBConsoleDataSourceFactory.getJdbcParams(config);

        assertEquals(expectedParams, actualParams);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);

        DataSource expectedDataSource = null; // Replace null with an actual instance of the expected DataSource
        DataSource actualDataSource = factory.getDataSource();

        assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        factory.resetUsername(oldValue -> "newUsername");

        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        factory.resetPassword(oldValue -> "newPassword");

        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig config = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);

        OBConsoleDataSourceFactory expectedFactory = null; // Replace null with an actual instance of the expected OBConsoleDataSourceFactory
        OBConsoleDataSourceFactory actualFactory = (OBConsoleDataSourceFactory) factory.deepCopy();

        assertEquals(expectedFactory, actualFactory);
    }
}