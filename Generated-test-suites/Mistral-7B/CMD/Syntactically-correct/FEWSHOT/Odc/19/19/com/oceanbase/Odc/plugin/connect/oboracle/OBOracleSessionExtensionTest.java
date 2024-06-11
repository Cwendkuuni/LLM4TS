package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {
    private JdbcOperations jdbcOperationsMock;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        jdbcOperationsMock = mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperations(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "12345";
        when(jdbcOperationsMock.execute(Mockito.anyString())).thenReturn(true);

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        assertTrue(true); //TODO: Add assertions based on the expected behavior
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "12345";
        when(jdbcOperationsMock.execute(Mockito.anyString())).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(jdbcOperationsMock.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        //TODO: Add assertions based on the expected behavior
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(schemaName);

        String actualSchemaName = obOracleSessionExtension.getCurrentSchema(connectionMock);

        assertEquals(schemaName, actualSchemaName); //TODO: Add proper assertion statement
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "12345";
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);

        assertEquals(connectionId, actualConnectionId); //TODO: Add proper assertion statement
    }

    @Test
    public void testGetConnectionIdWhenConnectionObjectIsNull() {
        Connection connectionMock = null;
        String connectionId = null;
        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(Optional.ofNullable(null));

        String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);

        assertNull(actualConnectionId); //TODO: Add proper assertion statement
    }

    @Test
    public void testGetConnectionIdWhenJdbcOperationsThrowsException() {
        Connection connectionMock = mock(Connection.class);
        String connectionId = null;

        when(jdbcOperationsMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenThrow(new SQLException("Test Exception"));

        String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);

        assertNull(actualConnectionId); //TODO: Add proper assertion statement
    }
}