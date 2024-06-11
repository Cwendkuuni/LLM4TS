package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("resultofmethodcallsused")
public class OBOracleSessionExtensionTest {

    @InjectMocks
    private OBOracleSessionExtension objUnderTest;

    private JdbcOperations jdbcOperationsMock;
    private Connection connectionMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        jdbcOperationsMock = mock(JdbcOperations.class);
        connectionMock = mock(Connection.class);
        when(objUnderTest.getJdbcOperations(connectionMock)).thenReturn(jdbcOperationsMock);
    }

    @Test
    public void testKillQuery() {
        try {
            objUnderTest.killQuery(connectionMock, "connectionId");
            assertEquals("KILL QUERY connectionId", JdbcOperationsUtil.getJdbcOperations(connectionMock).execute("KILL QUERY connectionId"));
        } catch (SQLException e) {
            // expected since test is using mock
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(objUnderTest.getCurrentSchema(connectionMock)).thenReturn(currentSchema);
        when(jdbcOperationsMock.update(StringUtils.anyString())).thenReturn(1);

        objUnderTest.switchSchema(connectionMock, schemaName);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        assertEquals(1, JdbcOperationsUtil.getJdbcOperations(connectionMock).update(alterSql));

        assertNull(objUnderTest.getCurrentSchema(connectionMock));
        assertEquals(schemaName, objUnderTest.getCurrentSchema(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(objUnderTest.getCurrentSchema(connectionMock)).thenReturn(schemaName);

        objUnderTest.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsMock.queryForObject(StringUtils.anyString(), String.class)).thenReturn(schemaName);

        assertEquals(schemaName, objUnderTest.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "connectionId";
        when(jdbcOperationsMock.queryForObject(StringUtils.anyString(), String.class)).thenReturn(connectionId);

        assertEquals(connectionId, objUnderTest.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithNullConnectionId() {
        Optional<String> connectionIdOptional = Optional.empty();
        when(jdbcOperationsMock.queryForObject(StringUtils.anyString(), String.class)).thenReturn(null);

        when(connectionMock instanceof OceanBaseConnection).thenReturn(true);

        assertEquals("1", objUnderTest.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnectionNull() {
        when(connectionMock instanceof OceanBaseConnection).thenReturn(false);

        assertNull(objUnderTest.getConnectionId(connectionMock));
    }
}