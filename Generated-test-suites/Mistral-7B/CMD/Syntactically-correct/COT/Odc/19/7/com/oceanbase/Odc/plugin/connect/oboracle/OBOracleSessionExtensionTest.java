package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.CurrentSchemaGetter;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.KillQueryExecutor;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SwitchSchemaExecutor;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    private Connection connection;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        connection = Mockito.mock(Connection.class);
        Mockito.when(connection.createStatement()).thenReturn(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryNullConnection() {
        obOracleSessionExtension.killQuery(null, "connectionId");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnectionId() {
        obOracleSessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = schemaName;
        Mockito.when(getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(jdbcOperationsMock, Mockito.never()).update(Mockito.anyString());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaNullConnection() {
        obOracleSessionExtension.switchSchema(null, "schemaName");
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullSchemaName() {
        obOracleSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "anotherSchema";
        Mockito.when(getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        Mockito.verify(jdbcOperationsMock, Mockito.times(1)).update(alterSql);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaNullConnection() {
        obOracleSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(getCurrentSchema(connection)).thenReturn(schemaName);
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        obOracleSessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "1234";
        Mockito.when(getConnectionId(connection)).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    private String getCurrentSchema(Connection connection) {
        return obOracleSessionExtension.getCurrentSchema(connection);
    }

    private String getConnectionId(Connection connection) {
        return obOracleSessionExtension.getConnectionId(connection);
    }
}