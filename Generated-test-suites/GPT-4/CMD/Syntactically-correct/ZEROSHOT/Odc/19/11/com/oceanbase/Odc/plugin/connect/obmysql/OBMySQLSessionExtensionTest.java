
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection connection;

    @Before
    public void setup() {
        extension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        // Execute
        extension.switchSchema(connection, schemaName);

        // Verify
        verify(connection, times(0)).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaDiff() throws SQLException {
        String schemaName = "testSchema";
        when(extension.getCurrentSchema(connection)).thenReturn("diffSchema");

        // Execute
        extension.switchSchema(connection, schemaName);

        // Verify
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn("1234");

        // Execute & Verify
        assert extension.getConnectionId(connection).equals("1234");
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        Connection obConnection = Mockito.mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) obConnection).getServerThreadId()).thenReturn(1234L);

        // Execute & Verify
        assert extension.getConnectionId(obConnection).equals("1234");
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        extension.killQuery(connection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn("testSchema");

        // Execute & Verify
        assert extension.getCurrentSchema(connection).equals("testSchema");
    }
}
