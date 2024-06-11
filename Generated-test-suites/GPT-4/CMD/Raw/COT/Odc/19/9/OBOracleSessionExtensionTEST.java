
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {
    private Connection connection;
    private OceanBaseConnection oceanBaseConnection;
    private OBOracleSessionExtension ext;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        oceanBaseConnection = mock(OceanBaseConnection.class);
        ext = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        ext.killQuery(connection, "1");  // no exception means success
    }

    @Test
    public void testSwitchSchema() {
	setupCurrentSchema("schema");
	ext.switchSchema(connection, "schema");  // no exception means success
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "schema";
	setupCurrentSchema(expectedSchema);
	assertEquals(expectedSchema, ext.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "1";
	when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
	    .thenReturn(expectedId);
	assertEquals(expectedId, ext.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        String expectedId = "2";
	when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
	    .thenThrow(new RuntimeException());
	when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(expectedId));
	assertEquals(expectedId, ext.getConnectionId(connection));
    }

    private void setupCurrentSchema(String schemaName) {
	when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
	    .thenReturn(schemaName);
    }
}
