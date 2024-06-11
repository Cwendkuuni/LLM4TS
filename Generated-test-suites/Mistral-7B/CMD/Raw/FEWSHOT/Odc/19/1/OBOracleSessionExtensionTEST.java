package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        jdbcOperations = mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperationsUtil(new JdbcOperationsUtil(jdbcOperations));
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connectionId";
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY " + connectionId))).thenReturn(1);

        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWhenExecuteThrowsException() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connectionId";
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY " + connectionId)))
                .thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "schemaName";
        String currentSchema = "currentSchema";

        when(jdbcOperations.update(Mockito.eq("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)))).thenReturn(1);
        when(jdbcOperations.queryForObject(Mockito.eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), Mockito.eq(String.class))).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsSame() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "schemaName";
        String currentSchema = schemaName;

        obOracleSessionExtension.switchSchema(connection, schemaName);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWhenUpdateThrowsException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "schemaName";

        when(jdbcOperations.update(Mockito.eq("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)))).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "currentSchema";

        when(jdbcOperations.queryForObject(Mockito.eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connectionId";

        when(jdbcOperations.queryForObject(Mockito.eq("select userenv('sessionid') from dual"), Mockito.eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWhenExceptionOccurs() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = null;

        when(jdbcOperations.queryForObject(Mockito.eq("select userenv('sessionid') from dual"), Mockito.eq(String.class))).thenThrow(new SQLException("Test Exception"));

        if (connection instanceof OceanBaseConnection) {
            connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
        }

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }
}