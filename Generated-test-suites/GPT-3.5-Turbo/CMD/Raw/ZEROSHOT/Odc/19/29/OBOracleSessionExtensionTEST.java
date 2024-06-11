import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
        Statement statement = mock(Statement.class);
        String connectionId = "123";

        when(connection.createStatement()).thenReturn(statement);

        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(statement).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "default_schema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection).createStatement().executeUpdate("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(expectedSchema);

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "123456";

        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn(expectedConnectionId);

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, result);
    }
}