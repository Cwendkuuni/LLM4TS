package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension class
 */
@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private static final String TEST_SCHEMA = "test_schema";
    private static final String TEST_CONNECTION_ID = "test_connection_id";
    private Connection connection;
    private OBOracleSessionExtension extension;

    @Before
    public void setup() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        extension = (OBOracleSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        connection = Mockito.mock(Connection.class);
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.when(connection.isClosed()).thenThrow(new SQLException("Connection is closed"));
        extension.init(connection);
    }

    /**
     * Test method for killQuery(Connection, String)
     */
    @Test
    public void testKillQuery() throws Exception {
        extension.killQuery(connection, TEST_CONNECTION_ID);
        verifyKillQuery(connection, TEST_CONNECTION_ID);
    }

    /**
     * Test method for killQuery(Connection, String) with connection is closed
     */
    @Test(expected = SQLException.class)
    public void testKillQueryWithConnectionClosed() throws Exception {
        Mockito.when(connection.isClosed()).thenReturn(true);
        extension.killQuery(connection, TEST_CONNECTION_ID);
    }

    /**
     * Test method for switchSchema(Connection, String)
     */
    @Test
    public void testSwitchSchema() throws Exception {
        String currentSchema = extension.getCurrentSchema(connection);
        assertNotEquals(TEST_SCHEMA, currentSchema);
        extension.switchSchema(connection, TEST_SCHEMA);
        assertEquals(TEST_SCHEMA, extension.getCurrentSchema(connection));
    }

    /**
     * Test method for switchSchema(Connection, String) with current schema is the same
     */
    @Test
    public void testSwitchSchemaWithSameSchema() throws Exception {
        String currentSchema = TEST_SCHEMA;
        extension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    /**
     * Test method for getCurrentSchema(Connection)
     */
    @Test
    public void testgetCurrentSchema() throws Exception {
        String currentSchema = extension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    /**
     * Test method for getConnectionId(Connection)
     */
    @Test
    public void testGetConnectionId() throws Exception {
        Optional<String> connectionId = Optional.ofNullable(extension.getConnectionId(connection));
        assertTrue(connectionId.isPresent());
        assertEquals(TEST_CONNECTION_ID, connectionId.get());

        Mockito.when(connection.isClosed()).thenReturn(true);
        connectionId = Optional.ofNullable(extension.getConnectionId(connection));
        assertFalse(connectionId.isPresent());

        OceanBaseConnection obConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(connection.unwrap(OceanBaseConnection.class)).thenReturn(obConnection);
        connectionId = Optional.ofNullable(extension.getConnectionId(connection));
        assertEquals(obConnection.getServerThreadId(), connectionId.get());
    }

    /**
     * Test method for getConnectionId(Connection) with connection is closed
     */
    @Test
    public void testGetConnectionIdWithConnectionClosed() throws Exception {
        Mockito.when(connection.isClosed()).thenReturn(true);
        Optional<String> connectionId = Optional.ofNullable(extension.getConnectionId(connection));
        assertFalse(connectionId.isPresent());
    }

    private void verifyKillQuery(Connection connection, String connectionId) throws SQLException {
        String querySql = "KILL QUERY " + connectionId;
        Mockito.verify(connection.createStatement(), Mockito.times(1)).execute(querySql);
    }

}