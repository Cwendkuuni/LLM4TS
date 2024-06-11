package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {
  
  private OBMySQLSessionExtension obMySQLSessionExtension;
  private Connection mockConnection = Mockito.mock(Connection.class);

  @Test
  public void testKillQuery() {
    obMySQLSessionExtension = new OBMySQLSessionExtension();
    String connectionId = "123";
    
    obMySQLSessionExtension.killQuery(mockConnection, connectionId);
    
    // Verify that execute method is called with correct query
    Mockito.verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
  }
  
  @Test
  public void testSwitchSchema() throws SQLException {
    obMySQLSessionExtension = new OBMySQLSessionExtension();
    String schemaName = "test_schema";
    
    Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("current_schema");
    obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
    
    // Verify setCatalog is called with correct schemaName
    Mockito.verify(mockConnection).setCatalog(schemaName);
  }
  
  @Test
  public void testGetCurrentSchema() {
    obMySQLSessionExtension = new OBMySQLSessionExtension();
    
    String expectedSchema = "test_schema";
    Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
    
    String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
    
    assertEquals(expectedSchema, currentSchema);
  }
  
  @Test
  public void testGetConnectionId() {
    obMySQLSessionExtension = new OBMySQLSessionExtension();
    
    String expectedConnectionId = "12345";
    
    Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
    
    String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
    
    assertEquals(expectedConnectionId, connectionId);
  }
  
}