package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension
 *
 * @author yaobin
 * @since 4.2.0
 */
@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        connection = Mockito.spy(Mockito.mock(Connection.class));
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_ExceptionThrown() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SchemaNameIsEmpty() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SchemaNameIsNull() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeQuery(querySql)).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().next()).thenReturn(true);
        Mockito.when(connection.createStatement().getResultSet().getString(1)).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchema_ExceptionThrown() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        String querySql = "select userenv('sessionid') from dual";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeQuery(querySql)).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().next()).thenReturn(true);
        Mockito.when(connection.createStatement().getResultSet().getString(1)).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_ConnectionIsNull() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_ConnectionIsOceanBaseConnection() {
        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connection.toString(), connectionId);
    }
}