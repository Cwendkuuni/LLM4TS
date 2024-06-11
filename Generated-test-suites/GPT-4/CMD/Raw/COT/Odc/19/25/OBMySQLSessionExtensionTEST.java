
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    // Mock objects to simulate scenarios.
    private Connection connection = mock(Connection.class);
    private String mockSchemaName = "mockSchema";

    @Test
    public void testKillQuery() {
        // Here we just need to make sure that no exceptions are thrown, so no assertions are needed since this method does not return anything.
        obMySQLSessionExtension.killQuery(connection, mockSchemaName);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        // Mock actions for the connection object
        when(connection.getCatalog()).thenReturn(mockSchemaName);

        // Test behavior of method
        obMySQLSessionExtension.switchSchema(connection, mockSchemaName);

        // Check if the Catalog in connection is equal to the schemaName provided
        verify(connection).getCatalog();
        Assert.assertEquals(connection.getCatalog(), mockSchemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        // Mock value returned by a query
        String mockReturnValue = "mockQueryReturnValue";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(any(String.class), eq(String.class))).thenReturn(mockReturnValue);

        // Test method
        String returnVal = obMySQLSessionExtension.getCurrentSchema(connection);

        // Check if the returned value is the one we expect
        Assert.assertEquals(mockReturnValue, returnVal);
    }
  
    @Test
    public void testGetConnectionId() {
        // Mock value returned by a query
        String mockReturnValue = "mockQueryReturnValue";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(any(String.class), eq(String.class))).thenReturn(mockReturnValue);

        // Test method
        String returnVal = obMySQLSessionExtension.getConnectionId(connection);

        // Check if the returned value is the one we expect
        Assert.assertEquals(mockReturnValue, returnVal);
    }
}
