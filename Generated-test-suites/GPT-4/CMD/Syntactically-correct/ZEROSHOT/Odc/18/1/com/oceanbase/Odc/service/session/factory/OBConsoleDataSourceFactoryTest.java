package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        // Prepare ConnectionConfig
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_username");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        
        // use the ConnectionConfig to initialize the dataSourceFactory
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void getJdbcUrlTest() {
        assertNotNull(dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void getUsernameTest() {
        assertEquals("test_username", dataSourceFactory.getUsername());
    }

    @Test
    public void getPasswordTest() {
        assertEquals("test_password", dataSourceFactory.getPassword());
    }

    @Test
    public void getDataSourceTest() {
        assertNotNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void getDialectTypeTest() {
        assertNotNull(dataSourceFactory.getDialectType());
    }

    @Test
    public void deepCopyTest() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotSame(clonedDataSourceFactory, dataSourceFactory);
        assertEquals(clonedDataSourceFactory.getUsername(), dataSourceFactory.getUsername());
    }

    @Test
    public void resetUsernameTest() {
        dataSourceFactory.resetUsername(value -> value + "_modified");
        assertEquals("test_username_modified", dataSourceFactory.getUsername());
    }

    @Test
    public void resetPasswordTest() {
        dataSourceFactory.resetPassword(value -> value + "_modified");
        assertEquals("test_password_modified", dataSourceFactory.getPassword());
    }

    @Test
    public void resetHostTest() {
        dataSourceFactory.resetHost(value -> "192.168.0.101");
        assertEquals("192.168.0.101", dataSourceFactory.getHost());
    }

    @Test
    public void resetPortTest() {
        dataSourceFactory.resetPort(value -> 3307);
        assertEquals((Integer)3307, dataSourceFactory.getPort());
    }

    @Test
    public void resetSchemaTest() {
        dataSourceFactory.resetSchema(value -> "test_schema");
        assertEquals("test_schema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void resetParametersTest() {
        dataSourceFactory.resetParameters(value -> {
            value.put("param1", "value1");
            return value;
        });
        assertTrue(dataSourceFactory.getParameters().containsKey("param1"));
        assertEquals("value1", dataSourceFactory.getParameters().get("param1"));
    }
}
