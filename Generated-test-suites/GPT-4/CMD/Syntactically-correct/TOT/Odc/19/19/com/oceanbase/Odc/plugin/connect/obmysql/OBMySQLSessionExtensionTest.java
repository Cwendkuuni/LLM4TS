package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        extension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "test";
        when(extension.getCurrentSchema(connection)).thenReturn("oldSchema");
        extension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        extension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        extension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }
}
