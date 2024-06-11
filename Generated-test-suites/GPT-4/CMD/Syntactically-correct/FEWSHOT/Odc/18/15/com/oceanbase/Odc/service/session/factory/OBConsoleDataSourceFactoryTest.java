
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup(){
        connectionConfig = new ConnectionConfig();
        // Fill connectionConfig with required values
        // connectionConfig.set... 
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource(){
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testDeepCopy(){
        CloneableDataSourceFactory cloneFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(cloneFactory);
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newUsername";
        obConsoleDataSourceFactory.resetUsername(mapper);
        assertEquals("newUsername", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newPassword";
        obConsoleDataSourceFactory.resetPassword(mapper);
        assertEquals("newPassword", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newHost";
        obConsoleDataSourceFactory.resetHost(mapper);
        assertEquals("newHost", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> 8080;
        obConsoleDataSourceFactory.resetPort(mapper);
        assertEquals(Integer.valueOf(8080), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newSchema";
        obConsoleDataSourceFactory.resetSchema(mapper);
        assertEquals("newSchema", obConsoleDataSourceFactory.getSchema());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> new HashMap<>();
        obConsoleDataSourceFactory.resetParameters(mapper);
        assertTrue(obConsoleDataSourceFactory.getParameters().isEmpty());
    }

    // Add more tests as required for each method in OBConsoleDataSourceFactory
}
