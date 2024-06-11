package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("resultofmethodcallsignature")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection.getConnection()).thenReturn(connection);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenAnswer(i -> "schemaName");
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperations).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName");
        assertEquals("schemaName", currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        assertEquals("schemaName", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        assertEquals("connectionId", obOracleSessionExtension.getConnectionId(connection));

        connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertEquals("serverThreadId", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithoutConnection() {
        obOracleSessionExtension.killQuery(null, "connectionId");
    }

    @Test
    public void testGetCurrentSchemaWithoutConnection() {
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionIdWithoutConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }
}