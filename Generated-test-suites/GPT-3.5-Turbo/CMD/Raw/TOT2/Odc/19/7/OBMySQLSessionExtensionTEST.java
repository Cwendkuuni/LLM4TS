import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockedConnection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(mockedConnection, connectionId);

        // Verify that the JdbcOperationsUtil.execute method was called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(mockedConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(mockedConnection.getCatalog()).thenReturn("existing_schema");

        obMySQLSessionExtension.switchSchema(mockedConnection, schemaName);

        // Verify that connection.setCatalog was called with the correct schemaName
        verify(mockedConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockedConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);

        String result = obMySQLSessionExtension.getCurrentSchema(mockedConnection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(mockedConnection).queryForObject("select connection_id()", String.class)).thenReturn(connectionId);

        String result = obMySQLSessionExtension.getConnectionId(mockedConnection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdException() {
        when(JdbcOperationsUtil.getJdbcOperations(mockedConnection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());

        String connectionId = "56789";
        when(mockedConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockedConnection).getServerThreadId()).thenReturn(56789L);

        String result = obMySQLSessionExtension.getConnectionId(mockedConnection);

        assertEquals("56789", result);
    }
}