package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseDriver;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension extension;

    @Before
    public void setUp() throws Exception {
        OceanBaseDriver driver = new OceanBaseDriver();
        connection = DriverManager.getConnection("jdbc:obmysql://localhost:27017/testdb", "root", "password");
        extension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        extension.killQuery(connection, connectionId);
        // This test case does not have an assertion as it's just a command
    }

    @Test
    public void testSwitchSchema_Valid() throws SQLException {
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Invalid() throws SQLException {
        String schemaName = null;
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId_JdbcOperations() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
        String connectionId = extension.getConnectionId(oceanBaseConnection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionId_Exception() throws SQLException {
        String querySql = "select connection_id()";
        assertThrows(SQLException.class, () -> JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class));
    }
}