package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint extension;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
        connection = DriverManager.getConnection("jdbc:obcd:localhost:28800/testdb", "user", "password");
        extension = new OBMySQLSessionExtension();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        extension.killQuery(connection, connectionId);
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchemaFailed() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "old_schema";
        connection.setCatalog(currentSchema);
        extension.switchSchema(connection, schemaName);
        String newSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaFailed() throws SQLException {
        connection.createStatement().execute("drop database if exists testdb");
        extension.getCurrentSchema(connection);
    }

    @Test
    public void testGetCurrentSchemaSuccess() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("testdb", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertNull(connectionId);
        connection.close();
        connection = DriverManager.getConnection("jdbc:obcd:localhost:28800/testdb", "user", "password");
        connectionId = extension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection obConnection = (OceanBaseConnection) connection;
            assertEquals(obConnection.getServerThreadId() + "", connectionId);
        }
    }
}