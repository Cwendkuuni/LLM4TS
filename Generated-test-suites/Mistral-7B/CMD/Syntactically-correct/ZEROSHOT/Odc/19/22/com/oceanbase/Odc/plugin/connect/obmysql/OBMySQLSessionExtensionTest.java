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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection( "jdbc:oceansbase:localhost:10000:testdb", "username", "password" );
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperations.execute(connection, "KILL QUERY 123")).thenReturn(1);

        obMySQLSessionExtension.killQuery(connection, "123");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        when(jdbcOperations.execute(connection, "KILL QUERY 123")).thenThrow(new SQLException("Error killing query"));

        obMySQLSessionExtension.killQuery(connection, "123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "test_schema";
        String newSchema = "new_schema";
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.execute(connection, "SET SCHEMA 'new_schema'")).thenReturn(0);
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(newSchema);

        obMySQLSessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String currentSchema = "test_schema";
        String newSchema = "new_schema";
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.execute(connection, "SET SCHEMA 'new_schema'")).thenThrow(new SQLException("Error switching schema"));

        obMySQLSessionExtension.switchSchema(connection, newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);

        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "1234";
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithoutQuery() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenThrow(new SQLException("Error getting connection id"));

        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }
}