package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.util.JdbcOperationsUtil;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    /**
     * The instance of OBMySQLSessionExtension to be tested
     */
    private OBMySQLSessionExtension obMySQLSessionExtension;

    /**
     * Mock JdbcOperationsUtil
     */
    private JdbcOperationsUtil jdbcOperationsUtil;

    /**
     * Mock Connection
     */
    private Connection connection;

    /**
     * Set up before each test method is called
     */
    @Before
    public void setUp() {
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension() {
            {
                this.jdbcOperationsUtil = jdbcOperationsUtil;
            }
        };
        connection = mock(Connection.class);
    }

    /**
     * Tear down after each test method is called
     */
    @After
    public void tearDown() {
        obMySQLSessionExtension = null;
        jdbcOperationsUtil = null;
        connection = null;
    }

    /**
     * Test killQuery method
     * @throws SQLException
     */
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(DriverManager.getConnection.class));
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Add assertions here
    }

    /**
     * Test switchSchema method
     * @throws SQLException
     */
    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = "testSchema";
        String currentSchema = "oldSchema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(DriverManager.getConnection.class));
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, newSchema);

        assertEquals(newSchema, currentSchema);
        // Add assertions here
    }

    /**
     * Test getCurrentSchema method
     */
    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
        // Add assertions here
    }

    /**
     * Test getConnectionId method
     */
    @Test
    public void testGetConnectionId() {
        String connectionId = "12345";
        when(jdbcOperationsUtil.queryForObject(connection, "select connection_id()", String.class)).thenReturn(connectionId);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, actualConnectionId);
        // Add assertions here

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(connection.getCatalog()).thenReturn(null);
        when(connection.getServerThreadId()).thenReturn("67890");

        String actualServerThreadIdConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("67890", actualServerThreadIdConnectionId);
    }
}