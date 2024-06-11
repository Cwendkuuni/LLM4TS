import static org.junit.Assert.assertNoException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection).createStatement();
        verify(connection).execute(Mockito.contains("KILL QUERY " + connectionId));
        assertNoExceptionThrown(obOracleSessionExtension::killQuery, connection, connectionId);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnection() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(null, "test_connection_id");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryWithEmptyConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryConnectionDoesntSupportStatement() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenThrow(new SQLException("Connection doesn't support statement."));
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, "test_connection_id");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryConnectionIsClosed() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, "test_connection_id");
    }

    private void assertNoExceptionThrown(SessionExtensionPoint obOracleSessionExtension, Connection connection, String connectionId) {
        try {
            obOracleSessionExtension.killQuery(connection, connectionId);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}