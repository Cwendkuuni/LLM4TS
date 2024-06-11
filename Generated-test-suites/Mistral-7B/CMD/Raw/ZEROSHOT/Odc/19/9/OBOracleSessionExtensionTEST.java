package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperations;

/**
 * Test class for OBOracleSessionExtension.
 *
 * @author yaobin
 */
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint extension;
    private Connection connection;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        when(extension.getJdbcOperations(connection)).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "test_connection_id";

        try {
            extension.killQuery(connection, connectionId);
            Mockito.verify(jdbcOperations).execute("KILL QUERY " + connectionId);
        } catch (SQLException e) {
            // expected
        }
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() {
        String connectionId = "test_connection_id";

        when(jdbcOperations.execute(Mockito.anyString())).thenThrow(new SQLException("Test exception"));

        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        String newSchema = "new_schema";

        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);

        Mockito.verify(jdbcOperations).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "test_schema";

        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);
        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException("Test exception"));

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "current_schema";

        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "test_connection_id";

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);

        assertEquals(connectionId, extension.getConnectionId(connection));

        connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(false);
        when(extension.getConnectionId(connection)).thenReturn(Optional.of("test_connection_id_2"));

        assertEquals("test_connection_id_2", extension.getConnectionId(connection).get());
    }

    @Test
    public void testGetConnectionIdNull() {
        when(extension.getConnectionId(connection)).thenReturn(Optional.empty());

        assertNull(extension.getConnectionId(connection));
    }
}