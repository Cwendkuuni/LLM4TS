
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
  private OBMySQLSessionExtension obMySqlSessionExtension;
  private Connection connection;

  @Before
  public void setup() {
    obMySqlSessionExtension = new OBMySQLSessionExtension();
    connection = Mockito.mock(Connection.class);
  }

  @Test
  public void testKillQuery() {
    String connectionId = "123";

    obMySqlSessionExtension.killQuery(connection, connectionId);

    verify(connection, times(1)).execute("KILL QUERY " + connectionId);
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    String schemaName = "test_schema";
    String currentSchema = "current_schema";
    
    Mockito.when(obMySqlSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
  
    obMySqlSessionExtension.switchSchema(connection, schemaName);
    
    verify(connection, times(1)).setCatalog(schemaName);
    assertNotEquals(currentSchema, schemaName);
  }

  @Test
  public void testGetCurrentSchema() {
    String querySql = "SELECT DATABASE()";

    String result = obMySqlSessionExtension.getCurrentSchema(connection);

    verify(connection, times(1)).queryForObject(querySql, String.class);
    assertEquals(result, querySql);
  }

  @Test
  public void testGetConnectionId() {
    String querySql = "select connection_id()";
    String expectedConnectionId = "expected_connection_id";

    String result = obMySqlSessionExtension.getConnectionId(connection);

    verify(connection, times(1)).queryForObject(querySql, String.class);
    assertEquals(result, expectedConnectionId);
    assertNotEquals(expectedConnectionId, null);
  }
}
