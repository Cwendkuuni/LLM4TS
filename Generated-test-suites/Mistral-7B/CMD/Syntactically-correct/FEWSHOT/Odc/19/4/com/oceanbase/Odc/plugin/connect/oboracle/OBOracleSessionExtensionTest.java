package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import com.oceanbase.jdbc.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.StringUtils;

@Slf4j
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBOracleSessionExtension ut;

    private Connection connection;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        ut.killQuery(connection, "connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryException() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Exception"));
        ut.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String currentSchema = "schemaName";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection).update(anyString())).thenReturn(1);
        when(ut.getCurrentSchema(connection)).thenReturn(currentSchema);
        ut.switchSchema(connection, currentSchema);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        String currentSchema = "currentSchema";
        String schemaName = "schemaToSwitch";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(ut.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(jdbcOperationsUtil.getJdbcOperations(connection).update(anyString())).thenReturn(1);
        ut.switchSchema(connection, schemaName);
        assertEquals(schemaName, ut.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Exception"));
        ut.switchSchema(connection, "schemaName");
    }

    @Test
    public void testGetCurrentSchemaNullConnection() {
        assertNull(ut.getCurrentSchema(null));
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), anyString())).thenReturn("schemaName");
        assertEquals("schemaName", ut.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdOracle() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), anyString())).thenReturn("threadId");
        assertEquals("threadId", ut.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdDual() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), String.class)).thenReturn("connectionId");
        assertEquals("connectionId", ut.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNull() {
        assertNull(ut.getConnectionId(null));
    }
}