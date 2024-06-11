
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private OceanBaseConnection oceanBaseConnection;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        oceanBaseConnection = mock(OceanBaseConnection.class);
        jdbcOperations = mock(JdbcOperations.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "mySchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "userenv('sessionid')";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        String connectionId = "12345";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}
