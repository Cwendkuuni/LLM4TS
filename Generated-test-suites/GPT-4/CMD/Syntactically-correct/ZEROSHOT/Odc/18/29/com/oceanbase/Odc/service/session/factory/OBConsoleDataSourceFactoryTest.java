package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.Before;

import javax.sql.DataSource;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.HashMap;

/**
 * The test class for OBConsoleDataSourceFactory. 
 * @author John Doe
 * @version 1.0
 * @since 2021-11-16
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    
    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("username", username);
    }

    @Test
    public void testGetPassword() {
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(jdbcParams);
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
        OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        ValueMapper<String> mapper = (username) -> "new_username";
        dataSourceFactory.resetUsername(mapper);
        assertEquals("new_username", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ValueMapper<String> mapper = (password) -> "new_password";
        dataSourceFactory.resetPassword(mapper);
        assertEquals("new_password", dataSourceFactory.getPassword());
    }

    // Add your other test methods here...

}
