package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import org.junit.Assert;
import org.junit.Test;
import javax.sql.DataSource;
import java.util.HashMap;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetDialectType() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);

        Assert.assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(3306);
        config.setUsername("username");
        config.setPassword("password");
        config.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);

        DataSource dataSource = factory.getDataSource();
        Assert.assertNotNull(dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortInvalid() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPort(-1);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        factory.resetPort(val -> -1);
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig config = new ConnectionConfig();
        config.setJdbcUrlParameters(new HashMap<String, String>());
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);

        factory.resetParameters(val -> {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("testKey", "testValue");
            return params;
        });

        Assert.assertEquals("testValue", factory.parameters.get("testKey"));
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("TestUsername");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        Assert.assertEquals("TestUsername", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPassword("TestPassword");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        Assert.assertEquals("TestPassword", factory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        ConnectionConfig config = new ConnectionConfig();
        config.setType(ConnectType.OCEANBASE);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        Assert.assertEquals(ConnectType.OCEANBASE, factory.getConnectType());
    }
}
