package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private Boolean autoCommit;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        autoCommit = true;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(dataSourceFactory.getDialectType(), connectionConfig.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
        assertNotNull(copy);
        assertEquals(copy.getDialectType(), dataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        String username = "testUser";
        dataSourceFactory.resetUsername(val -> username);
        assertEquals(dataSourceFactory.getUsername(), username);
    }

    @Test
    public void testResetPassword() {
        String password = "testPassword";
        dataSourceFactory.resetPassword(val -> password);
        assertEquals(dataSourceFactory.getPassword(), password);
    }

    @Test
    public void testGetJdbcUrl() {
        // Add test cases for different scenarios of generating jdbc URL
        // Eg: connectionConfig with different components like host, port, schema
    }

    // Add more test cases for other public methods if needed

}
