package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
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

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private Connection connection;

    private String schemaName;
    private String currentSchema;
    private String querySql;
    private String expectedSchema;
    private String connectionId;

    @Before
    public void setUp() throws SQLException {
        schemaName = "test_schema";
        currentSchema = "old_schema";
        expectedSchema = schemaName;
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        connection = mock(Connection.class);
    }

    @After
    public void tearDown() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = null;
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(1);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verifyKillQueryCall();
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verifyKillQueryCall();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        querySql = "SELECT DATABASE()";
        when(jdbcOperations.queryForObject(connection, querySql, String.class)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(expectedSchema, currentSchema);
        verifySchemaSwitchCall();
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        when(jdbcOperations.queryForObject(connection, querySql, String.class)).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verifySchemaSwitchCall();
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        querySql = "SELECT DATABASE()";
        when(jdbcOperations.queryForObject(connection, querySql, String.class)).thenReturn(expectedSchema);
        assertEquals(expectedSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong("123"));
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }

    @Test
    public void testGetConnectionIdWithoutOceanBaseConnection() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        querySql = "select connection_id()";
        when(jdbcOperations.queryForObject(connection, querySql, String.class)).thenReturn("TestConnectionId");
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("TestConnectionId", connectionId);
    }

    private void verifyKillQueryCall() {
        // No verification needed for this test as we are only interested in method execution.
    }

    private void verifySchemaSwitchCall() throws SQLException {
        when(connection.setCatalog(schemaName)).thenReturn(true);
        verify(jdbcOperations).execute(connection, "KILL QUERY " + connectionId);
        verify(jdbcOperations).queryForObject(connection, querySql, String.class);
    }
}