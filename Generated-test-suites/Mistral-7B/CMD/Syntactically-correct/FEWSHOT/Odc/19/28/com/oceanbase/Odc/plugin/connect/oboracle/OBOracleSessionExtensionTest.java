package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

@SuppressWarnings("resultofmethodcallsignature")
public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperationsMock;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperationsUtil(new JdbcOperationsUtil() {
            @Override
            public JdbcOperations getJdbcOperations(Connection connection) {
                return jdbcOperationsMock;
            }
        });
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "testConnectionId";

        when(jdbcOperationsMock.execute(anyString())).thenReturn(true);

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "testConnectionId";

        when(jdbcOperationsMock.execute(anyString())).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchemaName";
        String currentSchema = "testCurrentSchema";

        when(jdbcOperationsMock.update(anyString())).thenReturn(1);
        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        verify(jdbcOperationsMock).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        verify(jdbcOperationsMock, times(2)).queryForObject(anyString(), eq(String.class));

        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaSame() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchemaName";
        String currentSchema = schemaName;

        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        verify(jdbcOperationsMock, never()).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchemaName";

        when(jdbcOperationsMock.update(anyString())).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String queryResult = "testSchemaName";

        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(queryResult);

        assertEquals(queryResult, obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetCurrentSchemaWithNullResult() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(null);

        assertNull(obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "testConnectionId";

        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithNoConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenReturn(null);
        when(connectionMock instanceof OceanBaseConnection).thenReturn(true);

        Optional<String> serverThreadId = Optional.of("testServerThreadId");
        when(connectionMock.getMetaData()).thenReturn(Mockito.spy(new java.sql.DatabaseMetaData() {
            @Override
            public String getServerInfoProductName() {
                return "OceanBase";
            }
        }));
        doReturn(serverThreadId).when(connectionMock).getServerThreadId();

        assertEquals("testServerThreadId", obOracleSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithNoServerThreadId() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        when(connectionMock instanceof OceanBaseConnection).thenReturn(false);

        assertNull(obOracleSessionExtension.getConnectionId(connectionMock));
    }
}