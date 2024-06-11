import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;

@PrepareForTest({OBMySQLSessionExtension.class})
@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

    // Test cases for each method
    // ...

    // Edge cases and exceptions test cases

    @Test
    public void testKillQuery_NullConnection() {
        Executable executable = () -> new OBMySQLSessionExtension().killQuery(null, connectionId);
        Assertions.assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testKillQuery_EmptyConnectionId() {
        Connection mockConnection = Mockito.mock(Connection.class);
        Executable executable = () -> new OBMySQLSessionExtension().killQuery(mockConnection, "");
        Assertions.assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testSwitchSchema_NullConnection() {
        Executable executable = () -> new OBMySQLSessionExtension().switchSchema(null, schemaName);
        Assertions.assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testSwitchSchema_NullSchemaName() {
        Connection mockConnection = Mockito.mock(Connection.class);
        Executable executable = () -> new OBMySQLSessionExtension().switchSchema(mockConnection, "");
        Assertions.assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testSwitchSchema_Success() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        PowerMockito.doNothing().when(mockConnection).setCatalog(schemaName);
        String currentSchema = new OBMySQLSessionExtension().getCurrentSchema(mockConnection);
        Assertions.assertNotEquals(schemaName, currentSchema);
        new OBMySQLSessionExtension().switchSchema(mockConnection, schemaName);
        String newSchema = new OBMySQLSessionExtension().getCurrentSchema(mockConnection);
        Assertions.assertEquals(schemaName, newSchema);
    }

    @Test
    public void testSwitchSchema_Failure() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        PowerMockito.doThrow(new SQLException("Test Error")).when(mockConnection).setCatalog(schemaName);
        Assertions.assertThrows(SQLException.class, () -> new OBMySQLSessionExtension().switchSchema(mockConnection, schemaName));
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        Executable executable = () -> new OBMySQLSessionExtension().getConnectionId(null);
        Assertions.assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testGetConnectionId_NotOceanBaseConnection() {
        Connection mockConnection = Mockito.mock(Connection.class);
        PowerMockito.when(mockConnection.isWrapperFor(OceanBaseConnection.class)).thenReturn(false);
        Executable executable = () -> new OBMySQLSessionExtension().getConnectionId(mockConnection);
        Assertions.assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testGetConnectionId_QueryForObject_Exception() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PowerMockito.when(mockConnection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(PowerMockito.anyString(), PowerMockito.eq(String.class))).thenThrow(new SQLException("Test Error"));
        Executable executable = () -> new OBMySQLSessionExtension().getConnectionId(mockConnection);
        Assertions.assertThrows(SQLException.class, executable);
    }

    // ... (Previous test cases)

}