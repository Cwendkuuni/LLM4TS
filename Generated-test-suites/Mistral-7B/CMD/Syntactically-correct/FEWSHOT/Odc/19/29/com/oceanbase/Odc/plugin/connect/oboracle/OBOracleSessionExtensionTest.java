package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@Slf4j
public class OBOracleSessionExtensionTest {

    private JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    private Connection connection = mock(Connection.class);
    private OBOracleSessionExtension obj = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "connection_1";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new Object() {
            public void execute(String sql) throws SQLException {
                // Do nothing
            }
        });
        obj.killQuery(connection, connectionId);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcOperationsUtil).getJdbcOperations(connection);
        verify(jdbcOperationsUtil).getJdbcOperations(connection).execute(sqlCaptor.capture());
        assertEquals("KILL QUERY " + connectionId, sqlCaptor.getValue());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new Object() {
            public String queryForObject(String sql, Class type) throws SQLException {
                if (type == String.class) {
                    if (sql.startsWith("SELECT SYS_CONTEXT")) {
                        return currentSchema;
                    } else {
                        return "OK";
                    }
                }
                return null;
            }
        });
        obj.switchSchema(connection, schemaName);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcOperationsUtil).getJdbcOperations(connection).update(sqlCaptor.capture());
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName), sqlCaptor.getValue());
        verify(jdbcOperationsUtil).getJdbcOperations(connection).queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(currentSchema));
        verify(jdbcOperationsUtil).getJdbcOperations(connection).queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), any());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "current_schema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new Object() {
            public String queryForObject(String sql, Class type) throws SQLException {
                if (type == String.class) {
                    return currentSchema;
                }
                return null;
            }
        });
        assertEquals(currentSchema, obj.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "connection_1";
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new Object() {
            public String queryForObject(String sql, Class type) throws SQLException {
                if (type == String.class && sql.startsWith("select userenv")) {
                    return null;
                }
                return connectionId;
            }
        });
        assertEquals(connectionId, obj.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_null() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(obj.getConnectionId(connection));
    }
}