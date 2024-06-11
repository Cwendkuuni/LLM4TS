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
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension sessionExtension;

    @Before
    public void setUp() {
        jdbcOperations = mock(JdbcOperations.class);
        sessionExtension = new OBOracleSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY " + connectionId))).thenReturn(null);
        sessionExtension.killQuery(connection, connectionId);
        Mockito.verify(jdbcOperations).execute(Mockito.eq("KILL QUERY " + connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY " + connectionId))).thenThrow(new SQLException());
        sessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        sessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(jdbcOperations).update(Mockito.eq("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);
        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String queryResult = schemaName;
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(queryResult);
        assertEquals(schemaName, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullResult() throws SQLException {
        Connection connection = mock(Connection.class);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);
        assertNull(sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithValidConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnectionId() {
        Connection connection = mock(Connection.class);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);
        Optional<String> connectionId = Optional.ofNullable(sessionExtension.getConnectionId(connection));
        assertTrue(connectionId.isPresent());
        assertNull(connectionId.get());
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "123";
        when(connection.getServerThreadId()).thenReturn(connectionId);
        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }
}