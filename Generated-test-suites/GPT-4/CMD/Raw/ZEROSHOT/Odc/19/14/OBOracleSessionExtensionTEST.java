import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OceanBaseConnection OBConnection;
    private OBOracleSessionExtension OBOracleSessionExtension;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        OBOracleSessionExtension = new OBOracleSessionExtension();
        OBConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "01002020";
        OBOracleSessionExtension.killQuery(connection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtension.switchSchema(connection, schemaName);
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "SessionId";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);
        assertEquals(expectedConnectionId, OBOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        String expectedConnectionId = "OceanBaseServerThreadId";
        when(OBConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));
        assertEquals(expectedConnectionId, OBOracleSessionExtension.getConnectionId(OBConnection));
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "CurrentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        assertEquals(expectedSchema, OBOracleSessionExtension.getCurrentSchema(connection));
    }
}