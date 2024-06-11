

package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {
   private OBOracleSessionExtension extension;
   private Connection mockConnection;

   @Before
   public void setUp() {
       extension = new OBOracleSessionExtension();
       mockConnection = mock(Connection.class);
   }

   @Test
   public void testKillQuery() {
       String connectionId = "conn1";
       // It's a void method. Just test if it does not throw an exception.
       extension.killQuery(mockConnection, connectionId);
   }

   @Test
   public void testSwitchSchema() throws SQLException {
       String newSchema = "new_schema";
       String currentSchema = "current_schema";

       when(mockConnection.getSchema()).thenReturn(currentSchema, newSchema);

       // Switching to a new schema
       extension.switchSchema(mockConnection, newSchema);
       assertEquals(newSchema, mockConnection.getSchema());

       // Switching to the same schema
       extension.switchSchema(mockConnection, newSchema);
       assertEquals(newSchema, mockConnection.getSchema());
   }

   @Test
   public void testGetCurrentSchema() {
       String expectedQuery = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
       when(mockConnection.getSchema()).thenReturn(expectedQuery);

       String actualQuery = extension.getCurrentSchema(mockConnection);
       assertEquals(expectedQuery, actualQuery);
   }

   @Test
   public void testGetConnectionId() {
       String mockId = "conn1";
       when(mockConnection.toString()).thenReturn(mockId); 

       String actualId = extension.getConnectionId(mockConnection); 
       assertEquals(mockId, actualId);
   }
}
