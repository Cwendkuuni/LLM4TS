import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.Test;

public class OBOracleSessionExtensionTest {
    @Test
    public void testKillQuery() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "example_connection_id";

        extension.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
        assertDoesNotThrow(() -> extension.killQuery(connection, connectionId));
    }
}