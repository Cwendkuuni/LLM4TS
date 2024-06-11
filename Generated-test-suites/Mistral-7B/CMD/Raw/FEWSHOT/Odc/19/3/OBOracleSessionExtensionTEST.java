package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setUp() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension() {
            {
                this.jdbcOperations = jdbcOperationsMock;
            }
        };
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "123";
        String query = "KILL QUERY " + connectionId;

        when(jdbcOperationsMock.update(query)).thenReturn(1);
        obOracleSessionExtension.killQuery(connectionMock, connectionId);
        verify(jdbcOperationsMock, times(1)).update(query);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWhenExceptionThrown() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String connectionId = "123";
        String query = "KILL QUERY " + connectionId;

        when(jdbcOperationsMock.update(query)).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);

        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        verify(jdbcOperationsMock, times(1)).update(alterSql);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWhenExceptionThrown() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String schemaName = "test_schema";

        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "current_schema";

        when(jdbcOperationsMock.queryForObject(querySql, String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "123";

        when(jdbcOperationsMock.queryForObject(querySql, String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWhenConnectionInstanceIsOceanBaseConnection() {
        Connection oceanBaseConnectionMock = Mockito.mock(OceanBaseConnection.class);
        String connectionId = "123";

        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(oceanBaseConnectionMock));
    }

    @Test
    public void testGetConnectionIdWhenExceptionThrown() {
        Connection connectionMock = Mockito.mock(Connection.class);

        try {
            obOracleSessionExtension.getConnectionId(connectionMock);
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // Expected
        }
    }
}