
package com.oceanbase.odc.service.session.factory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.SSLConfig;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig testConfig;
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        testConfig = new ConnectionConfig();
        testConfig.setHost("localhost");
        testConfig.setPort(3306);
        testConfig.setDefaultSchema("test");
        testConfig.setUsername("testuser");
        testConfig.setPassword("password");

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        sslConfig.setKeyStoreFilePassword("SSLPassword");
        testConfig.setSslConfig(sslConfig);
        factory = new OBConsoleDataSourceFactory(testConfig, true);
    }

    @Test
    public void getJdbcUrlTest() {
        String expectedUrl = "jdbc:oceanbase://localhost:3306/test";
        Assert.assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void getUsernameTest() {
        Assert.assertEquals("testuser@localhost#3306", factory.getUsername(testConfig));
    }

    @Test
    public void getPasswordTest() {
        Assert.assertEquals("password", factory.getPassword(testConfig));
    }

    @Test
    public void getDataSourceTest() {
        SingleConnectionDataSource dataSource = factory.getDataSource();
        Assert.assertNotNull(dataSource);
        Assert.assertEquals("testuser@localhost#3306", dataSource.getUsername());
        Assert.assertEquals("password", dataSource.getPassword());
    }
}
