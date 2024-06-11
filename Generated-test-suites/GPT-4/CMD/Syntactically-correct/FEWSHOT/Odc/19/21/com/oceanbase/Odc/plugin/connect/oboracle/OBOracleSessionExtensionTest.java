
package com.oceanbase.odc.plugin.connect.oboracle;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;
    
    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBOracleSessionExtension oBOracleSessionExtension;

    @Before
    public void setUp() {
        oBOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        oBOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        when(oBOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        oBOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("schema");
        String schema = oBOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("schema", schema);
    }

    @Test
    public void testGetConnectionId() {
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenThrow(Exception.class);
        when(connection instanceof OceanBaseConnection).thenReturn("1");
        String connectionId = oBOracleSessionExtension.getConnectionId(connection);
        assertEquals("1", connectionId);
    }
}
