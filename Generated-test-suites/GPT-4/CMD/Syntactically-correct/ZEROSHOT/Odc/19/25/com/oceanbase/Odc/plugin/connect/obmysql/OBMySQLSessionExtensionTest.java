
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private String schemaName;
    private String connectionId;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
        schemaName = "testSchema";
        connectionId = "123";
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() {
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(schemaName);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        String currentConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, currentConnectionId);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaShouldThrowException() throws Exception {
        doThrow(new SQLException()).when(connection).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(connection, anyString());
    }
}
