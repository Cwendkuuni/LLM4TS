import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {
    private Connection connection;
    private JdbcOperations jdbcOperations;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        jdbcOperations = mock(JdbcOperations.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(connection.createStatement()).thenReturn(jdbcOperations.createStatement());
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verifyKillQuery(connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchemaName";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, getSchemaNameFromConnection());
    }

    @Test
    public void testgetCurrentSchema() {
        String currentSchema = "testCurrentSchema";
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return currentSchema;
            }
        });
        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    private void verifyKillQuery(String connectionId) {
        String querySql = "KILL QUERY " + connectionId;
        when(jdbcOperations.execute(querySql)).thenReturn(0);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_CurrentSchemaEquals() throws SQLException {
        String schemaName = "testSchemaName";
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    private String getSchemaNameFromConnection() {
        String querySql = "SELECT DATABASE()";
        return jdbcOperations.queryForObject(querySql, String.class);
    }

    @Test
    public void testgetConnectionId() throws SQLException {
        obMySQLSessionExtension.getConnectionId(connection);
        String connectionId = ((OceanBaseConnection) connection).getServerThreadId();
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testgetConnectionId_Exception() throws SQLException {
        obMySQLSessionExtension.getConnectionId(connection);
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testSwitchSchema_EmptySchemaName() throws SQLException {
        String schemaName = "";
        try {
            obMySQLSessionExtension.switchSchema(connection, schemaName);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testgetCurrentSchema_EmptyResult() throws SQLException {
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException("Empty result"));
        try {
            obMySQLSessionExtension.getCurrentSchema(connection);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testGetConnectionId_NoOceanBaseConnection() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testKillQuery_NullConnection() {
        String connectionId = "testConnectionId";
        try {
            obMySQLSessionExtension.killQuery(null, connectionId);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSwitchSchema_NullConnection() {
        String schemaName = "testSchemaName";
        try {
            obMySQLSessionExtension.switchSchema(null, schemaName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSwitchSchema_NullSchemaName() {
        String schemaName = null;
        try {
            obMySQLSessionExtension.switchSchema(connection, schemaName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}