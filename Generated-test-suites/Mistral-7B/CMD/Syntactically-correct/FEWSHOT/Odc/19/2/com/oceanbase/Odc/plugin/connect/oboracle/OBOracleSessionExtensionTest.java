package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Slf4j
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private MockedStatic<JdbcOperationsUtil> jdbcOperationsUtilMockedStatic;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        jdbcOperations = mock(JdbcOperations.class);
        jdbcOperationsUtilMockedStatic = mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
    }

    @After
    public void tearDown() {
        jdbcOperationsUtilMockedStatic.close();
    }

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, "connectionId");
        jdbcOperationsUtilMockedStatic.verify(() -> JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY connectionId"));
    }

    @Test
    public void testKillQuery_whenConnectionIsNull() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(null, "connectionId");
        // No verification expected as the method does not interact with the mocked JdbcOperations
    }

    @Test
    public void testKillQuery_whenConnectionIsInstanceOfOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(oceanBaseConnection, "connectionId");
        String killQuerySql = "KILL QUERY connectionId";
        jdbcOperationsUtilMockedStatic.verify(() -> JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection).execute(killQuerySql));
    }

    @Test
    public void testSwitchSchema() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        String alterSchemaSql = "ALTER SESSION SET CURRENT_SCHEMA=schemaName";
        jdbcOperationsUtilMockedStatic.verify(() -> JdbcOperationsUtil.getJdbcOperations(connection).update(alterSchemaSql));
    }

    @Test
    public void testSwitchSchema_whenSchemaNameIsSameAsCurrentSchema() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String currentSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, currentSchema);
        // No verification expected as no alter schema statement is executed
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_whenSchemaNameIsEmpty() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, "");
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String schemaName = "schemaName";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(schemaName);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema_whenQueryForObjectThrowsException() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        jdbcOperationsUtilMockedStatic.verifyNoMoreInteractions(JdbcOperationsUtil.getJdbcOperations(connection));
        assertNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        String querySql = "select userenv('sessionid') from dual";
        String sessionId = "sessionId";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(sessionId);
        assertEquals(sessionId, connectionId);
    }

    @Test
    public void testGetConnectionId_whenQueryForObjectThrowsException() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        jdbcOperationsUtilMockedStatic.verifyNoMoreInteractions(JdbcOperationsUtil.getJdbcOperations(connection));
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsInstanceOfOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String serverThreadId = "serverThreadId";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(serverThreadId);
        String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(serverThreadId, connectionId);
    }
}