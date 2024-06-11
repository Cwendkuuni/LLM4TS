package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.common.util.StringUtils;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        when(connection.createStatement()).thenReturn(mock(Statement.class));
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        extension.killQuery(connection, "connectionId");

        Mockito.verify(jdbcOperations).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        StringUtils stringUtils = mock(StringUtils.class);

        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);
        when(connection.createStatement()).thenReturn(mock(Statement.class));
        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);
        when(stringUtils.quoteOracleIdentifier(Mockito.anyString())).thenReturn("schemaName");

        extension.switchSchema(connection, "schemaName");

        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName");
    }

    @Test
    public void testSwitchSchemaFailed() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);
        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException("Error switching schema"));

        assertThrows(SQLException.class, () -> extension.switchSchema(connection, "schemaName"));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        String currentSchema = extension.getCurrentSchema(connection);

        Mockito.verify(jdbcOperations).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
        assertEquals("CURRENT_SCHEMA", currentSchema);
    }

    @Test
    public void testGetConnectionIdFromConnection() throws Exception {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        String connectionId = extension.getConnectionId(connection);

        if (connection instanceof OceanBaseConnection) {
            Mockito.verify(connection).getServerThreadId();
            assertEquals("serverThreadId", connectionId);
        } else {
            assertNull(connectionId);
        }
    }

    @Test
    public void testGetConnectionIdFromNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        assertNull(extension.getConnectionId(null));
    }
}