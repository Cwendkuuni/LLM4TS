package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
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

/**
 * Test class for OBMySQLSessionExtension.
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension() {
            {
                this.jdbcOperations = jdbcOperations;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(true);
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        Mockito.verify(jdbcOperations).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        assertEquals(schemaName, JdbcOperationsUtil.getDefaultSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("testSchema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        when(connection.getCatalog()).thenReturn(connectionId);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNull() {
        String result = obMySQLSessionExtension.getConnectionId(null);
        assertEquals(null, result);
    }

    @Test
    public void testGetConnectionIdWhenJdbcOperationsExecuteThrowsException() throws SQLException {
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException("Test Exception"));
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(null, result);
    }

    @Test
    public void testGetConnectionIdWhenOceanBaseConnection() throws SQLException {
        String serverThreadId = "testServerThreadId";
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.asWrapper(OceanBaseConnection.class)).thenReturn(Mockito.mock(OceanBaseConnection.class));
        when(Mockito.mock(OceanBaseConnection.class).getServerThreadId()).thenReturn(serverThreadId);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(serverThreadId, result);
    }
}