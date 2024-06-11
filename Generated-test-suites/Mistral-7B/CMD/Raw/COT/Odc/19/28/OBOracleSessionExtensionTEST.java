package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtensionFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 *
 * @author yaobin
 * @since 4.2.0
 */
@SuppressWarnings("static-access")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:oceanbase:obcds:localhost:10015/oceanbase:1521:orcl", "user", "password");
        obOracleSessionExtension = new DefaultExtensionFactory().getExtensions(SessionExtensionPoint.class).get(0);
    }

    // Test killQuery method
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        String connectionId = null;
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId + "X");
    }

    // Test switchSchema method
    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = "test_schema";
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(newSchema, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchemaName() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String schemaName = "test_schemaX";
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    // Test getCurrentSchema method
    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() throws SQLException {
        obOracleSessionExtension.getCurrentSchema(null);
    }

    // Test getConnectionId method
    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() throws SQLException {
        obOracleSessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        obOracleSessionExtension.getConnectionId(new OceanBaseConnection(connection.createStatement().getConnection()));
    }

    @Test
    public void testGetConnectionIdWithConnectionIdReturned() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        String connectionIdFromDB = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, connectionIdFromDB);
    }
}