package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@Extension
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setup() {
        extension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
        jdbcOperationsUtil = Mockito.mock(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "conn-id";
        extension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection).getCurrentSchema()).thenReturn(schemaName);
        extension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, times(1)).getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String actualSchema = extension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "conn-id";
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedId);
        String actualId = extension.getConnectionId(connection);
        assertEquals(expectedId, actualId);
    }
}