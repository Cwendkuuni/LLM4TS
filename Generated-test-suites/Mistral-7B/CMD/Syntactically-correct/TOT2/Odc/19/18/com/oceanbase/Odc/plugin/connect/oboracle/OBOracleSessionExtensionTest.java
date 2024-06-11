import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.TestExtension;
import org.pf4j.extensions.DatabaseConnectionExtension;

@ExtendWith(TestExtension.class)
@ExtendWith(DatabaseConnectionExtension.class)
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection connection = null; // Replace this with a mock or real connection

        try {
            obOracleSessionExtension.killQuery(connection, "testConnectionId");
            Assertions.assertDoesNotThrow(() -> JdbcOperationsUtil.getJdbcOperations(connection).execute("SELECT 1 FROM DUAL"));
            Assertions.fail("Expected SQLException not thrown");
        } catch (Exception e) {
            Assertions.assertInstanceOf(SQLException.class, e);
            // You can add additional asserts here to check the exception message, etc.
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Handle any exceptions when closing the connection
                }
            }
        }
    }
}