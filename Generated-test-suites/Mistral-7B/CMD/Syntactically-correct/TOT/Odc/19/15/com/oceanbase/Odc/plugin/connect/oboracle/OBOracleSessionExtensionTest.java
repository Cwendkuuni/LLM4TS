package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.exception.ODCPluginException;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Extension
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "test_query_id");
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY test_query_id");
    }

    @Test
    public void testKillQuery_whenConnectionIsNull_thenThrowsException() {
        assertThrows(ODCPluginException.class, () -> obOracleSessionExtension.killQuery(null, "test_query_id"));
    }

    @Test
    public void testKillQuery_whenQueryIdIsNull_thenDoNothing() {
        obOracleSessionExtension.killQuery(connection, null);
        Mockito.verify(connection, Mockito.never()).createStatement();
        Mockito.verify(connection, Mockito.never()).execute("KILL QUERY null");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = "test_schema";
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(newSchema, currentSchema);
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchema));
    }

    @Test
    public void testSwitchSchema_whenConnectionIsNull_thenThrowsException() {
        assertThrows(ODCPluginException.class, () -> obOracleSessionExtension.switchSchema(null, "test_schema"));
    }

    @Test
    public void testSwitchSchema_whenSchemaNameIsNull_thenDoNothing() {
        obOracleSessionExtension.switchSchema(connection, null);
        Mockito.verify(connection, Mockito.never()).createStatement();
        Mockito.verify(connection, Mockito.never()).execute("ALTER SESSION SET CURRENT_SCHEMA=null");
    }

    @Test
    public void testSwitchSchema_whenCurrentSchemaIsSame_thenDoNothing() throws SQLException {
        String currentSchema = "test_schema";
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, currentSchema);
        Mockito.verify(connection, Mockito.never()).createStatement();
        Mockito.verify(connection, Mockito.never()).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(currentSchema));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_whenConnectionIsNull_thenReturnsNull() {
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "test_connection_id";
        Mockito.when(connection.getServerThreadId()).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_whenConnectionIsNull_thenReturnsNull() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_whenConnectionDoesntSupportGetServerThreadId_thenUsesConnectionToString() {
        Mockito.doThrow(SQLException.class).when(connection).getServerThreadId();
        OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
        Mockito.when(oceanBaseConnection.getConnectionString()).thenReturn("test_connection_string");
        assertEquals("test_connection_string", obOracleSessionExtension.getConnectionId(connection));
    }
}