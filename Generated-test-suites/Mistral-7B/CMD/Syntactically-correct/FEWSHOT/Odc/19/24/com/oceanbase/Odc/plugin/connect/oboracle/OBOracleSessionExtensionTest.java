package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperationsUtil((JdbcOperationsUtil) () -> jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connection_1";
        when(jdbcOperations.execute(ArgumentCaptor.capture(String.class))).thenReturn(null);

        obOracleSessionExtension.killQuery(connection, connectionId);
        assertEquals("KILL QUERY " + connectionId, ArgumentCaptor.forClass(String.class).getValue());
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(jdbcOperations.update(ArgumentCaptor.capture(String.class))).thenReturn(1);
        when(jdbcOperations.queryForObject(ArgumentCaptor.capture(String.class), String.class)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=test_schema", ArgumentCaptor.forClass(String.class).getValue());
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaFailure() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(jdbcOperations.queryForObject(ArgumentCaptor.capture(String.class), String.class)).thenReturn(currentSchema);
        when(jdbcOperations.update(ArgumentCaptor.capture(String.class))).thenThrow(new SQLException("Error switching schema"));

        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=test_schema", ArgumentCaptor.forClass(String.class).getValue());
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "current_schema";

        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "connection_id_1";

        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));

        when(jdbcOperations.queryForObject(querySql, String.class)).thenThrow(new SQLException("Error getting connection id"));
        assertNull(obOracleSessionExtension.getConnectionId(connection));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals("server_thread_id", obOracleSessionExtension.getConnectionId(connection));
    }
}