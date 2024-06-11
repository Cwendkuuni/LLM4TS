import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
        oracleSessionExtension.killQuery(mockConnection, "1234");
        // assertion to verify KILL QUERY is exceuted
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
        oracleSessionExtension.switchSchema(mockConnection, "TEST_SCHEMA");
        // assertion to verify SWITCH SCHEMA is exceuted
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
        String result = oracleSessionExtension.getCurrentSchema(mockConnection);        
        // assertion to verify correct current schema is returned
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
        String result = oracleSessionExtension.getConnectionId(mockConnection);        
        // assertion to verify correct connection id is returned
    }

    @Test
    public void testGetConnectionIdOceanBaseException() {
        OceanBaseConnection mockConnection = mock(OceanBaseConnection.class);
        OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
        when(mockConnection.getServerThreadId()).thenReturn(1234L);
        String result = oracleSessionExtension.getConnectionId(mockConnection);        
        // assertion to verify correct connection id is returned when exception occurred
    }
}