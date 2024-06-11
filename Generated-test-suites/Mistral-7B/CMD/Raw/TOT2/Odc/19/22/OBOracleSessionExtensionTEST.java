import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() {
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        extension = new OBOracleSessionExtension() {
            {
                JdbcOperationsUtil = jdbcOperationsUtil;
            }
        };
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "testConnectionId";
        extension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
        assertNoThrows(Exception.class, () -> extension.killQuery(connection, connectionId));

        // Edge cases and exception handling scenarios test
        assertThrows(NullPointerException.class, () -> extension.killQuery(null, connectionId));
        assertThrows(NullPointerException.class, () -> extension.killQuery(connection, null));
        assertThrows(NullPointerException.class, () -> extension.killQuery(null, null));
        assertThrows(SQLException.class, () -> extension.killQuery(connection, ""));
        assertThrows(SQLException.class, () -> extension.killQuery(null, "testConnectionId"));
        assertThrows(SQLException.class, () -> extension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchemaName";
        String currentSchema = "testCurrentSchema";
        when(jdbcOperationsUtil.queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(currentSchema);
        when(jdbcOperationsUtil.update(eq("ALTER SESSION SET CURRENT_SCHEMA=" + eq("testSchemaName")))).thenReturn(1);

        extension.switchSchema(connection, schemaName);
        ArgumentCaptor<String> alterSqlArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcOperationsUtil).update(alterSqlArgumentCaptor.capture());
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=testSchemaName", alterSqlArgumentCaptor.getValue());

        // Edge cases and exception handling scenarios test
        assertThrows(NullPointerException.class, () -> extension.switchSchema(null, schemaName));
        assertThrows(NullPointerException.class, () -> extension.switchSchema(connection, null));
        assertThrows(NullPointerException.class, () -> extension.switchSchema(null, null));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, ""));
        assertThrows(SQLException.class, () -> extension.switchSchema(null, "testSchemaName"));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, ""));

        // Test if the schema already is the target schema
        when(jdbcOperationsUtil.queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(schemaName);
        assertDoesNotThrow(() -> extension.switchSchema(connection, schemaName));

        // Test if the `switchSchema` method throws a `SQLException`
        when(jdbcOperationsUtil.update(eq("ALTER SESSION SET CURRENT_SCHEMA=" + eq("testSchemaName")))).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String currentSchema = "testCurrentSchema";
        when(jdbcOperationsUtil.queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String connectionId = "testConnectionId";
        when(jdbcOperationsUtil.queryForObject(eq("select userenv('sessionid') from dual"), eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));

        // Edge cases and exception handling scenarios test
        when(jdbcOperationsUtil.queryForObject(eq("select userenv('sessionid') from dual"), eq(String.class))).thenThrow(new Exception());
        assertThrows(Exception.class, () -> extension.getConnectionId(connection));

        // Test if the connection is an OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        assertNotNull(extension.getConnectionId(oceanBaseConnection));
        assertEquals(oceanBaseConnection.getServerThreadId() + "", extension.getConnectionId(oceanBaseConnection));
    }
}