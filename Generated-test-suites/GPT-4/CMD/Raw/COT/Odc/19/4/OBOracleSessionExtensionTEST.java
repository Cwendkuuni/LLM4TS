import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;
    
    @Before
    public void setup() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnection";
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
        sessionExtension.killQuery(mockConnection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "testSchema";
        when(sessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);
        sessionExtension.switchSchema(mockConnection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        String schemaName = "errorSchema";
        sessionExtension.switchSchema(mockConnection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class)).thenReturn("testSchema");
        assertEquals("testSchema", sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class)).thenReturn("testConnection");
        assertEquals("testConnection", sessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdException() {
        String querySql = "select userenv('sessionid') from dual";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        assertNull(sessionExtension.getConnectionId(mockConnection));
    }
}