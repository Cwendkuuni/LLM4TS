package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.exception.OdcSqlException;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Extension
class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @BeforeEach
    void setup() {
        connection = Mockito.mock(OceanBaseConnection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    void testKillQuery() {
        String connectionId = "test_connection_id";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    void testSwitchSchema_success() {
        String schemaName = "test_schema";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        String currentSchema = "old_schema";
        Mockito.when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).executeUpdate("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test
    void testSwitchSchema_sameSchema() {
        String schemaName = "test_schema";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        String currentSchema = schemaName;

        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    void testSwitchSchema_sqlException() {
        String schemaName = "test_schema";
        Mockito.when(connection.createStatement()).thenThrow(new SQLException("Test SQLException"));

        assertThrows(OdcSqlException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    void testGetCurrentSchema() {
        String schemaName = "test_schema";
        Mockito.when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);

        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    void testGetConnectionId_normal() {
        String connectionId = "test_connection_id";
        Mockito.when(connection.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    void testGetConnectionId_exception() {
        Mockito.when(connection.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new Exception("Test Exception"));

        assertEquals("", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    void testGetConnectionId_obConnection() {
        String connectionId = "test_connection_id";
        Mockito.when(connection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }
}