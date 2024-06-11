package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.Extensions;
import org.pf4j.TestExtension;
import org.pf4j.util.Pair;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@TestExtension
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() throws SQLException {
        connection = JdbcOperationsUtil.getConnection();
        obOracleSessionExtension = Extensions.getExtension(SessionExtensionPoint.class, "OBOracleSessionExtension");
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(null, "testConnectionId"));
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchemaWithValidParameters() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.switchSchema(null, "testSchema"));
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() {
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(connection, ""));
    }

    @Test
    public void testSwitchSchemaWithSameSchemaName() throws SQLException {
        String schemaName = getCurrentSchema(connection);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String invalidSchemaName = "testInvalidSchemaName";
        obOracleSessionExtension.switchSchema(connection, invalidSchemaName);
    }

    @Test
    public void testGetCurrentSchemaWithValidConnection() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        String connectionSchema = getCurrentSchema(connection);
        assertEquals(connectionSchema, currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionIdWithValidConnection() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        Pair<String, String> connectionInfo = JdbcOperationsUtil.getConnectionInfo(connection);
        assertEquals(connectionInfo.getFirst(), connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String oceanBaseConnectionId = obOracleSessionExtension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
            assertEquals(oceanBaseConnection.getServerThreadId() + "", oceanBaseConnectionId);
        }
    }

    @Test
    public void testGetConnectionIdWithOtherConnection() {
        String otherConnectionId = obOracleSessionExtension.getConnectionId(new Object() {}.toString().getBytes());
        assertEquals(null, otherConnectionId);
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }
}