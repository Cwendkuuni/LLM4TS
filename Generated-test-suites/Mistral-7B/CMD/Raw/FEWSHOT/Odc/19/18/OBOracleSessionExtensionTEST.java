package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.api.JdbcOperationsUtilTest;

@Slf4j
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(1);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);

        // No exception is thrown, so test is a success
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenThrow(new SQLException("Test exception"));
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)))
                .thenReturn(1);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // No exception is thrown, so test is a success
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)))
                .thenThrow(new SQLException("Test exception"));
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(currentSchema);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String schema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, schema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(connection, "select userenv('sessionid') from dual", String.class))
                .thenReturn(connectionId);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String id = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, id);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String id = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, id);
    }

    @Test
    public void testGetConnectionId_Null() throws SQLException {
        Connection connection = null;

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String id = obOracleSessionExtension.getConnectionId(connection);

        assertNull(id);
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = mock(Connection.class);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(connection, "select userenv('sessionid') from dual", String.class))
                .thenThrow(new SQLException("Test exception"));

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String id = obOracleSessionExtension.getConnectionId(connection);

        assertNull(id);
    }

    @org.junit.Before
    public void mockJdbcOperationsUtil() throws Exception {
        MockedStatic<JdbcOperationsUtil> jdbcOperationsUtilMock = mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenCallRealMethod();
    }
}