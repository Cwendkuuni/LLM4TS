package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for {@link OBOracleSessionExtension}
 */
public class OBOracleSessionExtensionTest {

    private static Connection connection;
    private static SessionExtensionPoint sessionExtension;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.oceanbase.jdbc.driver.OceanBaseDriver");
        connection = DriverManager.getConnection("jdbc:obproxy:127.0.0.1:10000/testDB", "user", "password");
        sessionExtension = new OBOracleSessionExtension();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Test for killQuery method with valid connection and connectionId
     */
    @Test
    public void testKillQuerySuccess() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        sessionExtension.killQuery(connection, connectionId);
        // Add assertion for the verification of the query killing
    }

    /**
     * Test for killQuery method with null connection
     */
    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnection() {
        sessionExtension.killQuery(null, "connectionId");
    }

    /**
     * Test for killQuery method with null connectionId
     */
    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        sessionExtension.killQuery(connection, null);
    }

    /**
     * Test for switchSchema method with valid connection and schemaName
     */
    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);
        String newSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
    }

    /**
     * Test for switchSchema method with null connection
     */
    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaWithNullConnection() {
        sessionExtension.switchSchema(null, "schemaName");
    }

    /**
     * Test for switchSchema method with null schemaName
     */
    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        sessionExtension.switchSchema(connection, null);
    }

    /**
     * Test for getCurrentSchema method with valid connection
     */
    @Test
    public void testgetCurrentSchemaSuccess() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    /**
     * Test for getCurrentSchema method with null connection
     */
    @Test(expected = NullPointerException.class)
    public void testgetCurrentSchemaWithNullConnection() {
        sessionExtension.getCurrentSchema(null);
    }

    /**
     * Test for getConnectionId method with valid connection
     */
    @Test
    public void testGetConnectionIdSuccess() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    /**
     * Test for getConnectionId method with null connection
     */
    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdWithNullConnection() {
        sessionExtension.getConnectionId(null);
    }

    /**
     * Test for getConnectionId method with OceanBaseConnection
     */
    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = sessionExtension.getConnectionId((OceanBaseConnection) connection);
        assertNotNull(connectionId);
    }
}