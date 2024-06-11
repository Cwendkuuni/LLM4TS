package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    // Test variables
    private final ConnectionConfig connectionConfig = new ConnectionConfig();
    private final Boolean autoCommit = true;
    private final OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:mock://localhost:3306/db";
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("db");
        
        String actual = obConsoleDataSourceFactory.getJdbcUrl();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsername() {
        String expected = "user";
        connectionConfig.setUsername("user");
        
        String actual = obConsoleDataSourceFactory.getUsername();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPassword() {
        String expected = "pass";
        connectionConfig.setPassword("pass");
        
        String actual = obConsoleDataSourceFactory.getPassword();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetConnectType() {
        ConnectType expected = ConnectType.OBCONSOLE;
        connectionConfig.setType(ConnectType.OBCONSOLE);
        
        ConnectType actual = obConsoleDataSourceFactory.getConnectType();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDialectType() {
        DialectType expected = DialectType.OB_ORACLE;
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        
        DialectType actual = obConsoleDataSourceFactory.getDialectType();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        
        assertNotNull(clonedFactory);
        assertEquals(obConsoleDataSourceFactory.getUsername(), clonedFactory.getUsername());
        assertEquals(obConsoleDataSourceFactory.getPassword(), clonedFactory.getPassword());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_user";
        
        obConsoleDataSourceFactory.resetUsername((value) -> newUsername);
        
        assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_pass";
        
        obConsoleDataSourceFactory.resetPassword((value) -> newPassword);
        
        assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        
        obConsoleDataSourceFactory.resetHost((value) -> newHost);
        
        assertEquals(newHost, obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer newPort = 3307;
        
        obConsoleDataSourceFactory.resetPort((value) -> newPort);
        
        assertEquals(newPort, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        
        obConsoleDataSourceFactory.resetSchema((value) -> newSchema);
        
        assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("param1", "value1");
        
        obConsoleDataSourceFactory.resetParameters((value) -> newParams);
        
        assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
    }

    // Add more test cases as needed...

}