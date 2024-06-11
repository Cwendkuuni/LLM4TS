package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "12345");

        verify(connection).prepareStatement("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        
        when(extension.getCurrentSchema(connection)).thenReturn("schema1");

        extension.switchSchema(connection, "schema2");

        verify(connection).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema1");

        String currentSchema = extension.getCurrentSchema(connection);

        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        String connectionId = extension.getConnectionId(connection);

        assertEquals("12345", connectionId);
    }
}