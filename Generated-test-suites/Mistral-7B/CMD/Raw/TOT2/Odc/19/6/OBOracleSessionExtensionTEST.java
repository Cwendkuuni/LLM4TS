import static org.junit.Assert.assertNoThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = "12345";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Add assertions here if necessary
    }
}