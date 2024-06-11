
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    Connection connection = mock(Connection.class);
    String connectionId = "123456";
    String schemaName = "testSchema";

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnection() {
        obMySQLSessionExtension.killQuery(null, connectionId);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnectionId() {
        obMySQLSessionExtension.killQuery(connection, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullConnection() throws SQLException {
        obMySQLSessionExtension.switchSchema(null, schemaName);
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, null);
    }
   
    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaNullConnection() {
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdNullConnection() {
        obMySQLSessionExtension.getConnectionId(null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSqlException() throws SQLException {
        doThrow(new SQLException()).when(connection).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }
}
