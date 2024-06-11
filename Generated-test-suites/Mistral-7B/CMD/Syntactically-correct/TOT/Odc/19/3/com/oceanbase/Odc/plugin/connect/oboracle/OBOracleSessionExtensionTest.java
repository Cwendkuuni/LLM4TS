package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("resultofMethodCallIgnored")
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
    }

    @Test
    public void testKillQuerySuccess() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperations.class));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        assertEquals("KILL QUERY testConnectionId", obOracleSessionExtension.killQueryStatement(connection, "testConnectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryFailure() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test SQLException"));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperations.class));
        obOracleSessionExtension.switchSchema(connection, "testSchemaName");
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=testSchemaName", obOracleSessionExtension.switchSchemaStatement(connection, "testSchemaName"));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFailure() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test SQLException"));
        obOracleSessionExtension.switchSchema(connection, "testSchemaName");
    }

    @Test
    public void testGetCurrentSchemaSuccess() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperations.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq("CURRENT_SCHEMA"))).thenReturn("testSchemaName");
        assertEquals("testSchemaName", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaFailure() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test SQLException"));
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdSuccess() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperations.class));
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFailed() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test SQLException"));
        assertEquals(null, obOracleSessionExtension.getConnectionId(connection));
    }

    private static <T> java.sql.Statement mock(Class<T> clazz) {
        return mock(java.sql.Statement.class);
    }

    private static <T> Object eq(T value) {
        return eq(value.getClass(), value);
    }

    private static <T> Object eq(Class<T> clazz, T value) {
        return org.hamcrest.Matchers.equalTo(value);
    }

    private static class JdbcOperationsUtilMock {
        // JdbcOperationsUtil mock implementation
    }

    private static class JdbcOperations {
        // JdbcOperations mock implementation
    }
}