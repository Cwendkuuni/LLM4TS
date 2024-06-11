import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtensionTest;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("static-access")
public class OBOracleSessionExtensionTest {

    private static Connection connection;
    private static OBOracleSessionExtension obOracleSessionExtension;
    private static SessionExtensionPoint sessionExtensionPoint;

    @BeforeAll
    public static void setup() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        PluginWrapper pluginWrapper = new PluginWrapper("com.oceanbase.odc.plugin.connect.oboracle:OBOracleSessionExtension");
        extensionManager.loadPlugin(pluginWrapper);
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(OBOracleSessionExtension.class);
        sessionExtensionPoint = obOracleSessionExtension;
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        sessionExtensionPoint.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).execute(Mockito.eq("KILL QUERY " + connectionId));
        Assertions.assertDoesNotThrow(() -> sessionExtensionPoint.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithNullConnection() {
        String connectionId = "123";
        Assertions.assertThrows(NullPointerException.class, () -> sessionExtensionPoint.killQuery(null, connectionId));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        Assertions.assertThrows(NullPointerException.class, () -> sessionExtensionPoint.killQuery(connection, null));
    }
}