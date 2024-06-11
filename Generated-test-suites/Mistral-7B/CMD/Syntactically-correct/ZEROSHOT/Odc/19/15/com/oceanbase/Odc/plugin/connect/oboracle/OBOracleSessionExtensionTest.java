package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private Connection connection;
    private OBOracleSessionExtension extension;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        extension = new OBOracleSessionExtension();
    }

    @Test
    public void killQueryShouldExecuteKillQueryStatement() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(1);

        extension.killQuery(connection, "test_connection_id");

        Mockito.verify(jdbcOperations).execute("KILL QUERY test_connection_id");
    }

    @Test
    public void killQueryShouldReturnNullIfNoError() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(1);

        assertNull(extension.killQuery(connection, "test_connection_id"));
    }

    @Test(expected = SQLException.class)
    public void killQueryShouldThrowExceptionIfJdbcOperationsThrowsException() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenThrow(new SQLException());

        extension.killQuery(connection, "test_connection_id");
    }

    @Test
    public void switchSchemaShouldExecuteAlterSessionStatement() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        String alteredSchema = "altered_schema";

        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);

        extension.switchSchema(connection, schemaName);

        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(alteredSchema, extension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void switchSchemaShouldThrowExceptionIfJdbcOperationsThrowsException() throws SQLException {
        String schemaName = "test_schema";

        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("current_schema");
        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException());

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void getCurrentSchemaShouldReturnCurrentSchema() throws SQLException {
        String currentSchema = "current_schema";

        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void getConnectionIdShouldReturnConnectionIdFromJdbcOperations() throws SQLException {
        String connectionId = "test_connection_id";

        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void getConnectionIdShouldReturnThreadIdIfJdbcOperationsReturnsNull() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn("123");

        assertEquals("123", extension.getConnectionId(connection));
    }

    @Test
    public void getConnectionIdShouldReturnNullIfJdbcOperationsReturnsNullAndConnectionIsNotOceanBaseConnection() throws SQLException {
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);

        assertNull(extension.getConnectionId(connection));
    }
}