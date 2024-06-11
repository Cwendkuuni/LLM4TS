package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperationsMock;
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connectionMock;

    @Before
    public void setup() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        connectionMock = Mockito.mock(Connection.class);
        obOracleSessionExtension.setJdbcOperations(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() throws SQLException {
        // Given
        String connectionId = "123456";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        // Then
        Mockito.verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        obOracleSessionExtension.killQuery(null, "connectionId");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        obOracleSessionExtension.killQuery(connectionMock, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Given
        String newSchema = "testSchema";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obOracleSessionExtension.switchSchema(connectionMock, newSchema);

        // Then
        Mockito.verify(jdbcOperationsMock).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchema));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        obOracleSessionExtension.switchSchema(connectionMock, "");
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String currentSchema = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connectionMock, currentSchema);

        // Then
        Mockito.verifyNoMoreInteractions(jdbcOperationsMock);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Given
        String expectedSchema = "testSchema";
        Mockito.when(jdbcOperationsMock.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(expectedSchema);

        // When
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);

        // Then
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Given
        String expectedConnectionId = "connectionId";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(jdbcOperationsMock.queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn(expectedConnectionId);

        // When
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);

        // Then
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        // Given
        String expectedConnectionId = "1234567890";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connectionMock instanceof OceanBaseConnection)
                .thenReturn(Mockito.mock(OceanBaseConnection.class, Mockito.withSettings().extraInterfaces(OceanBaseConnection.class)));

        // When
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);

        // Then
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}