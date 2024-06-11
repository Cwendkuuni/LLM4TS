package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@Slf4j
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations = mock(JdbcOperations.class);
    private Connection connection = mock(Connection.class);

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(1);
        extension.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperations).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        when(jdbcOperations.execute(Mockito.anyString())).thenThrow(new SQLException("Test Exception"));
        extension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = "currentSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testSwitchSchemaToSameSchema() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = schemaName;
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException("Test Exception"));
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = "currentSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String connectionId = "connectionId";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullQueryResult() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        String connectionId = "";
        if (connection instanceof OceanBaseConnection) {
            connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
        }

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String connectionId = "123456";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(connectionId);
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        when(connection.unwrap(Mockito.eq(OceanBaseConnection.class))).thenReturn(oceanBaseConnection);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }
}