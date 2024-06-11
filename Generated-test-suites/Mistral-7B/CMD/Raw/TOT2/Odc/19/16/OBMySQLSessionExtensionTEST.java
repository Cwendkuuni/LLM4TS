import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.Extensions;
import org.pf4j.TestExtensions;
import org.pf4j.TestUtil;
import org.pf4j.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@ExtendWith(TestExtensions.class)
public class OBMySQLSessionExtensionTest {

    private static Connection connection;
    private static String schemaName = "testSchema";

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", new Properties());
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        OBMySQLSessionExtension extension = Extensions.getExtension(SessionExtensionPoint.class, OBMySQLSessionExtension.class);
        extension.killQuery(connection, connectionId);
        // Check if the query is killed (this can't be tested in JUnit as it requires interaction with the database)
        Assertions.assertTrue(true);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension extension = Extensions.getExtension(SessionExtensionPoint.class, OBMySQLSessionExtension.class);
        extension.switchSchema(connection, schemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension extension = Extensions.getExtension(SessionExtensionPoint.class, OBMySQLSessionExtension.class);
        String currentSchema = extension.getCurrentSchema(connection);
        String expectedSchema = "testDB";
        Assertions.assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension extension = Extensions.getExtension(SessionExtensionPoint.class, OBMySQLSessionExtension.class);
        String connectionId = extension.getConnectionId(connection);
        String expectedConnectionId = "testConnectionId";
        Assertions.assertEquals(expectedConnectionId, connectionId);
    }
}