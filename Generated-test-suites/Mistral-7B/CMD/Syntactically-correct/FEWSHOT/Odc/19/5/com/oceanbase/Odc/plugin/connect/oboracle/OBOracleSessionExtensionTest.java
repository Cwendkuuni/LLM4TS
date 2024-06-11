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

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;
    private JdbcOperations mockJdbcOperations;

    @Before
    public void setUp() {
        mockJdbcOperations = mock(JdbcOperations.class);
        mockConnection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        when(mockConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(mockJdbcOperations.update(Mockito.anyString()))
                .thenReturn(Optional.of(1));
        when(mockJdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenAnswer(invocation -> invocation.getArgument(0).substring(1, invocation.getArgument(0).length() - 1));
    }

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(mockConnection, "connectionId");
        Mockito.verify(mockConnection).createStatement();
        Mockito.verify(mockConnection).createStatement().execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema_ValidSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockJdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(currentSchema);
        sessionExtension.switchSchema(mockConnection, schemaName);
        Mockito.verify(mockConnection).createStatement();
        Mockito.verify(mockConnection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockJdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(currentSchema);
        sessionExtension.switchSchema(mockConnection, schemaName);
        Mockito.verify(mockConnection).createStatement();
        Mockito.verify(mockConnection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        throw new SQLException("Unexpected error");
    }

    @Test
    public void testgetCurrentSchema() {
        String currentSchema = "currentSchema";
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockJdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(currentSchema);
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId_ValidConnection() {
        String connectionId = "connectionId";
        assertEquals(connectionId, sessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionId_InvalidConnection() {
        assertNull(sessionExtension.getConnectionId(mockConnection));
    }
}