package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionPointAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        connection = Mockito.mock(Connection.class);
        extension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        extension.killQuery(connection, connectionId);
        Mockito.verify(connection.createStatement()).execute(Mockito.eq("KILL QUERY " + connectionId));
    }

    @Test
    public void testSwitchSchemaWhenSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(connection.getCatalog()).thenReturn(schemaName);
        extension.switchSchema(connection, schemaName);
        // No action should be taken since the schema is already active
    }

    @Test
    public void testSwitchSchemaWhenDifferentSchema() throws SQLException {
        String currentSchema = "oldSchema";
        String schemaName = "testSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);
        when(connection.setCatalog(anyString())).thenReturn(true);

        extension.switchSchema(connection, schemaName);
        Mockito.verify(connection, Mockito.times(1)).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWhenFailed() throws SQLException {
        String schemaName = "testSchema";
        when(connection.getCatalog()).thenReturn("oldSchema");
        when(connection.setCatalog(anyString())).thenThrow(new SQLException());

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "testSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("DATABASE()")).thenReturn(expectedSchema);

        String resultSchema = extension.getCurrentSchema(connection);
        assertEquals(expectedSchema, resultSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("connection_id()")).thenReturn("12345");

        String resultConnectionId = extension.getConnectionId(connection);
        assertEquals("12345", resultConnectionId);
    }

    @Test
    public void testGetConnectionIdWhenNoID() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(false);

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn("67890");

        String resultConnectionId = extension.getConnectionId(connection);
        assertEquals("67890", resultConnectionId);
    }
}