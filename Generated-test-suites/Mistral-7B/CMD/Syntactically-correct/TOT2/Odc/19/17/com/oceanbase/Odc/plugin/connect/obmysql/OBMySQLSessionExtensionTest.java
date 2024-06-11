import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;
import org.pf4j.TestTools;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.jdbc.OceanBaseConnection;

@TestTools
@TestExtension
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OceanBaseConnection connection = TestTools.getTestDatabaseConnection();
        String connectionId = "1";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, connectionId);
    }
}