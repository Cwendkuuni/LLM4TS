
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension sessionExtension;
    private OceanBaseConnection obConnection;

    @Before
    public void setUp() {
        connection = Mockito.mock(Connection.class);
        sessionExtension = new OBMySQLSessionExtension();
        obConnection = Mockito.mock(OceanBaseConnection.class);
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "123";
        sessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn(schemaName);
        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaDiffSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn("diffSchema");
        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "testSchema";
        Mockito.when(sessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        assertEquals(schemaName, sessionExtension.getCurrentSchema(connection));
    }
    
    @Test
    public void testGetConnectionIdReturnResult() {
        String connectionId = "123";
        Mockito.when(sessionExtension.getConnectionId(connection)).thenReturn(connectionId);
        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }
    
    @Test
    public void testGetConnectionIdReturnException() {
        String expectedConnectionId = "123";
        Mockito.when(obConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedConnectionId));
        Mockito.when(sessionExtension.getConnectionId(connection)).thenThrow(RuntimeException.class);
        assertEquals(expectedConnectionId, sessionExtension.getConnectionId(obConnection));
    }
}
