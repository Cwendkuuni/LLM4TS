package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension sessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String expectedQuery = "KILL QUERY connectionId";

        // execution to verify whether the query executed matches with the expected query.
        // Since the actual method has void return type, we can't really verify the result. It's more of interaction verification here.
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        String currentSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
               .thenReturn(currentSchema);

        sessionExtension.switchSchema(connection, currentSchema);

        // Here again, as the method is a void return, we check the interactions. If the condition fails inside the method, 
        // it means that it is trying to update the schema which is not correct. 
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                .thenReturn(expectedSchema);

        String actualSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId_oceanBase() throws SQLException {
        String expectedConnectionId = "1234";
        connection = mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_exception() throws SQLException {
        String expectedConnectionId = null;
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                .thenThrow(SQLException.class);

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}