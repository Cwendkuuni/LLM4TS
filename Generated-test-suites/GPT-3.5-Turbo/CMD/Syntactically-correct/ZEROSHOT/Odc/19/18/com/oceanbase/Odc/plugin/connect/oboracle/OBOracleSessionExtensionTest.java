import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPointFactory;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    private Connection mockConnection;
    
    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("other_schema");
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA='" + schemaName + "'");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("1234");
        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals("1234", connectionId);
    }

}