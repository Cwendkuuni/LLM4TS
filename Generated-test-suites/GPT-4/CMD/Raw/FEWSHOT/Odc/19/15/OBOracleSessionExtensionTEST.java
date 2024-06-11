
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension extension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";

        extension.killQuery(connection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        String currentSchema = extension.getCurrentSchema(connection);
        if (!currentSchema.equals(schemaName)) {
            extension.switchSchema(connection, schemaName);
            verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";

        String result = extension.getCurrentSchema(connection);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(querySql, String.class);
        assertNotNull(result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String querySql = "select userenv('sessionid') from dual";
        String expected = "1234";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(expected);

        String result = extension.getConnectionId(connection);

        assertEquals(expected, result);
    }
}
