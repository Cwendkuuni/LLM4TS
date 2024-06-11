package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySqlSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySqlSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection), Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testDB";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(schemaName);
        obMySqlSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection, Mockito.never()).setCatalog(Mockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testDB";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = obMySqlSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "123";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String actualConnectionId = obMySqlSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        String expectedConnectionId = "123";
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedConnectionId));
        String actualConnectionId = obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
