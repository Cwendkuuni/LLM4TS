package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setUsername("admin");
        connectionConfig.setPassword("admin");
        connectionConfig.setDefaultSchema("test-schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        Boolean autoCommit = true;
        factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }
    
    @Test
    public void testGetJdbcUrl() {
        assertNotNull(factory.getJdbcUrl());
    }
    
    @Test
    public void testGetDefaultSchema() {
        assertEquals("test-schema", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
    }
    
    @Test
    public void testGetUsername() {
        assertEquals("admin", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }
    
    @Test
    public void testGetPassword() {
        assertEquals("admin", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }
    
    @Test
    public void testGetJdbcParams() {
        assertNotNull(OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory cloneFactory = factory.deepCopy();
        assertNotNull(cloneFactory);
        assertNotSame(factory, cloneFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortNegetive() {
        factory.resetPort(value -> -1);
    }

    @Test
    public void testResetPortPositive() {
        factory.resetPort(value -> 8888);
        assertEquals(8888, factory.getPort().intValue());
    }

    @Test
    public void testResetUsername() {
        factory.resetUsername(value -> "new-username");
        assertEquals("new-username", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        factory.resetPassword(value -> "new-password");
        assertEquals("new-password", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        factory.resetHost(value -> "new-host");
        assertEquals("new-host", factory.getHost());
    }

    @Test
    public void testResetSchema() {
        factory.resetSchema(value -> "new-schema");
        assertEquals("new-schema", factory.getSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("new-param", "new-value");
        factory.resetParameters(value -> newParams);
        assertEquals(newParams, factory.getParameters());
    }
}
