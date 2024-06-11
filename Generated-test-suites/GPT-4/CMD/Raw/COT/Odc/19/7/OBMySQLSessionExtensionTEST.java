
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        testObject.killQuery(connection, "testId");
        Mockito.verify(connection).execute("KILL QUERY " + "testId"); 
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        testObject.switchSchema(connection, "testSchema");
        Mockito.verify(connection).setCatalog("testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        String schema = testObject.getCurrentSchema(connection);
        assertEquals("ExpectedSchema", schema); 
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        String connId = testObject.getConnectionId(connection);
        assertEquals("ExpectedConnectionId", connId);
    }

    // Test for exception in switchSchema
    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        Mockito.doThrow(new SQLException()).when(connection).setCatalog(Mockito.anyString());
        testObject.switchSchema(connection, "testSchema");
    }

    // Test for exception in getConnectionId
    @Test(expected = Exception.class)
    public void testGetConnectionIdException() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObject = new OBMySQLSessionExtension();
        Mockito.doThrow(new Exception()).when(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(Mockito.anyString(), Mockito.any(Class.class));
        testObject.getConnectionId(connection);
    }
}
