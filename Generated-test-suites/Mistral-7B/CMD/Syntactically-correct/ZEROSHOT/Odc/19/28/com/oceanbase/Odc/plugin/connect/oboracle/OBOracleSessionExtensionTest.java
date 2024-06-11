package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension
 */
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connectionMock;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setUp() {
        jdbcOperationsMock = mock(JdbcOperations.class);
        connectionMock = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperationsMock.execute(Mockito.anyString())).thenReturn(true);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenAnswer(invocation -> invocation.getArguments()[0].toString());
        when(connectionMock.createStatement()).thenReturn(jdbcOperationsMock);
        when(connectionMock.isClosed()).thenReturn(false);
        obOracleSessionExtension.setJdbcOperationsUtil(new JdbcOperationsUtil() {
            @Override
            public JdbcOperations getJdbcOperations(Connection connection) {
                return jdbcOperationsMock;
            }
        });
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connectionMock, connectionId);
        verifyKillQueryCall(connectionId);
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        obOracleSessionExtension.killQuery(connectionMock, null);
        // No exception is thrown since the method is void
    }

    @Test
    public void testKillQueryWithClosedConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        obOracleSessionExtension.killQuery(connectionMock, "123");
        // No exception is thrown
    }

    private void verifyKillQueryCall(String connectionId) {
        String expectedKillQuerySql = "KILL QUERY " + connectionId;
        Mockito.verify(jdbcOperationsMock).execute(expectedKillQuerySql);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
        String expectedSwitchSchemaSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        Mockito.verify(jdbcOperationsMock).update(expectedSwitchSchemaSql);
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaSameAsTargetSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = schemaName;
        when(getProperty(connectionMock, "CURRENT_SCHEMA")).thenReturn(Optional.of(currentSchema));
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
        // No SQL statement is executed
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String schemaName = "testSchema";
        when(getProperty(connectionMock, "CURRENT_SCHEMA")).thenReturn(Optional.empty());
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
        // SQLException is thrown since CURRENT_SCHEMA property does not exist in the connection
    }

    @Test
    public void testSwitchSchemaWithClosedConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        obOracleSessionExtension.switchSchema(connectionMock, "testSchema");
        // No exception is thrown
    }

    private Optional<String> getProperty(Connection connection, String propertyName) throws SQLException {
        String querySql = "SELECT SYS_CONTEXT('userenv', '" + propertyName + "') FROM DUAL";
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class) != null
            ? Optional.of(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
            : Optional.empty();
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(getProperty(connectionMock, "CURRENT_SCHEMA")).thenReturn(Optional.of(currentSchema));
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testgetCurrentSchemaWithNullCurrentSchema() throws SQLException {
        when(getProperty(connectionMock, "CURRENT_SCHEMA")).thenReturn(Optional.empty());
        assertEquals(null, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testgetCurrentSchemaWithClosedConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        assertEquals(null, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testgetConnectionId() throws SQLException {
        String connectionId = "123";
        when(getProperty(connectionMock, "sessionid")).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testgetConnectionIdWithNullConnectionId() throws SQLException {
        when(getProperty(connectionMock, "sessionid")).thenReturn(null);
        assertEquals(null, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testgetConnectionIdWithClosedConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        assertEquals(null, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testgetConnectionIdWithOceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn("123");
        when(connectionMock.isInstanceOf(OceanBaseConnection.class)).thenReturn(true);
        when(connectionMock.cast(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        assertEquals("123", obOracleSessionExtension.getConnectionId(connectionMock));
    }
}