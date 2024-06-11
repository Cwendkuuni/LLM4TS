import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify that the execute method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the update method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("testSchema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = mock(OceanBaseConnection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // Verify that the connectionId is retrieved from OceanBaseConnection's getServerThreadId method
        assertEquals("0", connectionId); // Assuming getServerThreadId returns 0 when an exception is thrown
    }
}