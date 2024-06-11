
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection obConnection;

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "1");
        verify(connection, times(1)).execute("KILL QUERY " + "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("schema1");
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "schema1");
        verify(connection, times(0)).setCatalog(any());

        extension.switchSchema(connection, "schema2");
        verify(connection, times(1)).setCatalog("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "schema1";
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
  
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schema = extension.getCurrentSchema(connection);
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "connection1";
        when(connection.queryForObject("select connection_id()", String.class)).thenReturn(expectedId);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String id = extension.getConnectionId(connection);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String expectedId = "connection1";
        when(obConnection.getServerThreadId()).thenReturn(expectedId);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String id = extension.getConnectionId(obConnection);
        assertEquals(expectedId, id);
    }
}
