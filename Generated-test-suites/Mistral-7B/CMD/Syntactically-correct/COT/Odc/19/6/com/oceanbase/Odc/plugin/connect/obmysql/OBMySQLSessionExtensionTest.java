package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.util.Extensions;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class, JdbcOperationsUtil.class})
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_Success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        PowerMockito.verifyStatic();
        connection.setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getCurrentSchema(connection)).thenReturn(currentSchema);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema_2";
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getCurrentSchema(connection)).thenReturn(currentSchema);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        PowerMockito.verifyStatic();
        connection.setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema_Success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_Success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        when(JdbcOperationsUtil.queryForObject(connection, "select connection_id()", String.class)).thenReturn(connectionId);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        String serverThreadId = "test_server_thread_id";
        when(connection.getServerThreadId()).thenReturn(serverThreadId);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        assertEquals(serverThreadId, obMySQLSessionExtension.getConnectionId(connection));
    }
}