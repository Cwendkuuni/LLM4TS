import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

  private Connection connection;
  private OBOracleSessionExtension obOracleSessionExtension;

  @Before
  public void setup() {
    connection = mock(Connection.class);
    obOracleSessionExtension = new OBOracleSessionExtension();
  }

  @Test
  public void testKillQuery() {
    String connectionId = "123456";
    obOracleSessionExtension.killQuery(connection, connectionId);
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    String schemaName = "test_schema";
    obOracleSessionExtension.switchSchema(connection, schemaName);
  }

  @Test
  public void testGetCurrentSchema() {
    assertEquals("", obOracleSessionExtension.getCurrentSchema(connection));
  }

  @Test
  public void testGetConnectionId() {
    assertEquals("", obOracleSessionExtension.getConnectionId(connection));
  }
  
}