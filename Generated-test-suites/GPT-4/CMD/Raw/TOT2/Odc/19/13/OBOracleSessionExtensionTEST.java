
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import static org.mockito.ArgumentMatchers.anyString;

public class OBOracleSessionExtensionTest {
  @Mock
  private Connection connection;
  private OBOracleSessionExtension obOracleSessionExtension;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    obOracleSessionExtension = new OBOracleSessionExtension();
  }

  @Test
  public void testKillQuery() {
    obOracleSessionExtension.killQuery(connection, "testId");
    // verify that connection was used
    Mockito.verify(connection).execute(anyString());
  }

  @Test
  public void testSwitchSchema() {
    obOracleSessionExtension.switchSchema(connection, "testSchema");
    // verify that connection was used
    Mockito.verify(connection).update(anyString());
  }

  @Test
  public void testGetCurrentSchema() {
    obOracleSessionExtension.getCurrentSchema(connection);
    // verify that connection was used
    Mockito.verify(connection).queryForObject(anyString(), anyString());
  }

  @Test
  public void testGetConnectionId() {
    obOracleSessionExtension.getConnectionId(connection);
    // verify that connection was used
    Mockito.verify(connection).queryForObject(anyString(), anyString());
  }
}
