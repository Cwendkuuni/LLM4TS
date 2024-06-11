package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.TestUtil;
import org.pf4j.util.Logging;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtensionTest;
import com.oceanbase.odc.plugin.connect.obmysql.util.JdbcOperationsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class, JdbcOperationsUtil.class})
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        String connectionId = "123456";
        extension.killQuery(connection, connectionId);
        // Add assertions or verification for the method's functionality
    }

    @Test
    public void testSwitchSchema_Success() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SchemaNotFound() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        String schemaName = "non_existent_schema";
        extension.switchSchema(connection, schemaName);
        // No need to add assertions or verification for the exception
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        String currentSchema = extension.getCurrentSchema(connection);
        // Add assertions or verification for the method's functionality
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestOceanBaseConnection();
        String connectionId = extension.getConnectionId(connection);
        // Add assertions or verification for the method's functionality
    }

    @Test
    public void testGetConnectionId_NoQueryForObject() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        String connectionId = extension.getConnectionId(connection);
        if (connectionId == null) {
            // Add assertions or verification for the null case
        } else {
            // Add assertions or verification for the non-null case
        }
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        try {
            extension.getConnectionId(connection);
            fail("An SQLException should have been thrown");
        } catch (SQLException e) {
            // Add assertions or verification for the exception
        }
    }

    @Test
    public void testKillQuery_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = "123456";
        extension.killQuery(null, connectionId);
        // Add assertions or verification for the null connection case
    }

    @Test
    public void testKillQuery_NullConnectionId() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        extension.killQuery(connection, null);
        // Add assertions or verification for the null connectionId case
    }

    @Test
    public void testSwitchSchema_NullSchemaName() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        extension.switchSchema(connection, null);
        // Add assertions or verification for the null schemaName case
    }

    @Test
    public void testSwitchSchema_EmptySchemaName() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = TestUtil.getTestConnection();
        extension.switchSchema(connection, "");
        // Add assertions or verification for the empty schemaName case
    }

    @Test
    public void testGetCurrentSchema_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.getCurrentSchema(null);
        // Add assertions or verification for the null connection case
    }

    @Test
    public void testGetCurrentSchema_EmptyConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.getCurrentSchema(TestUtil.getTestEmptyConnection());
        // Add assertions or verification for the empty connection case
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.getConnectionId(null);
        // Add assertions or verification for the null connection case
    }

    @Test
    public void testGetConnectionId_EmptyConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.getConnectionId(TestUtil.getTestEmptyConnection());
        // Add assertions or verification for the empty connection case
    }

    static {
        MockitoAnnotations.initMocks(OBMySQLSessionExtensionTest.class);
    }
}