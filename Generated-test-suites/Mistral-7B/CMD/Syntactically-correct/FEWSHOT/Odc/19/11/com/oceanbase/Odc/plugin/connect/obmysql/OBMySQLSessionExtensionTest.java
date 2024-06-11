package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";

        try {
            extension.killQuery(connection, connectionId);
            Mockito.verify(connection).createStatement();
            Mockito.verify(connection.createStatement()).execute("KILL QUERY " + connectionId);
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "test_current_schema";

        Mockito.when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        Mockito.when(connection.setCatalog(schemaName)).thenReturn(true);

        extension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String querySql = "SELECT DATABASE()";
        String currentSchema = "test_schema";

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String querySql = "select connection_id()";
        String connectionId = "test_connection_id";

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));

        connection = Mockito.mock(OceanBaseConnection.class);
        String serverThreadId = "test_server_thread_id";

        Mockito.when(connection.getServerThreadId()).thenReturn(Long.parseLong(serverThreadId));

        assertEquals(serverThreadId, extension.getConnectionId(connection));

        connection = Mockito.mock(Connection.class);

        assertNull(extension.getConnectionId(connection));
    }

    @Test
    public void testExtensionRegistration() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        extensionManager.addExtension(extension);

        SessionExtensionPoint sessionExtensionPoint = extensionManager.getExtension(SessionExtensionPoint.class);
        assertNotNull(sessionExtensionPoint);
        assertEquals(OBMySQLSessionExtension.class, sessionExtensionPoint.getClass());
    }

    @Test
    public void testExtensionUsage() throws Exception {
        String url = "jdbc:oceanbase:localhost:10000/test_database";
        Properties properties = new Properties();
        Connection connection = DriverManager.getConnection(url, properties);

        SessionExtensionPoint sessionExtensionPoint = new DefaultExtensionPoint();
        ExtensionManager extensionManager = new DefaultExtensionManager();

        extensionManager.addExtension(new OBMySQLSessionExtension());
        extensionManager.loadExtensions();

        sessionExtensionPoint.setExtensionManager(extensionManager);
        OBMySQLSessionExtension extension = sessionExtensionPoint.getExtension(OBMySQLSessionExtension.class);

        assertNotNull(extension);
        extension.switchSchema(connection, "test_schema");
    }
}