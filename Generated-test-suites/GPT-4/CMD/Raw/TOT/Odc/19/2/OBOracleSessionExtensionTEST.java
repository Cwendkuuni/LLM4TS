import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @InjectMocks
    OBOracleSessionExtension oBOracleSessionExtension;

    @Mock
    Connection connection;

    @Test
    public void testKillQuery() {
        String connectionId = "test";
        oBOracleSessionExtension.killQuery(connection, connectionId);
        // validate your results
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        oBOracleSessionExtension.switchSchema(connection, schemaName);
        // validate your results
    }

    @Test
    public void testGetCurrentSchema() {
        String result = oBOracleSessionExtension.getCurrentSchema(connection);
        // validate your results
    }

    @Test
    public void testGetConnectionId() {
        String result = oBOracleSessionExtension.getConnectionId(connection);
        // validate your results
    }
}