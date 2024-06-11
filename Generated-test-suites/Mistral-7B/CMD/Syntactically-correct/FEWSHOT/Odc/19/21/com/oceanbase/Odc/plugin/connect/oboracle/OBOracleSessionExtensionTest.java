package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperationsMock;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        jdbcOperationsMock = mock(JdbcOperations.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsMock);

        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQueryNullConnection() {
        obOracleSessionExtension.killQuery(null, "connectionId");
        // No need to assert anything, as killQuery() doesn't throw an exception when the Connection is null
    }

    @Test
    public void testKillQueryValidConnection() {
        when(jdbcOperationsMock.execute("KILL QUERY connectionId")).thenReturn(1);
        obOracleSessionExtension.killQuery(connection, "connectionId");
        // No need to assert anything, as killQuery() doesn't return anything
    }

    @Test(expected = SQLException.class)
    public void testKillQueryException() {
        when(jdbcOperationsMock.execute("KILL QUERY connectionId")).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchemaNullConnection() {
        obOracleSessionExtension.switchSchema(null, "schemaName");
        // No need to assert anything, as switchSchema() doesn't throw an exception when the Connection is null
    }

    @Test
    public void testSwitchSchemaValidConnection() throws SQLException {
        String currentSchema = "currentSchema";
        String schemaName = "newSchema";

        when(jdbcOperationsMock.update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenReturn(1);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals("newSchema", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        String currentSchema = "currentSchema";
        String schemaName = "newSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        when(jdbcOperationsMock.update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchemaNullConnection() {
        obOracleSessionExtension.getCurrentSchema(null);
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetCurrentSchemaValidConnection() throws SQLException {
        String schemaName = "schemaName";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);

        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        obOracleSessionExtension.getConnectionId(null);
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(null);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345L);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException("Test Exception"));

        Optional<String> connectionId = Optional.ofNullable(obOracleSessionExtension.getConnectionId(connection));
        assertFalse(connectionId.isPresent());
    }
}