package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito; 
import java.sql.Connection;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
       String connectionId = "123";
       obOracleSessionExtension.killQuery(connection, connectionId);
       // assertions will go here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
       String schemaName = "schema";
       obOracleSessionExtension.switchSchema(connection, schemaName);
       // assertions will go here - Since the method is VOID, assertions will be based on the behavior of the objects after the method call 
    }

    @Test
    public void testGetCurrentSchema() {
       obOracleSessionExtension.getCurrentSchema(connection);
       // assertions will go here
    }


    @Test
    public void testGetConnectionId() {
       obOracleSessionExtension.getConnectionId(connection);
       // assertions will go here
    }
}
