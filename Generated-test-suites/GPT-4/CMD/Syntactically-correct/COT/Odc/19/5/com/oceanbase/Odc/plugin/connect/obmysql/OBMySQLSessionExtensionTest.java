package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "connectionId");
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "schemaName");
        Mockito.verify(connection, Mockito.times(1)).setCatalog("schemaName");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals("ExpectedSchema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertNotNull(extension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.setCatalog(Mockito.anyString())).thenThrow(SQLException.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "schemaName");
    }

    @Test
    public void testGetConnectionIdException() {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any())).thenThrow(Exception.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertNotNull(extension.getConnectionId(connection));
    }
}