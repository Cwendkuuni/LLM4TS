package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection connection;

    @Before
    public void setUp() {
        extension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1");
        Mockito.verify(connection).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Mockito.when(extension.getCurrentSchema(connection)).thenReturn("old_schema");
        extension.switchSchema(connection, "new_schema");
        Mockito.verify(connection).setCatalog("new_schema");        
    }

    @Test
    public void testGetCurrentSchema() {
       Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("current_schema");
       assertEquals("current_schema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        Mockito.when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        String connectionId = "1";
        assertEquals(connectionId, extension.getConnectionId(connection));
    }
    
    @Test
    public void testGetConnectionIdWithoutException() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("1");
        String connectionId = "1";
        assertEquals(connectionId, extension.getConnectionId(connection));
    }
}