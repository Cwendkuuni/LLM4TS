
import com.oceanbase.jdbc.OceanBaseConnection;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest extends TestCase {

    private OBOracleSessionExtension oBOracleSessionExtension;
    private Connection connection;
    private OceanBaseConnection oceanBaseConnection;


    @Before
    public void setUp() throws Exception {
        oBOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        oceanBaseConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testId";
        oBOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(oBOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        oBOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String schemaName = "testSchema";

        when(oBOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        assertEquals(schemaName, oBOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "testConnectionId";

        when(oBOracleSessionExtension.getConnectionId(connection)).thenReturn(connectionId);
        assertEquals(connectionId, oBOracleSessionExtension.getConnectionId(connection));
    }
    
    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "12345";

        when(oBOracleSessionExtension.getConnectionId(oceanBaseConnection)).thenReturn(connectionId);
        assertEquals(connectionId, oBOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}
