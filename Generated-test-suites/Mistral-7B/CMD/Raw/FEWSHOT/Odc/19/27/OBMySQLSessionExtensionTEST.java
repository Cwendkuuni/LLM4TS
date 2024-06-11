package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test cases for OBMySQLSessionExtension class.
 */
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        jdbcOperationsMock = mock(JdbcOperations.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperations(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";

        when(jdbcOperationsMock.execute(eq("KILL QUERY " + connectionId), anyString())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // no need to check the result here
                return null;
            }
        });

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";

        when(jdbcOperationsMock.execute(eq("KILL QUERY " + connectionId), anyString()))
                .thenThrow(new SQLException("Test exception"));

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        String currentSchema = "currentSchema";
        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);

        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenThrow(new SQLException("Test exception"));

        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String currentSchema = "currentSchema";

        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";

        when(jdbcOperationsMock.queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OceanBaseConnection oceanBaseConnectionMock = mock(OceanBaseConnection.class);
        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn("serverThreadId");

        Connection connectionMock = mock(Connection.class);
        when(connectionMock.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnectionMock);

        assertEquals("serverThreadId", obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithNullConnectionId() {
        Connection connectionMock = mock(Connection.class);

        assertNull(obMySQLSessionExtension.getConnectionId(connectionMock));
    }
}