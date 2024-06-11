package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("static-access")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;
    private JdbcOperationsUtil jdbcOperationsUtil;
    private MockedStatic<JdbcOperationsUtil> jdbcOperationsUtilMock;

    @Before
    public void setUp() throws SQLException {
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        jdbcOperationsUtilMock = mockStatic(JdbcOperationsUtil.class);
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        obOracleSessionExtension.killQuery(connection, "connectionId");
        jdbcOperationsUtilMock.verify(() -> jdbcOperationsUtil.execute("KILL QUERY connectionId"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(StringUtils.EMPTY);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        jdbcOperationsUtilMock.verify(() -> jdbcOperationsUtil.update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName));
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, currentSchema);
        // No verification needed as no SQL statement is executed when the current and target schemas are the same
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=newSchemaName")).thenThrow(new SQLException());
        obOracleSessionExtension.switchSchema(connection, "newSchemaName");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullResult() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(null);
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "select userenv('sessionid') from dual")).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullResult() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "select userenv('sessionid') from dual")).thenReturn(null);
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = "testConnectionId";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }
}