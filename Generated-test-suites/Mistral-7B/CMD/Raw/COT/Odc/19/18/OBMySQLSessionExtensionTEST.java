package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

/**
 * Test class for OBMySQLSessionExtension.
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obMySQLSessionExtension;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        jdbcOperations = mock(JdbcOperations.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension() {
            {
                this.jdbcOperations = jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(jdbcOperations.createStatement());
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperations, Mockito.times(1)).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        obMySQLSessionExtension.killQuery(null, "connectionId");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        obMySQLSessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection, Mockito.times(1)).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        obMySQLSessionExtension.switchSchema(null, "testSchema");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        obMySQLSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        Mockito.verify(jdbcOperations, Mockito.times(1)).queryForObject("SELECT DATABASE()", String.class);
        assertEquals(currentSchema, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Mockito.verify(jdbcOperations, Mockito.times(1)).queryForObject("select connection_id()", String.class);
        assertNull(connectionId);

        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        Mockito.verify(connection, Mockito.times(1)).getServerThreadId();
        assertEquals(connectionId, ((OceanBaseConnection) connection).getServerThreadId() + "");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() {
        String connectionId = obMySQLSessionExtension.getConnectionId(null);
    }
}