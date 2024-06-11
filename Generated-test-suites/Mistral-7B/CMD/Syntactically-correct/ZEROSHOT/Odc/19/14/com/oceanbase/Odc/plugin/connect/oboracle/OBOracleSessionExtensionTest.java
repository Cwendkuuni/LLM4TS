package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension
 */
public class OBOracleSessionExtensionTest {

    private static Connection connection;
    private static ExtensionManager extensionManager;
    private static OBOracleSessionExtension obOracleSessionExtension;

    @BeforeClass
    public static void setup() throws SQLException, ClassNotFoundException {
        Class.forName("com.oceanbase.jdbc.driver.OceanBaseDriver");
        Properties props = new Properties();
        props.setProperty("user", "testuser");
        props.setProperty("password", "testpass");
        props.setProperty("oracle.net.connectString.OceanBase", "localhost:1527/obtest");
        connection = DriverManager.getConnection("jdbc:oceanbase:obtest:1527:testdb", props);
        extensionManager = new ExtensionManager();
        extensionManager.init(OBOracleSessionExtensionTest.class.getResource("/META-INF/pf4j-extensions.xml"));
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class)[0];
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "testQueryId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        assertEquals("testdb", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        assertEquals(connection.hashCode() + "", obOracleSessionExtension.getConnectionId(connection));
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection obConnection = (OceanBaseConnection) connection;
            assertEquals(obConnection.getServerThreadId() + "", obOracleSessionExtension.getConnectionId(connection));
        }
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }
}