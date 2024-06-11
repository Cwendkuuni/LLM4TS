package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "1234567890";
        OBOracleSessionExtension oBOracleSessionExtension = new OBOracleSessionExtension();
        oBOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        OBOracleSessionExtension oBOracleSessionExtension = new OBOracleSessionExtension();
        oBOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension oBOracleSessionExtension = new OBOracleSessionExtension();
        assertEquals("1234567890", oBOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetClonedSession() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension oBOracleSessionExtension = new OBOracleSessionExtension();
        assertEquals("test_schema", oBOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWhenExceptionIsThrown() {
        Connection connection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(Exception.class);
        OBOracleSessionExtension oBOracleSessionExtension = new OBOracleSessionExtension();
        String returnedConnectionId = oBOracleSessionExtension.getConnectionId(connection);
        verify(OceanBaseConnection.class, times(1)).getServerThreadId();
        assertEquals(String.valueOf(OceanBaseConnection.getServerThreadId()), returnedConnectionId);
    }

    // You may add additional tests and scenarios here...

}
