
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;
  
    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(33060);
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
      
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", obConsoleDataSourceFactory.getPassword());
    }
  
    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> value + "_new");
        assertEquals("username_new", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> value + "_new");
        assertEquals("password_new", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> value + ".com");
        assertEquals("localhost.com", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> value + 1);
        assertEquals(33061, obConsoleDataSourceFactory.getPort().intValue());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParams = obConsoleDataSourceFactory.getParameters();
        Map<String, String> newParams = new HashMap<>(oldParams);
        newParams.put("autoCommit", "false");
        obConsoleDataSourceFactory.resetParameters(value -> newParams);
        assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
    }
}
