
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "testConnectionId";

        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchemaName";
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String querySql = "SELECT DATABASE()";

        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String result = sessionExtension.getCurrentSchema(connection);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(querySql, String.class);
        assertNotNull(result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        String querySql = "select connection_id()";
        String serverThreadId = "serverThreadId";

        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(serverThreadId);

        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String result = sessionExtension.getConnectionId(connection);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(querySql, String.class);
        assertEquals(serverThreadId, result);
    }
}
