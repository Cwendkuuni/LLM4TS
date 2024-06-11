package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;

/**
 * Test class for OBOracleSessionExtension
 */
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = getConnection();
        String connectionId = "123456";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithInvalidConnection() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = null;
        String connectionId = "123456";
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithInvalidConnectionId() {
        Connection connection = getConnection();
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = getConnection();
        String schemaName = "testSchema";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaEqual() throws SQLException {
        Connection connection = getConnection();
        String schemaName = "testSchema";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithInvalidConnection() {
        Connection connection = null;
        String schemaName = "testSchema";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        Connection connection = getConnection();
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(connection, ""));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = getConnection();
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertEquals("DEFAULT", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = getConnection();
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection connection = getOceanBaseConnection();
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        assertEquals(connectionId, ((OceanBaseConnection) connection).getServerThreadId());
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "username", "password");
    }

    private Connection getOceanBaseConnection() throws SQLException {
        return new OceanBaseConnection("jdbc:obcd:orcl:1521/orcl:user=username:password=password");
    }
}