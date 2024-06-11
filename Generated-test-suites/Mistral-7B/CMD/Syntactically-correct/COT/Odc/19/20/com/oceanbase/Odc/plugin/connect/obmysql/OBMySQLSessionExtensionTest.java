package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.ExtensionRegistry;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionRegistry;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(org.junit.runner.JUnit4.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        extension.killQuery(connection, "testQueryId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        extension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionRegistry extensionRegistry = new DefaultExtensionRegistry();
        ExtensionManager extensionManager = new DefaultExtensionManager(extensionRegistry);
        extensionManager.loadExtensions();
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        String currentSchema = extensionPoint.getCurrentSchema(connection);
        extension.switchSchema(connection, "testSchema");
        assertEquals("testSchema", extensionPoint.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaToSameSchema() throws Exception {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionRegistry extensionRegistry = new DefaultExtensionRegistry();
        ExtensionManager extensionManager = new DefaultExtensionManager(extensionRegistry);
        extensionManager.loadExtensions();
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        String currentSchema = extensionPoint.getCurrentSchema(connection);
        extension.switchSchema(connection, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws Exception {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionRegistry extensionRegistry = new DefaultExtensionRegistry();
        ExtensionManager extensionManager = new DefaultExtensionManager(extensionRegistry);
        extensionManager.loadExtensions();
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        extension.switchSchema(connection, null);
    }

    @Test
    public void testgetCurrentSchema() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        String currentSchema = extension.getCurrentSchema(connection);
        assertNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        String connectionId = extension.getConnectionId(connection);
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:oceanbase:local:1527/testdb");
        String connectionId = extension.getConnectionId(connection);
        assertEquals("", connectionId);
    }
}