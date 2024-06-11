package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "123";

        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        // Since method killQuery doesn't return anything, we just test if exception occurs
        ext.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "schema1";

        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        ext.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
       
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        String result = ext.getCurrentSchema(connection);
        assertNotNull(result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
       
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        String result = ext.getConnectionId(connection);
        assertNotNull(result);
    }

    // Test for exception scenarios
    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        doThrow(new SQLException()).when(connection).setCatalog(anyString());

        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        ext.switchSchema(connection, "schemaName");
    }
}
