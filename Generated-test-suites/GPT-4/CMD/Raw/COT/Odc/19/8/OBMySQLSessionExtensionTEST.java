
import org.junit.Test;
import org.junit.Assert;
import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private Connection mockedConnection = mock(Connection.class);

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockedConnection, "1");
        verify(mockedConnection, times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(mockedConnection.getCurrentSchema()).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(mockedConnection, "schema2");
        verify(mockedConnection, times(1)).setCatalog("schema2");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        when(mockedConnection.getCurrentSchema()).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(mockedConnection, "schema1");
        verify(mockedConnection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expected = "testSchema";
        when(mockedConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expected);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockedConnection);
        Assert.assertEquals(expected, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expected = "testConnectionId";
        when(mockedConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expected);
        String actual = obMySQLSessionExtension.getConnectionId(mockedConnection);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetConnectionIdFallbackToServerThreadId() {
        OceanBaseConnection oceanBaseMockedConnection = mock(OceanBaseConnection.class);
        when(oceanBaseMockedConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(oceanBaseMockedConnection.getServerThreadId()).thenReturn(1234L);
        String actual = obMySQLSessionExtension.getConnectionId(oceanBaseMockedConnection);
        Assert.assertEquals("1234", actual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetConnectionIdExceptionNotOceanBaseConnection() {
        when(mockedConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        obMySQLSessionExtension.getConnectionId(mockedConnection);
    }

}
