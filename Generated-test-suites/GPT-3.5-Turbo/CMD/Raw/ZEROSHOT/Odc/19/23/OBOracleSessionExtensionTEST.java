import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, connectionId);

        verify(connection, times(1)).prepareStatement("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='TEST_SCHEMA'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.getCurrentSchema(connection);

        verify(connection, times(1)).prepareStatement("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL");
        verify(connection, times(1)).executeQuery();
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.getConnectionId(connection);

        verify(connection, times(1)).prepareStatement("select userenv('sessionid') from dual");
        verify(connection, times(1)).executeQuery();
    }
}