package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // verify if the method is called
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // verify if the method is called
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "expectedSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "expectedId";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        Assert.assertEquals(expectedConnectionId, actualConnectionId);
    }
}