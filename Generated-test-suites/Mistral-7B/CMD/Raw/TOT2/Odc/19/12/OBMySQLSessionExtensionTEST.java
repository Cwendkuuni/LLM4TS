import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.TestExtension;
import org.pf4j.impl.DefaultExtensionFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

@ExtendWith(TestExtension. class)
public class OBMySQLSessionExtensionTest {
    private static Connection connection;
    private static OBMySQLSessionExtension obMySQLSessionExtension;

    @BeforeAll
    public static void setup() throws Exception {
        ExtensionFactory extensionFactory = new DefaultExtensionFactory();
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionFactory.getExtension(SessionExtensionPoint.class);
        connection = obMySQLSessionExtension.getConnection();
    }

    @Test
    public void testKillQuery() throws Exception {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        Assertions.assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }
}