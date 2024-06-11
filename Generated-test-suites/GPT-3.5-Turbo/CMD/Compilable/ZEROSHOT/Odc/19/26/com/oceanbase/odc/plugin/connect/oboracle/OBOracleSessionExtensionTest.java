package com.oceanbase.odc.plugin.connect.oboracle;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {
  
   @Test
    public void testKillQuery() {
       Connection connection = mock(Connection.class);
       OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
       obOracleSessionExtension.killQuery(connection, "123");
       // add assertions here
   }

   @Test
   public void testSwitchSchema() throws SQLException {
       Connection connection = mock(Connection.class);
       OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
       obOracleSessionExtension.switchSchema(connection, "test_schema");
       // add assertions here
   }

   @Test
   public void testGetCurrentSchema() {
       Connection connection = mock(Connection.class);
       OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
       obOracleSessionExtension.getCurrentSchema(connection);
       // add assertions here
   }

   @Test
   public void testGetConnectionId() {
       Connection connection = mock(Connection.class);
       OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
       obOracleSessionExtension.getConnectionId(connection);
       // add assertions here
   }
}