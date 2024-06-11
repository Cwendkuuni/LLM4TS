package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.ExtensionAdapter;
import org.pf4j.impl.DefaultExtensionPointRegistry;
import org.pf4j.impl.DefaultExtensionPointRegistryInitializer;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testOBMySQLSessionExtension() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(SessionExtensionPoint.class, extension.getClass());

        Extension extensionInstance = new ExtensionAdapter(extension);
        DefaultExtensionPointRegistry extensionPointRegistry = new DefaultExtensionPointRegistry();
        extensionPointRegistry.registerExtension(extensionInstance);
        DefaultExtensionPointRegistryInitializer registryInitializer = new DefaultExtensionPointRegistryInitializer();
        registryInitializer.setRegistry(extensionPointRegistry);
        SessionExtensionPoint sessionExtensionPoint = (SessionExtensionPoint) registryInitializer.getExtensionPoints(SessionExtensionPoint.class.getName()).get(0);
        assert(sessionExtensionPoint instanceof OBMySQLSessionExtension);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String connectionId = "connection_id_1";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(mockConnection, "KILL QUERY " + connectionId)).thenReturn(true);
        sessionExtension.setJdbcOperationsUtil(jdbcOperationsUtil);

        sessionExtension.killQuery(mockConnection, connectionId);
        assertEquals(true, sessionExtension.isQueryKilled(mockConnection, connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String connectionId = "connection_id_1";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(mockConnection, "KILL QUERY " + connectionId)).thenThrow(new SQLException());
        sessionExtension.setJdbcOperationsUtil(jdbcOperationsUtil);

        sessionExtension.killQuery(mockConnection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String schemaName = "test_schema";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        String currentSchema = "current_schema";
        when(mockConnection.getCatalog()).thenReturn(currentSchema);
        sessionExtension.switchSchema(mockConnection, schemaName);
        assertEquals(schemaName, mockConnection.getCatalog());

        when(mockConnection.getCatalog()).thenReturn(null);
        sessionExtension.switchSchema(mockConnection, schemaName);
        assertEquals(schemaName, mockConnection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String currentSchema = "current_schema";
        when(mockConnection.getCatalog()).thenReturn(currentSchema);

        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        String connectionId = "connection_id_1";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(mockConnection.getCatalog()).thenReturn(null);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals(connectionId, sessionExtension.getConnectionId(mockConnection));

        when(mockConnection.getCatalog()).thenReturn("test_schema");
        when(mockConnection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(sessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        String connectionId = "connection_id_1";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertEquals(connectionId, sessionExtension.getConnectionId(oceanBaseConnection));
    }

    @Test
    public void testIsQueryKilled() {
        Connection mockConnection = mock(Connection.class);
        String connectionId = "connection_id_1";
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        sessionExtension.killQuery(mockConnection, connectionId);
        assertTrue(sessionExtension.isQueryKilled(mockConnection, connectionId));

        sessionExtension.killQuery(mockConnection, "other_connection_id");
        assertFalse(sessionExtension.isQueryKilled(mockConnection, connectionId));
    }

    @Test
    public void testIsQueryKilledWithNullConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertFalse(sessionExtension.isQueryKilled(null, "connection_id_1"));
    }

}