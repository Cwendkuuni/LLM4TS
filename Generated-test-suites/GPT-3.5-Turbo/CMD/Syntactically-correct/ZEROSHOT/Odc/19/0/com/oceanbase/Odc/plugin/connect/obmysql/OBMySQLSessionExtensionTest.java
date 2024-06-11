import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(mockConnection.getCatalog()).thenReturn("current_schema");
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test_schema";
        when(mockConnection.getCatalog()).thenReturn(expectedSchema);
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "12345";
        when(mockConnection.createStatement().executeQuery("select connection_id()")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(expectedConnectionId);
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}