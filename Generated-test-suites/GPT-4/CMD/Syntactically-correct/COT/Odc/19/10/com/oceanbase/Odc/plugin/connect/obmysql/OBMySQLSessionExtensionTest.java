
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
  
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockedConnection;

    @Before
    public void setUp() throws Exception {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockedConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(mockedConnection, connectionId);
        // verify if the method executes without exceptions
    }

    @Test
    public void testSwitchSchema() throws Exception{
        String schemaName = "test-schema";
        obMySQLSessionExtension.switchSchema(mockedConnection, schemaName);
        //verify if the method executes without exceptions
        // further assertions can be added based on the expected behavior
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test-schema";
        String querySql = "SELECT DATABASE()";
        when(JdbcOperationsUtil.getJdbcOperations(mockedConnection).queryForObject(querySql, String.class)).
            thenReturn(expectedSchema);
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockedConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "123";
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(mockedConnection).queryForObject(querySql, String.class)).
            thenReturn(expectedId);
        String actualId = obMySQLSessionExtension.getConnectionId(mockedConnection);
        assertEquals(expectedId, actualId);
    }

    @Test (expected = SQLException.class)
    public void testProcException() throws SQLException {
        doThrow(new SQLException()).when(mockedConnection).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(mockedConnection, "testCat");
    }
}
