import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @BeforeEach
    public void setup() {
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        obOracleSessionExtension.killQuery(connection, null);
        Mockito.verify(connection, Mockito.never()).createStatement();
        Mockito.verify(connection, Mockito.never()).execute("KILL QUERY null");
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        obOracleSessionExtension.killQuery(connection, "");
        Mockito.verify(connection, Mockito.never()).createStatement();
        Mockito.verify(connection, Mockito.never()).execute("KILL QUERY ''");
    }
}