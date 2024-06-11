package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;

@Extension
@Slf4j
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY 1")).thenReturn(true);

        new OBMySQLSessionExtension().killQuery(connection, "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(getCurrentSchema(connection)).thenReturn("schema1");
        
        new OBMySQLSessionExtension().switchSchema(connection, "schema2");
        
        when(getCurrentSchema(connection)).thenReturn("schema2");
        new OBMySQLSessionExtension().switchSchema(connection, "schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("schema");

        assertEquals("schema", new OBMySQLSessionExtension().getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("1");

        assertEquals("1", new OBMySQLSessionExtension().getConnectionId(connection));

        Connection nonOceanBaseConnection = mock(Connection.class);
        when(((OceanBaseConnection) nonOceanBaseConnection).getServerThreadId()).thenReturn(2L);

        assertEquals("2", new OBMySQLSessionExtension().getConnectionId(nonOceanBaseConnection));
    }
}