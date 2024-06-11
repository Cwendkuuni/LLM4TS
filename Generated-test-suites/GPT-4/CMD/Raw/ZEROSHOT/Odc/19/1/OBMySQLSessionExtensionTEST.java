
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "100";
        sessionExtension.killQuery(connection, connectionId);
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TestSchema";
        when(connection.getCurrentSchema()).thenReturn(schemaName);
        sessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        String expectedSchema = "TestSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
                .thenReturn(expectedSchema);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        String expectedId = "100";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
                .thenReturn(expectedId);
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals(expectedId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        String querySql = "select connection_id()";
        String expectedId = "200";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
                .thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.valueOf(expectedId));
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals(expectedId, connectionId);
    }
}
