import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        verify(mockConnection).createStatement();
        verify(mockStatement).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String schemaName = "test_schema";

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        verify(mockConnection).createStatement();
        verify(mockStatement).executeUpdate("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        obOracleSessionExtension.getCurrentSchema(mockConnection);

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL");
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection mockConnection = mock(Connection.class);

        obOracleSessionExtension.getConnectionId(mockConnection);

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery("select userenv('sessionid') from dual");
    }
}