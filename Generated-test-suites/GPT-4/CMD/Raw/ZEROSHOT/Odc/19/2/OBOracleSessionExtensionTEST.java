
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil.JdbcOperations;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        jdbcOperations = mock(JdbcOperations.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "NewSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("OldSchema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("1");
        String result = obOracleSessionExtension.getConnectionId(connection);
        verify(jdbcOperations).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        String querySql = "select userenv('sessionid') from dual";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenThrow(Exception.class);
        connection = mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn((long) 1);
        String result = obOracleSessionExtension.getConnectionId(connection);
        verify(((OceanBaseConnection) connection)).getServerThreadId();
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("SchemaName");
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        verify(jdbcOperations).queryForObject(querySql, String.class);
    }
}
