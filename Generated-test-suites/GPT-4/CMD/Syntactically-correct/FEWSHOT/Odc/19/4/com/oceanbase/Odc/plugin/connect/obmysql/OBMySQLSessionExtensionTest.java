
package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        ext.killQuery(connection, "1001");

        verify(connection, times(1)).execute("KILL QUERY 1001");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
       
        ext.switchSchema(connection, "testSchema");

        verify(connection, times(1)).setCatalog("testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        
        ext.getCurrentSchema(connection);
       
        verify(connection, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        
        ext.getConnectionId(connection);
        
        verify(connection, times(1)).queryForObject("select connection_id()", String.class);
    }
}
