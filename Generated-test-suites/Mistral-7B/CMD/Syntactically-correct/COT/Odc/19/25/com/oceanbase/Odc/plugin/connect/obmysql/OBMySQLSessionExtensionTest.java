package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension.getCurrentSchema;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension.killQuery;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension.switchSchema;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension.switchSchemaException;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension.getConnectionId;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OceanBaseConnection;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension extension;

    @Before
    public void setup() throws SQLException {
        connection = JdbcOperationsUtil.getTestConnection();
        extension = new OBMySQLSessionExtension();
        extension.init(connection);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = extension.getConnectionId(connection);
        assertEquals("Expected a valid connection Id", connectionId, getConnectionId(connection));
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = extension.getCurrentSchema(connection);
        String expectedSchema = "test_schema"; // replace with the actual schema name
        assertEquals("Expected current schema to be " + expectedSchema, expectedSchema, currentSchema);
    }

    @Test
    public void testKillQuery() {
        String queryId = "1234";
        extension.killQuery(connection, queryId);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_NoSuchQuery() {
        String queryId = "non_existent_query_id";
        extension.killQuery(connection, queryId);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_InvalidConnection() {
        Connection invalidConnection = null;
        assertThrows("Expected SQLException", SQLException.class, () -> extension.killQuery(invalidConnection, "queryId"));
    }

    @Test
    public void testSwitchSchema() {
        String newSchema = "test_schema";
        extension.switchSchema(connection, newSchema);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("Expected schema to be switched to " + newSchema, newSchema, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidConnection() {
        Connection invalidConnection = null;
        assertThrows("Expected SQLException", SQLException.class, () -> extension.switchSchema(invalidConnection, "test_schema"));
    }

    @Test(expected = switchSchemaException.class)
    public void testSwitchSchema_SchemaAlreadyActive() {
        String newSchema = getCurrentSchema(connection);
        extension.switchSchema(connection, newSchema);
    }
}

class switchSchemaException extends SQLException {
    public switchSchemaException() {
        super("Cannot switch to the same schema");
    }
}