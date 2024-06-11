import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.TestExtension;

import java.sql.*;

@ExtendWith(TestExtension.class)
public class OBOracleSessionExtensionTest {

    private static Connection connection;

    @BeforeAll
    public static void setup() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "username", "password");
    }

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "testConnectionId");
        //TODO: Add assertions to verify the query is killed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, "testSchema");
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "");
        //TODO: Add assertions to verify the error is thrown
    }

    @Test
    public void testKillQueryWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(null, "testConnectionId");
        //TODO: Add assertions to verify the error is thrown
    }

    @Test
    public void testKillQueryWithIncorrectConnectionId() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "incorrectConnectionId");
        //TODO: Add assertions to verify the error is thrown
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(null, "testSchema");
    }

    @Test
    public void testSwitchSchemaWithEmptyOrNullSchemaName() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, "");
        extension.switchSchema(connection, null);
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals(currentSchema, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNonexistentSchemaName() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, "nonexistentSchema");
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals(currentSchema, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.getCurrentSchema(null);
    }

    @Test
    public void testGetCurrentSchemaWithEmptyConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.getCurrentSchema(connection);
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals(currentSchema, getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithConnectionThatDoesntSupportGettingServerThreadId() {
        Connection connection = DriverManager.getConnection("jdbc:mock:testDatabase", "username", "password");
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    private String getCurrentSchema(Connection connection) {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
    }
}