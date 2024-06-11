package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Testing class for OBOracleSessionExtension class.
 */
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperationsMock;
    private Connection connectionMock;

    @Before
    public void setup() {
        jdbcOperationsMock = mock(JdbcOperations.class);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connectionMock, "connectionId");
        Mockito.verify(jdbcOperationsMock).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connectionMock, schemaName);
        String currentSchema = getCurrentSchema(connectionMock);
        assertNull(currentSchema);

        when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        extension.switchSchema(connectionMock, schemaName);
        String newSchema = getCurrentSchema(connectionMock);
        assertSame(schemaName, newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "expectedSchema";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedSchema);
        String actualSchema = new OBOracleSessionExtension().getCurrentSchema(connectionMock);
        assertSame(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(false);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException("Unexpected error"));
        String connectionId = new OBOracleSessionExtension().getConnectionId(connectionMock);
        assertNull(connectionId);

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("connectionId");
        connectionMock = Mockito.spy(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);
        connectionId = new OBOracleSessionExtension().getConnectionId(connectionMock);
        assertSame("connectionId", connectionId);
    }

    private String getCurrentSchema(Connection connection) {
        return new OBOracleSessionExtension().getCurrentSchema(connection);
    }
}