package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint sessionExtensionPoint;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() throws SQLException {
        connection = Mockito.mock(Connection.class);
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        sessionExtensionPoint = new OBOracleSessionExtension();
        sessionExtensionPoint.setJdbcOperations(jdbcOperationsMock);
    }

    @After
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "some_connection_id";
        sessionExtensionPoint.killQuery(connection, connectionId);
        verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnectionId() {
        sessionExtensionPoint.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        sessionExtensionPoint.switchSchema(connection, schemaName);
        String expectedSchema = schemaName;
        verify(jdbcOperationsMock).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        verify(jdbcOperationsMock).queryForObject(Mockito.anyString(), Mockito.eq(String.class)).thenReturn(expectedSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        sessionExtensionPoint.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "expected_schema";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedSchema);

        String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "some_connection_id";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.query(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.query(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        sessionExtensionPoint.getConnectionId(connection);
        assertEquals(connectionId, sessionExtensionPoint.getConnectionId(connection));

        when(connection.query(Mockito.anyString())).thenThrow(new SQLException());

        sessionExtensionPoint.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
            assertEquals(oceanBaseConnection.getServerThreadId() + "", sessionExtensionPoint.getConnectionId(connection));
        }
    }
}