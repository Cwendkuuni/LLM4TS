
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperationsUtil.JdbcOperations jdbcOperations;

    private OBOracleSessionExtension extension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        extension.killQuery(connection, "conn1");
        verify(jdbcOperations).execute("KILL QUERY conn1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getSchema()).thenReturn("Schema1");
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        extension.switchSchema(connection, "Schema2");
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=\"Schema2\"");
    }

    @Test
    public void testNoSwitchSchema() throws SQLException {
        when(connection.getSchema()).thenReturn("Schema1");
        extension.switchSchema(connection, "Schema1");
        verifyZeroInteractions(jdbcOperations);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("Schema1");
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("Schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("conn1");
        String connectionId = extension.getConnectionId(connection);
        assertEquals("conn1", connectionId);
    }

    @Test
    public void testGetOceanBaseConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when((OceanBaseConnection) connection).getServerThreadId().thenReturn(1L);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("conn1");
        String connectionId = extension.getConnectionId(oceanBaseConnection);
        assertEquals("1", connectionId);
    }
}
