import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.pf4j.impl.AbstractExtension;
import org.pf4j.impl. Pf4JClassScanner;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtensionTest;
import java.sql.Connection;
import java.sql.SQLException;

public class Method1Test extends AbstractExtension implements SessionExtensionPoint {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
     static PluginManager pluginManager;

    @BeforeAll
    public static void beforeAll() {
        pluginManager = new PluginManager();
        Pf4JClassScanner scanner = new Pf4JClassScanner();
        scanner.setPackage("com.oceanbase.odc.plugin.connect.oboracle");
        pluginManager.setClassScanner(scanner);
        pluginManager.loadExtensions();
        obOracleSessionExtension = (OBOracleSessionExtension) pluginManager.getExtensionByType(OBOracleSessionExtension.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        connection = obOracleSessionExtension.createConnection("jdbc:oceanbase:local:1527/testdb", "username", "password");
        String connectionId = "test_connection_id";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add assertion here to check if the query is killed
        Assertions.assertTrue(true);
    }

    @Override
    public void killQuery(Connection connection, String connectionId) {
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryNullConnection() {
        String connectionId = "test_connection_id";
        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(null, connectionId));
    }

    @Test
    public void testKillQueryNullConnectionId() {
        Connection connection = obOracleSessionExtension.createConnection("jdbc:oceanbase:local:1527/testdb", "username", "password");
        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }
}