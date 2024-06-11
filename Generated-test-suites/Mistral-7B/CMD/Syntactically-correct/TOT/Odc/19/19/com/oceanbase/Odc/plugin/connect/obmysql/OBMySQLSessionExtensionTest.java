package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations(Connection connection) throws SQLException {
                return jdbcOperationsMock;
            }
        };
    }

    @Test
    public void testKillQuerySuccess() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        when(jdbcOperationsMock.execute(Mockito.anyString())).thenReturn(null);

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
        verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryFailure() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        when(jdbcOperationsMock.execute(Mockito.anyString())).thenThrow(new SQLException("Test exception"));

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = "test_schema_name";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(schemaName);

        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
        verify(connectionMock).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFailure() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = "test_schema_name";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException("Test exception"));

        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String currentSchema = "test_current_schema";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionIdFromConnection() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        when(connectionMock.getCatalog()).thenReturn("test_catalog");
        when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdFromJdbcOperations() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdNullFromJdbcOperations() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdEmptyStringFromJdbcOperations() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("");

        String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertNull(connectionId);
    }
}