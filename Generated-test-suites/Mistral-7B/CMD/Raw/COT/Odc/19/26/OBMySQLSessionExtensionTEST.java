package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.util.JdbcOperationsUtil;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private OBMySQLSessionExtension extension;

    @Before
    public void setup() throws SQLException {
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        connection = Mockito.mock(Connection.class);
        extension = new OBMySQLSessionExtension() {
            {
                this.jdbcOperations = jdbcOperations;
            }
        };
    }

    @Test
    public void testKillQuery() throws SQLException {
        extension.killQuery(connection, "testConnectionId");
        Mockito.verify(jdbcOperations).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "testSchemaName";
        String currentSchema = "testCurrentSchema";

        Mockito.when(jdbcOperations.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        extension.switchSchema(connection, schemaName);

        Mockito.verify(connection).setCatalog(schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaAlreadyInSchema() throws SQLException {
        String schemaName = "testSchemaName";
        String currentSchema = schemaName;

        Mockito.when(jdbcOperations.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "testSchemaName";
        Mockito.when(jdbcOperations.queryForObject("SELECT DATABASE()", String.class)).thenReturn(schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdFromConnection() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(jdbcOperations.queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        Mockito.when(connection.getClass().getMethod("getServerThreadId")).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdFromJdbcOperationsThrowsException() throws SQLException {
        Mockito.when(jdbcOperations.queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> extension.getConnectionId(connection));
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdFromNullConnection() {
        assertThrows(NullPointerException.class, () -> extension.getConnectionId(null));
    }
}