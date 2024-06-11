import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionManager;

import java.sql.Connection;
import java.sql.SQLException;

@Extension
@Slf4j
public class OBOracleSessionExtensionTest {

    private static OBOracleSessionExtension obOracleSessionExtension;
    private static ExtensionManager extensionManager;
    private static PluginWrapper<OBOracleSessionExtension> pluginWrapper;
    private static Connection mockConnection;

    @BeforeAll
    public static void setup() throws Exception {
        extensionManager = new DefaultExtensionManager();
        pluginWrapper = new PluginWrapper<>();
        pluginWrapper.setExtensionClass(OBOracleSessionExtension.class);
        extensionManager.loadPlugin(pluginWrapper);
        obOracleSessionExtension = extensionManager.getExtension(OBOracleSessionExtension.class);
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "test_connection_id";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        Mockito.verify(mockConnection).createStatement();
        Mockito.verify(mockConnection).execute("KILL QUERY " + connectionId);
        Mockito.verifyNoMoreInteractions(mockConnection);
    }
}