import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {
  
  private OBOracleSessionExtension obOracleSessionExtension;
  private Connection mockConnection;
  
  @Before
  public void setUp() {
    obOracleSessionExtension = new OBOracleSessionExtension();
    mockConnection = mock(Connection.class);
  }
  
  @Test
  public void testKillQuery() throws SQLException {
    String connectionId = "12345";
    obOracleSessionExtension.killQuery(mockConnection, connectionId);
    
    // Verify that the JdbcOperationsUtil execute method is called with the correct query
    verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
  }
  
  @Test
  public void testSwitchSchema() throws SQLException {
    String schemaName = "test_schema";
    
    // Test when the current schema is different from the target schema
    when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("other_schema");
    obOracleSessionExtension.switchSchema(mockConnection, schemaName);
    verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA='" + schemaName + "'");
    
    // Test when the current schema is the same as the target schema
    when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
    obOracleSessionExtension.switchSchema(mockConnection, schemaName);
    verifyNoInteractions(JdbcOperationsUtil.getJdbcOperations(mockConnection));
  }
  
  @Test
  public void testGetCurrentSchema() {
    String expectedSchema = "test_schema";
    when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
    
    String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
    
    assertEquals(expectedSchema, currentSchema);
  }
  
  @Test
  public void testGetConnectionId() throws SQLException {
    String expectedConnectionId = "12345";
    when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class)))
      .thenThrow(new SQLException("Test exception"))
      .thenReturn(expectedConnectionId);
    
    String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
    
    assertEquals(expectedConnectionId, connectionId);
  }
}