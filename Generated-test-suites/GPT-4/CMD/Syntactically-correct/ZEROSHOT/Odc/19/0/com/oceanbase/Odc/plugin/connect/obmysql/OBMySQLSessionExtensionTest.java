package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension extension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        extension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        extension.killQuery(connection, connectionId);
        // We cannot assert anything as the method does not return anything
        // nor does it change any state that we could verify.
        // So, the best we can do is verifying if the execute method was called.
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";

        when(extension.getCurrentSchema(connection)).thenReturn("other");

        extension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        extension.getCurrentSchema(connection);

        verify(connection).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        extension.getConnectionId(connection);

        verify(connection).queryForObject(querySql, String.class);

        when(extension.getConnectionId(connection)).thenThrow(new RuntimeException());

        assertTrue(extension.getConnectionId(connection) instanceof String);
    }
}