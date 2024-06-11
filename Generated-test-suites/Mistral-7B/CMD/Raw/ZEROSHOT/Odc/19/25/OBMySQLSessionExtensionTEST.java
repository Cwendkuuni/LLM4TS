package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestFramework;
import org.pf4j.util.ClassUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(TestFramework.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() throws Exception {
        extensionManager = TestFramework.getExtensionManager();
        pluginWrapper = extensionManager.getPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        extensionManager.loadPlugin(pluginWrapper);
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(OBMySQLSessionExtension.class);
    }

    @After
    public void teardown() {
        extensionManager.unloadPlugin(pluginWrapper);
    }

    @Test
    public void testKillQuery() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceansbase:localhost:2880/testDB");
        try {
            obMySQLSessionExtension.killQuery(connection, "1");
        } finally {
            connection.close();
        }
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceansbase:localhost:2880/testDB1");
        try {
            obMySQLSessionExtension.switchSchema(connection, "testDB2");
            assertEquals("testDB2", obMySQLSessionExtension.getCurrentSchema(connection));
        } finally {
            connection.close();
            obMySQLSessionExtension.switchSchema(connection, "testDB1");
        }
    }

    @Test
    public void testgetCurrentSchema() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceansbase:localhost:2880/testDB");
        try {
            String schemaName = "testDB2";
            obMySQLSessionExtension.switchSchema(connection, schemaName);
            assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
            obMySQLSessionExtension.switchSchema(connection, "testDB1");
            assertEquals("testDB1", obMySQLSessionExtension.getCurrentSchema(connection));
        } finally {
            connection.close();
        }
    }

    @Test
    public void testGetConnectionId() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceansbase:localhost:2880/testDB");
        try {
            String connectionId = obMySQLSessionExtension.getConnectionId(connection);
            assertNotNull(connectionId);
        } finally {
            connection.close();
        }
    }
}