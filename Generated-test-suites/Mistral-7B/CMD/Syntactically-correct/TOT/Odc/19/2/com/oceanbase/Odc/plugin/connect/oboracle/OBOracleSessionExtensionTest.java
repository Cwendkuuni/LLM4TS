package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for {@link OBOracleSessionExtension}.
 */
public class OBOracleSessionExtensionTest {

    private static final String TEST_SCHEMA = "test_schema";
    private static final String TEST_CONNECTION_ID = "test_connection_id";
    private JdbcOperations jdbcOperationsMock;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperations(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = TEST_CONNECTION_ID;

        when(jdbcOperationsMock.execute(Mockito.anyString())).thenReturn(null);

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        verify(jdbcOperationsMock, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_SQLException() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = TEST_CONNECTION_ID;

        when(jdbcOperationsMock.execute(Mockito.anyString())).thenThrow(new SQLException("Test SQLException"));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = TEST_SCHEMA;
        String currentSchema = "current_schema";

        when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        verify(jdbcOperationsMock, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testSwitchSchema_CurrentSchemaIsTheSame() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = TEST_SCHEMA;
        String currentSchema = schemaName;

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        verify(jdbcOperationsMock, never()).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SQLException() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = TEST_SCHEMA;

        when(jdbcOperationsMock.update(Mockito.anyString())).thenThrow(new SQLException("Test SQLException"));

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String expectedSchema = "expected_schema";

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedSchema);

        assertEquals(expectedSchema, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = TEST_CONNECTION_ID;

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionId_ConnectionIsNull() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_NoConnectionId() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        OceanBaseConnection oceanBaseConnectionMock = Mockito.mock(OceanBaseConnection.class);

        when(connectionMock.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connectionMock.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnectionMock);
        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(12345);

        assertEquals("12345", obOracleSessionExtension.getConnectionId(connectionMock));
    }
}