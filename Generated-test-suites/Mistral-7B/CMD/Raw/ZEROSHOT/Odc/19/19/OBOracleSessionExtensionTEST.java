package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtilMock;

/**
 * Unit tests for {@link OBOracleSessionExtension}.
 *
 * @author yaobin
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint extension;

    @Before
    public void setup() throws SQLException {
        JdbcOperationsUtilMock jdbcOperationsUtilMock = new JdbcOperationsUtilMock();
        JdbcOperations jdbcOperations = jdbcOperationsUtilMock;
        connection = mock(Connection.class);
        extension = new OBOracleSessionExtension() {
            {
                setJdbcOperations(jdbcOperationsUtilMock);
            }
        };
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        extension.killQuery(connection, connectionId);
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        verify(connection).createStatement();
        verify(connection).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(Mockito.anyString()))
                .thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().getString(Mockito.anyInt()))
                .thenReturn(currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "12345";
        extension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            verify(connection).getServerThreadId();
            verify(connection).getServerThreadId().toString();
            assertEquals(connectionId, extension.getConnectionId(connection));
        } else {
            verify(connection).createStatement();
            verify(connection.createStatement()).execute(Mockito.anyString());
            verify(connection.createStatement().getResultSet()).next();
            verify(connection.createStatement().getResultSet()).getString(Mockito.anyInt());
            assertEquals(connectionId, extension.getConnectionId(connection));
        }
    }

    @After
    public void cleanUp() {
        extension = null;
        connection = null;
    }
}

class JdbcOperationsUtilMock {

    @Override
    public JdbcOperations getJdbcOperations(Connection connection) {
        return Mockito.mock(JdbcOperations.class);
    }
}