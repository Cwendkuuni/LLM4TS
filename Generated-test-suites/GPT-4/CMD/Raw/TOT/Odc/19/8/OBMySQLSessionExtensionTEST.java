
package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
        jdbcOperations = Mockito.mock(JdbcOperations.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "100";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        obMySQLSessionExtension.killQuery(connection, connectionId);

        verify(jdbcOperations, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(0)).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        String currentSchema = "oldSchema";
        String newSchema = "newSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, newSchema);

        verify(connection, times(1)).setCatalog(newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        obMySQLSessionExtension.getCurrentSchema(connection);

        verify(jdbcOperations, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionIdNormalConn() {
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        obMySQLSessionExtension.getConnectionId(connection);
        
        verify(jdbcOperations, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionIdOceanBaseConn() {
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
        doThrow(new RuntimeException()).when(jdbcOperations).queryForObject(any(), any());

        obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        
        verify(oceanBaseConnection, times(1)).getServerThreadId();
    }
}
