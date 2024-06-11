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
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private JdbcOperationsUtil jdbcOperationsUtil;
    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        jdbcOperations = mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        when(jdbcOperationsUtil.getJdbcOperations(Mockito.any())).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
        String querySql = "KILL QUERY " + connectionId;
        when(jdbcOperations.execute(querySql)).thenReturn(Optional.empty());
        assertEquals(0, obOracleSessionExtension.killQuery(connection, connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(jdbcOperations.execute(Mockito.anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        String expectedAlterSql = "ALTER SESSION SET CURRENT_SCHEMA=testSchema";

        when(getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        when(jdbcOperations.update(alterSql)).thenReturn(1);
        assertEquals(currentSchema, getCurrentSchema(connection));
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        String expectedAlterSql = "ALTER SESSION SET CURRENT_SCHEMA=testSchema";

        when(getCurrentSchema(connection)).thenReturn(currentSchema);
        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String expectedCurrentSchema = "expectedCurrentSchema";

        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn(expectedCurrentSchema);
        assertEquals(expectedCurrentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(getConnectionId(connection)).thenReturn(null);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123);
        assertEquals("123", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    private String getCurrentSchema(Connection connection) {
        return obOracleSessionExtension.getCurrentSchema(connection);
    }

    private String getConnectionId(Connection connection) {
        return obOracleSessionExtension.getConnectionId(connection);
    }
}