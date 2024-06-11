package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.CurrentSchemaGetter;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.KillQueryExecutor;

public class OBMySQLSessionExtensionTest {

    private Connection mockConnection;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private JdbcOperations jdbcOperationsMock;
    private CurrentSchemaGetter currentSchemaGetterMock;
    private KillQueryExecutor killQueryExecutorMock;

    @Before
    public void setup() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        currentSchemaGetterMock = new CurrentSchemaGetter();
        killQueryExecutorMock = new KillQueryExecutor();
        obMySQLSessionExtension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperationsMock;
            }

            @Override
            CurrentSchemaGetter getCurrentSchemaGetter() {
                return currentSchemaGetterMock;
            }

            @Override
            KillQueryExecutor getKillQueryExecutor() {
                return killQueryExecutorMock;
            }
        };
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(jdbcOperationsMock.execute(Mockito.eq("KILL QUERY " + connectionId))).thenReturn(null);
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        Mockito.verify(jdbcOperationsMock).execute(Mockito.eq("KILL QUERY " + connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(jdbcOperationsMock.execute(Mockito.eq("KILL QUERY " + connectionId))).thenThrow(new SQLException("Test exception"));
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchemaName";
        String currentSchema = "currentSchema";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.eq("SELECT DATABASE()"), Mockito.eq(String.class))).thenReturn(currentSchema);
        Mockito.when(jdbcOperationsMock.setCatalog(Mockito.eq(schemaName))).thenReturn(null);

        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(mockConnection).getCurrentCatalog());
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchemaName";
        String currentSchema = "testSchemaName";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.eq("SELECT DATABASE()"), Mockito.eq(String.class))).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        assertNull(JdbcOperationsUtil.getJdbcOperations(mockConnection).getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchemaName";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.eq("SELECT DATABASE()"), Mockito.eq(String.class))).thenReturn(currentSchema);

        String result = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.eq("select connection_id()"), Mockito.eq(String.class))).thenReturn(connectionId);

        String result = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdNoQuery() throws SQLException {
        Mockito.when(jdbcOperationsMock.queryForObject(Mockito.eq("select connection_id()"), Mockito.eq(String.class))).thenThrow(new SQLException("Query failed"));
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(mockConnection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn("testThreadId");

        String result = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("testThreadId", result);
    }
}