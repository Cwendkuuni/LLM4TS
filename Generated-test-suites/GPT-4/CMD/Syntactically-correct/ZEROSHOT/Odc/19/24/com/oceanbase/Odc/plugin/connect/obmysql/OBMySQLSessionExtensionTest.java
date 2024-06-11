
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock; 
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension; 

public class OBMySQLSessionExtensionTest {

    @Mock 
    Connection connection; 

    private static OBMySQLSessionExtension obMySQLSessionExtension;

    @BeforeClass
    public static void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockitoAnnotations.initMocks(this); 
    }

    @Test
    public void testKillQuery() {
        doNothing().when(connection).execute("KILL QUERY " + "1");
        obMySQLSessionExtension.killQuery(connection, "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        doNothing().when(connection).setCatalog("schema2");
        obMySQLSessionExtension.switchSchema(connection, "schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("schema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, "schema");
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("1");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, "1");
    }
}
