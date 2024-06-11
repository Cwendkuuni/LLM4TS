import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

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
        String connectionId = "1234";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "schema1";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "1234";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(mockConnection));
    }
}