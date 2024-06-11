import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

import java.sql.Connection;
import java.sql.SQLException;


public class OBOracleSessionExtensionTest {
  
  private OBOracleSessionExtension oracleSessionExtension; 
  private Connection connection; 
  private String schemaName;

  @Before
  public void setup() throws SQLException {
    oracleSessionExtension = new OBOracleSessionExtension();
    connection = mock(Connection.class);
    schemaName = "schemaName";
  }

  @Test
  public void testKillQuery(){
    oracleSessionExtension.killQuery(connection, "connectionId");
  }
  
  @Test(expected = NullPointerException.class)
  public void testKillQueryNullConnection(){
    oracleSessionExtension.killQuery(null, "connectionId");
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    oracleSessionExtension.switchSchema(connection, schemaName);
  }

  @Test(expected = NullPointerException.class)
  public void testSwitchSchemaNullConnection() throws SQLException {
    oracleSessionExtension.switchSchema(null, schemaName);
  }

  @Test
  public void testGetCurrentSchema(){
    oracleSessionExtension.getCurrentSchema(connection);
  }

  @Test(expected = NullPointerException.class)
  public void testGetCurrentSchemaNullConnection(){
    oracleSessionExtension.getCurrentSchema(null);
  }

  @Test
  public void testGetConnectionId(){
    oracleSessionExtension.getConnectionId(connection);
  }

  @Test(expected = NullPointerException.class)
  public void testGetConnectionIdNullConnection(){
    oracleSessionExtension.getConnectionId(null);
  }
}