package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.jdbc.common.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private SessionExtensionPoint obMySQLSessionExtension;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
        connection = DriverManager.getConnection("jdbc:obnew:odps://127.0.0.1:25010/root.odps", "root", "root");
        jdbcOperations = new JdbcOperations();
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);

        connection.setAutoCommit(false);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        assertEquals("KILL QUERY test_connection_id", JdbcOperationsUtil.getJdbcOperations(connection).getLastExecutedStatement());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        connection.setCatalog(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = schemaName;

        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "current_schema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "test_connection_id";
        when(connection.isClosed()).thenReturn(false);
        when(connection.getCatalog()).thenReturn(null);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));

        connection.close();
        assertEquals(null, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdForOceanBaseConnection() throws SQLException {
        String connectionId = "test_connection_id";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        connection = (Connection) oceanBaseConnection;

        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }
}