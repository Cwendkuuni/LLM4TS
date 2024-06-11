package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtension;
    private Connection connection;
    private ExtensionManager extensionManager;

    @Before
    public void init() throws Exception {
        extensionManager = Mockito.mock(ExtensionManager.class);
        connection = Mockito.mock(Connection.class);
        sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setExtensionManager(extensionManager);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        sessionExtension.killQuery(connection, "123");
        Mockito.verify(connection.createStatement()).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn(getCurrentSchema(connection));
        sessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaFail() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn(schemaName);
        assertThrows(SQLException.class, () -> sessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        String schemaName = "testSchema";
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).next()).thenReturn(true);
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).getString("DATABASE()")).thenReturn(schemaName);
        assertEquals(schemaName, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdSuccess() throws SQLException {
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).next()).thenReturn(true);
        Mockito.when(connection.createStatement().executeQuery(Mockito.anyString()).getString("connection_id()")).thenReturn("123");
        assertEquals("123", sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFail() throws SQLException {
        Mockito.when(connection.createStatement()).thenThrow(SQLException.class);
        assertNotNull(sessionExtension.getConnectionId(connection));
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
    }
}