import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;

/**
 * Test Class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        //Ensure execute method was hit.
        Mockito.verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test(expected=SQLException.class)
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection))
               .thenReturn("anotherSchema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
        //Ensure the update operation was attempted.
        Mockito.verify(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(connection);
        //Verify	query execution
        Mockito.verify(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void getConnectionId() {
        obOracleSessionExtension.getConnectionId(connection);
        //Verify query execution
        Mockito.verify(connection).queryForObject("select userenv('sessionid') from dual", String.class);
    }

    @Test(expected=Exception.class)
    public void getConnectionIdException() throws Exception {
        Mockito.when(connection.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new Exception());
        obOracleSessionExtension.getConnectionId(connection);
    }
}