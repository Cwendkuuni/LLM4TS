
package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        ext.killQuery(connection, "1");
        // Since this method does not return anything, there's no assertion we can make here
        Mockito.verify(connection).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        String currentSchema = "schema";
        when(ext.getCurrentSchema(connection)).thenReturn(currentSchema);
        ext.switchSchema(connection, currentSchema);
        // Since this method does not return anything, there's no assertion we can make here
        Mockito.verify(connection).setCatalog(currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        String expectedSchema = "schema";
        when(ext.getCurrentSchema(connection)).thenReturn(expectedSchema);
        String actualSchema = ext.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        String expectedId = "1";
        when(ext.getConnectionId(connection)).thenReturn(expectedId);
        String actualId = ext.getConnectionId(connection);
        assertEquals(expectedId, actualId);
    }
}
