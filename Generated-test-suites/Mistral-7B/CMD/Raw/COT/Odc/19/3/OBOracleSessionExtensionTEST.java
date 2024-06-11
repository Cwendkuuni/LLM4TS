package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;

/**
 * Test class for OBOracleSessionExtension
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint extensionPoint;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() throws SQLException {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        extensionPoint = new OBOracleSessionExtension() {
            @Override
            protected JdbcOperations getJdbcOperations() {
                return jdbcOperationsMock;
            }
        };
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        extensionPoint.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperationsMock).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(schemaName);
        extensionPoint.switchSchema(connection, schemaName);
        Mockito.verify(jdbcOperationsMock, Mockito.never()).update(Mockito.anyString());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        String currentSchema = "currentSchema";
        String schemaName = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(currentSchema);
        Mockito.when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        extensionPoint.switchSchema(connection, schemaName);
        Mockito.verify(jdbcOperationsMock).update("ALTER SESSION SET CURRENT_SCHEMA=testSchema");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_sqlException() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenThrow(new SQLException("Error switching schema"));
        extensionPoint.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(schemaName);
        assertEquals(schemaName, extensionPoint.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_connectionObject() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        Mockito.when(connection.getClass().getMethod("getServerThreadId")).thenReturn(connectionId);
        assertEquals(connectionId, extensionPoint.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_notOceanBaseConnection() throws SQLException {
        Mockito.when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(false);
        assertNull(extensionPoint.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_sqlException() throws SQLException {
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenThrow(new SQLException("Error getting connection id"));
        assertNull(extensionPoint.getConnectionId(connection));
    }
}