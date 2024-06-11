package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.tests.AbstractExtensionTest;
import org.pf4j.util.Props;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

    private JdbcOperations jdbcOperations;
    private SessionExtensionPoint obOracleSessionExtension;

    @Before
    public void setUp() throws Exception {
        jdbcOperations = mock(JdbcOperations.class);
        Props props = new Props();
        obOracleSessionExtension = new OBOracleSessionExtension();
        props.put("jdbcOperationsUtil.jdbcOperations", jdbcOperations);
        obOracleSessionExtension.init(props);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(1);

        obOracleSessionExtension.killQuery(connection, connectionId);

        // No assertions needed as the test relies on the return value from the mocked JdbcOperations.
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(jdbcOperations.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(jdbcOperations.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(jdbcOperations.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);

        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() throws SQLException {
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        when(jdbcOperations.queryForObject(connection, "select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";

        when(connection.getServerThreadId()).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return obOracleSessionExtension.getCurrentSchema(connection);
    }
}