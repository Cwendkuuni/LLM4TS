package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() throws Exception {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperations.execute(Objects.requireNonNull(obOracleSessionExtension, "obOracleSessionExtension is null")))
                .thenReturn(true);

        obOracleSessionExtension.killQuery(connection, connectionId);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        assertEquals("KILL QUERY " + connectionId, sqlCaptor.getValue(jdbcOperations.getLastExecutedStatement()));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnection() {
        obOracleSessionExtension.killQuery(null, "12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperations.queryForObject(Objects.requireNonNull(obOracleSessionExtension, "obOracleSessionExtension is null"),
                String.class))
                .thenReturn(Objects.requireNonNull(StringUtils.quoteOracleIdentifier(schemaName),
                        "Current schema name is null"));
        when(jdbcOperations.update(Objects.requireNonNull(obOracleSessionExtension, "obOracleSessionExtension is null"),
                String.class)).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);
        ArgumentCaptor<String> alterSqlCaptor = ArgumentCaptor.forClass(String.class);
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName),
                alterSqlCaptor.getValue(jdbcOperations.getLastExecutedStatement()));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() {
        String schemaName = null;
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        when(jdbcOperations.queryForObject(Objects.requireNonNull(obOracleSessionExtension, "obOracleSessionExtension is null"),
                String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testgetCurrentSchemaWithNullConnection() {
        obOracleSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testgetConnectionId() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperations.queryForObject(Objects.requireNonNull(obOracleSessionExtension, "obOracleSessionExtension is null"),
                String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testgetConnectionIdWithNullConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        obOracleSessionExtension.getConnectionId(connection);
        Objects.requireNonNull(connection.getServerThreadId(), "ServerThreadId is null");
    }
}