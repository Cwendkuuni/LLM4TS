
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension extension = new OBMySQLSessionExtension();;
    private Connection conn = mock(Connection.class);
    private OceanBaseConnection oceanBaseConn = mock(OceanBaseConnection.class);

    @Test
    public void testKillQuery() {
        extension.killQuery(conn, "1");
        verify(conn, times(1)).execute("KILL QUERY " + "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        extension.switchSchema(conn, "schema1");
        verify(conn, times(1)).setCatalog("schema1");
    }

    @Test
    public void testSwitchSchema_noSwitchIfSameSchema() throws SQLException {
        when(extension.getCurrentSchema(conn)).thenReturn("schema1");
        extension.switchSchema(conn, "schema1");
        verify(conn, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        extension.getCurrentSchema(conn);
        verify(conn, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        extension.getConnectionId(conn);
        verify(conn, times(1)).queryForObject("select connection_id()", String.class);
    }

    @Test
    public void testGetConnectionId_oceanBaseConnection() {
        when(conn instanceof OceanBaseConnection).thenReturn(true);
        extension.getConnectionId(conn);
        verify(oceanBaseConn, times(1)).getServerThreadId();
    }

}

