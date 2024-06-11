
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.springframework.jdbc.core.JdbcOperations;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {
    @Mock
    private Connection connection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    @Mock
    private JdbcOperations operations;

    private OBMySQLSessionExtension oBMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "newSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(operations);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("oldSchema");
        oBMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(operations);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = oBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(operations);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String actualConnectionId = oBMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when((oceanBaseConnection).getServerThreadId()).thenReturn(123L);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(operations);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        String actualConnectionId = oBMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", actualConnectionId);
    }
}
