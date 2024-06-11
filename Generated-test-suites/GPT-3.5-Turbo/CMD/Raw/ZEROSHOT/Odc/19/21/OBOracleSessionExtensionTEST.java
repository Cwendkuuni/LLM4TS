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

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }
}