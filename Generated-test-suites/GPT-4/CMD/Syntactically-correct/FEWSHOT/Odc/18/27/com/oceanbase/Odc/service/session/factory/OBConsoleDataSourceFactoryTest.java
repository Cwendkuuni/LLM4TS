package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.datasource.DataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        dataSourceFactory.resetUsername(value -> value + "_test");
        assertEquals("Expected username appended with _test", dataSourceFactory.getUsername() + "_test", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        dataSourceFactory.resetPassword(value -> value + "_test");
        assertEquals("Expected password appended with _test", dataSourceFactory.getPassword() + "_test", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        DialectType dt = dataSourceFactory.getDialectType();
        assertNotNull(dt);
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = dataSourceFactory.getDataSource();
        assertNotNull(ds);
    }

    @Test
    public void testResetHost() {
        String initialHost = dataSourceFactory.getHost();
        dataSourceFactory.resetHost(value -> value + "_test");
        assertEquals("Expected host appended with _test", initialHost + "_test", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer initialPort = dataSourceFactory.getPort();
        dataSourceFactory.resetPort(value -> value + 1);
        assertEquals("Expected port incremented by 1", initialPort + 1, dataSourceFactory.getPort());
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters(value -> {
            value.put("testKey", "testValue");
            return value;
        });
        assertEquals("Expected additional parameter added", "testValue", dataSourceFactory.getParameters().get("testKey"));
    }
    
    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        assertNotEquals("Deep copy should not equal original", dataSourceFactory, deepCopy);
    }
}