package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginWrapper;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private static final String OB_DRIVER_CLASS_NAME = "com.oceanbase.jdbc.OceanBaseDriver";
    private static final String OB_DB_URL = "jdbc:obnew:127.0.0.1:3306/testDB";
    private static final String OB_USER = "testUser";
    private static final String OB_PASSWORD = "testPassword";

    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;
    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        Class.forName(OB_DRIVER_CLASS_NAME);
        extensionManager = new ExtensionManager();
        pluginWrapper = new PluginWrapper("OBOracleSessionExtensionTest", "testVersion");
        extensionManager.addExtension(pluginWrapper);
        connection = DriverManager.getConnection(OB_DB_URL, OB_USER, OB_PASSWORD);
    }

    @After
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        extensionManager.shutdown();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add assertions here to test the result of killing a query
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class);
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        // Add assertions here to test the result of getting the current schema
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class);
        Optional<String> connectionIdOptional = Optional.ofNullable(obOracleSessionExtension.getConnectionId(connection));
        String connectionId = connectionIdOptional.orElse("defaultConnectionId");
        // Add assertions here to test the result of getting the connection id
    }
}