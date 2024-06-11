
package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.springframework.test.util.ReflectionTestUtils;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("username");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        String actual = ReflectionTestUtils.invokeMethod(factory, "getUsername");
        assertEquals("username", actual);
    }

    @Test
    public void testPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        String actual = ReflectionTestUtils.invokeMethod(factory, "getPassword");
        assertEquals("password", actual);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("username");
        config.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        DataSource datasource = factory.getDataSource();
        assertNotNull(datasource);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("username");
        config.setPassword("password");
        OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(config, true);
        OBConsoleDataSourceFactory copyFactory = originalFactory.deepCopy();
        assertNotNull(copyFactory);
    }

    @Test
    public void testGetDbUser() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("username");
        assertEquals("username", OBConsoleDataSourceFactory.getDbUser(config));
    }
    
    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDefaultSchema("default");
        assertEquals("\"default\"", OBConsoleDataSourceFactory.getDefaultSchema(config));
    }
    

}
