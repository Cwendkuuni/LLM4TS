import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtensionTest;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;
    private SessionExtensionPoint sessionExtensionPoint;

    @BeforeEach
    public void setup() {
        connection = Mockito.mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        sessionExtensionPoint = Mockito.spy(obOracleSessionExtension);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        Mockito.doNothing().when(connection).createStatement();
        sessionExtensionPoint.killQuery(connection, connectionId);
        Assertions.assertDoesNotThrow(() -> sessionExtensionPoint.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithNullConnection() {
        String connectionId = "testConnectionId";
        sessionExtensionPoint.killQuery(null, connectionId);
        Assertions.assertThrows(NullPointerException.class, () -> sessionExtensionPoint.killQuery(null, connectionId));
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        String connectionId = "";
        Mockito.doNothing().when(connection).createStatement();
        sessionExtensionPoint.killQuery(connection, connectionId);
        Assertions.assertDoesNotThrow(() -> sessionExtensionPoint.killQuery(connection, connectionId));
    }
}