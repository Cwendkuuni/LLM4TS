package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "connectionId");
        // Insert verification code here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        // Insert verification code here
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getCurrentSchema(connection)).thenReturn(schemaName);
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, null);
        // Insert verification code here
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("testSchema", currentSchema);
        // Insert verification code here
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test exception."));
        String currentSchema = extension.getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
        // Insert verification code here
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test exception."));
        String connectionId = extension.getConnectionId(connection);
    }

    @Test
    public void testGetConnectionIdWithoutOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            // Insert verification code for OceanBaseConnection here
        } else {
            assertNotNull(connectionId);
            // Insert verification code for non-OceanBaseConnection here
        }
    }

    @Test
    public void testExtensionPointInitialization() throws Exception {
        ExtensionFactory factory = mock(ExtensionFactory.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.initialize(factory);
        // Insert verification code here
    }
}