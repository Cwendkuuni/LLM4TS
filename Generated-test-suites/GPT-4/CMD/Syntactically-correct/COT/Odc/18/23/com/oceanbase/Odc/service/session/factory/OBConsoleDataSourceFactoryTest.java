package com.oceanbase.odc.service.session.factory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import  com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;

public class OBConsoleDataSourceFactoryTest {

    ConnectionConfig connectionConfig = new ConnectionConfig();
    
    @Test
    public void testGetUsername() {
        OBConsoleDataSourceFactory dsf = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(dsf.getUsername());
    }

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory dsf = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(dsf.getJdbcUrl());
    }

    @Test
    public void testGetPassword() {
        OBConsoleDataSourceFactory dsf = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(dsf.getPassword());
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory dsf = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(dsf.getDataSource());
    }

    @Test
    public void testGetJdbcParams() {
        HashMap<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(params);
        assertEquals("false", params.get("allowLoadLocalInfile"));
    }

    // Add more test methods here....
}