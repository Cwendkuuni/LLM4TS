package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultPluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.getMetaData()).thenReturn(mock(java.sql.DatabaseMetaData.class));
        when(connection.isClosed()).thenReturn(false);

        obMySQLSessionExtension.killQuery(connection, connectionId);

        assertEquals("KILL QUERY 123", JdbcOperationsUtil.getJdbcOperations(connection).getLastExecutedStatement());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "oldSchema";

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        when(connection.getCatalog()).thenReturn(schemaName);
        assertEquals(schemaName, connection.getCatalog());
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        assertNull(connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("DATABASE_NAME")).thenReturn(currentSchema);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "123";

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        when(connection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Null() throws SQLException {
        Connection connection = mock(Connection.class);

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = mock(Connection.class);

        PluginWrapper pluginWrapper = new DefaultPluginWrapper();
        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint<>();
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(connection);

        pluginWrapper.setExtensions(new OBMySQLSessionExtension[]{obMySQLSessionExtension});
        pluginWrapper.setExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);

        try {
            obMySQLSessionExtension.getConnectionId(connection);
            fail();
        } catch (Exception e) {
            // Expect exception
        }
    }
}