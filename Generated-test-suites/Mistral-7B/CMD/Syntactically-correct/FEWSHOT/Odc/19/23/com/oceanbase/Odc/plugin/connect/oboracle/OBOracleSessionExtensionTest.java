package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperationsMock;

    @Test
    public void testKillQuery() {
        connection = mock(Connection.class);
        jdbcOperationsMock = mock(JdbcOperations.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsMock);

        obOracleSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperationsMock).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        connection = mock(Connection.class);
        jdbcOperationsMock = mock(JdbcOperations.class);
        StringUtils stringUtilsMock = mock(StringUtils.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsMock);
        when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("previousSchema");
        when(stringUtilsMock.quoteOracleIdentifier(Mockito.anyString())).thenReturn("schemaName");

        obOracleSessionExtension.switchSchema(connection, "schemaName");
        Mockito.verify(jdbcOperationsMock).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertSame("previousSchema", currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        connection = mock(Connection.class);
        jdbcOperationsMock = mock(JdbcOperations.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsMock);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("schemaName");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertSame("schemaName", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        connection = mock(Connection.class);
        JdbcOperations jdbcOperationsMock = mock(JdbcOperations.class);
        OceanBaseConnection oceanBaseConnectionMock = mock(OceanBaseConnection.class);

        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsMock);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnectionMock);
        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(123L);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertSame("123", connectionId);
    }

    @Test
    public void testGetConnectionIdNull() {
        connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}