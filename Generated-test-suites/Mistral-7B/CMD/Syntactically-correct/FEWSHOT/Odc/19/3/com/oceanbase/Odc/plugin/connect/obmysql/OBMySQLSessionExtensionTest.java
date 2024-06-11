package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionManagerFactory;
import org.pf4j.impl.DefaultPluginWrapper;
import org.pf4j.impl.DefaultServiceManager;
import org.pf4j.impl.DefaultServiceManagerFactory;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint obMySQLSessionExtension;
    private Connection mockConnection;

    @Before
    public void setup() {
        PluginWrapper wrapper = new DefaultPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        extensionManager = new DefaultExtensionManagerFactory().getExtensionManager();
        extensionManager.addExtension(wrapper);

        mockConnection = mock(Connection.class);
    }

    @After
    public void teardown() {
        ExtensionManager extensionManager = this.extensionManager;
        if (extensionManager != null) {
            extensionManager.shutdown();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(mockConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(mockConnection.getSchema()).thenReturn("testSchema");

        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        obMySQLSessionExtension.killQuery(mockConnection, "connection_id");
        assertEquals("KILL QUERY connection_id", JdbcOperationsUtil.getJdbcOperations(mockConnection).executeQuery(mock(java.sql.PreparedStatement.class).toString()));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(mockConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(mockConnection.getSchema()).thenReturn("currentSchema");

        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        obMySQLSessionExtension.switchSchema(mockConnection, "newSchema");
        assertEquals("newSchema", JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(mockConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        assertEquals("currentSchema", obMySQLSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(mockConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        assertEquals("connection_id", obMySQLSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNull() {
        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsOceanBaseConnection() throws SQLException {
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getConnection()).thenReturn(mockConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123);

        OBMySQLSessionExtension obMySQLSessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
        assertEquals("123", obMySQLSessionExtension.getConnectionId(oceanBaseConnection));
    }
}