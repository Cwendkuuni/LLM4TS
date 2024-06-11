package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "testId";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        extension.killQuery(connection, connectionId);

        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(extension.getCurrentSchema(connection)).thenReturn("notTestSchema");
        extension.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        extension.getCurrentSchema(connection);

        verify(connection, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        extension.getConnectionId(connection);

        verify(connection, times(1)).queryForObject("select connection_id()", String.class);
    }

    @Test
    public void testGetConnectionIdException() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        String connectionId = extension.getConnectionId(connection);

        verify(((OceanBaseConnection) connection), times(1)).getServerThreadId();
    }
}
