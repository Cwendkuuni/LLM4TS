package com.oceanbase.odc.service.session.factory;
import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        Assert.assertNotNull(jdbcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOBConsoleDataSourceFactory_NullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }
}