package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBMySQLSessionExtension obMySqlSessionExtension;

    @Before
    public void setup() {
        obMySqlSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "test";
        obMySqlSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(obMySqlSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySqlSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        obMySqlSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String querySql = "select connection_id()";
        String expectedConnectionId = "testConnectionId";
        when(connection.queryForObject(querySql, String.class)).thenReturn(expectedConnectionId);
        String connectionId = obMySqlSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, connectionId); 
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        String querySql = "select connection_id()";
        when(connection.queryForObject(querySql, String.class)).thenThrow(new SQLException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        String expectedConnectionId = "123";
        String connectionId = obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedConnectionId, connectionId); 
    }
}
