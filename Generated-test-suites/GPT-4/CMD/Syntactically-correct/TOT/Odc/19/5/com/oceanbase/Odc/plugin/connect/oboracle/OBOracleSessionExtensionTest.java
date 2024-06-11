package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {
  
  private OBOracleSessionExtension extension;
  private Connection conn;
  
  @Before
  public void setup() {
    extension = new OBOracleSessionExtension();
    conn = mock(Connection.class);
  }


  @Test
  public void testKillQuery() {
    String testConnectionId = "test-connection-id";
    
    // Execute method under test
    extension.killQuery(conn, testConnectionId);

    // Verify the behavior (assuming there is a way to check if the query is killed)
    verify(conn).execute("KILL QUERY " + testConnectionId);
  }

  
  @Test
  public void testSwitchSchema() throws SQLException {
    String testSchemaName = "test-schema-name";

    // Execute method under test
    extension.switchSchema(conn, testSchemaName);

    // Verify the behavior 
    verify(conn).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(testSchemaName));
  }

  @Test
  public void testGetCurrentSchema() {
    String expectedSchema = "test-schema-name";

    // Mock behavior
    when(JdbcOperationsUtil.getJdbcOperations(conn).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);

    // Execute method under test
    String actualSchema = extension.getCurrentSchema(conn);

    // Assert
    assertEquals(expectedSchema, actualSchema);
  }

  @Test
  public void testGetConnectionId() {
    String expectedConnectionId = "test-connection-id";

    // Mock behavior
    when(JdbcOperationsUtil.getJdbcOperations(conn).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);

    // Execute method under test
    String actualConnectionId = extension.getConnectionId(conn);

    // Assert
    assertEquals(expectedConnectionId, actualConnectionId);
  }
}
