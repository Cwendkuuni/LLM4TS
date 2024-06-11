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

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() throws SQLException {
        connection = Mockito.mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        String currentSchema = "testCurrentSchema";
        String schemaName = "testSchema";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeUpdate(Mockito.anyString())).thenReturn(1);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_FailedToSwitch() throws SQLException {
        Mockito.when(connection.createStatement()).thenThrow(new SQLException("Failed to switch schema"));
        obOracleSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "testCurrentSchema";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).next()).thenReturn(true);
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).getString(Mockito.eq(1))).thenReturn(currentSchema);
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).next()).thenReturn(true);
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).getObject(Mockito.eq(1))).thenReturn(connectionId);
        String result = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionId_NotOceanBaseConnection() {
        String result = obOracleSessionExtension.getConnectionId(connection);
        assertNull(result);
    }
}