package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.sql.Connection;

public class OBMySQLSessionExtensionTest {
   @Mock
   private Connection mockConnection;
   
   private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

   @Test
   public void testKillQuery() {
       String connectionId = "TestConnectionId";
       obMySQLSessionExtension.killQuery(mockConnection, connectionId);
       Mockito.verify(mockConnection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
   }

   @Test(expected = SQLException.class)
   public void testSwitchSchema_WithException() throws SQLException {
       Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("TestSchema");
       obMySQLSessionExtension.switchSchema(mockConnection, "TestSchema");
   }

   @Test
   public void testSwitchSchema_WithDifferentSchema() throws SQLException {
       Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("DifferentSchema");
       obMySQLSessionExtension.switchSchema(mockConnection, "TestSchema");
       Mockito.verify(mockConnection, Mockito.times(1)).setCatalog("TestSchema");
   }

   @Test
   public void testGetCurrentSchema() {
       String expectedSchema = "TestSchema";
       Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(expectedSchema);
       String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
       Mockito.verify(mockConnection, Mockito.times(1)).queryForObject("SELECT DATABASE()", String.class);
       assertEquals(expectedSchema, actualSchema);
   }

   @Test
   public void testGetConnectionId() {
       String expectedConnectionId = "TestConnectionId";
       Mockito.when(obMySQLSessionExtension.getConnectionId(mockConnection)).thenReturn(expectedConnectionId);
       String actualConnectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
       Mockito.verify(mockConnection, Mockito.times(1)).queryForObject("select connection_id()", String.class);
       assertEquals(expectedConnectionId, actualConnectionId);
   }
}