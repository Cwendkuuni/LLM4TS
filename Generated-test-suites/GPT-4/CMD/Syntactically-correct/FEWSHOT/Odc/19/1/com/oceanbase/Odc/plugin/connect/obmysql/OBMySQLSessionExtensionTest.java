
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension obMySQLSessionExtension;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(connection.getCatalog(), schemaName);
    }

    @Test public void testSwitchSchemaDifferent() throws SQLException {
        String schemaName = "test";
        String differentSchema = "different";
        when(connection.getCatalog()).thenReturn(differentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertNotEquals(connection.getCatalog(), schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection).createStatement().executeQuery(querySql);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection).createStatement().executeQuery(querySql);
    }
}
