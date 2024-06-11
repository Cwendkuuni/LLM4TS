
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;

import org.junit.Test;

import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = Mockito.mock(Connection.class);
        String connectionId = "123";

        new OBOracleSessionExtension().killQuery(mockConnection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        String schemaName = "TestSchema";

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);

        new OBOracleSessionExtension().switchSchema(mockConnection, schemaName);

        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = Mockito.mock(Connection.class);
        String expectedSchema = "TestSchema";

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(expectedSchema);

        String actualSchema = new OBOracleSessionExtension().getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = Mockito.mock(Connection.class);
        String expectedConnectionId = "456";

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(expectedConnectionId);

        String actualConnectionId = new OBOracleSessionExtension().getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
