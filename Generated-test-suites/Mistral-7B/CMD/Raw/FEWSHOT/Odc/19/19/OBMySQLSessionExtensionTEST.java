package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY " + connectionId)).thenReturn(true);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, connectionId);

        // No verification needed as method does not have side effects
    }

    @Test(expected = SQLException.class)
    public void testKillQueryException() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY " + connectionId)).thenThrow(new SQLException());

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, connectionId);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        when(jdbcOperationsUtil.execute(connection, "SET SCHEMA " + schemaName)).thenReturn(true);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("currentSchema");
        when(jdbcOperationsUtil.execute(connection, "SET SCHEMA " + schemaName)).thenThrow(new SQLException());

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdNull() {
        Connection connection = mock(Connection.class);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        assertNull(sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdException() {
        Connection connection = mock(Connection.class);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        assertNull(sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetJdbcOperationsUtil() {
        Connection connection = mock(Connection.class);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();

        try (MockedStatic<JdbcOperationsUtil> jdbcOperationsUtilMockedStatic = mockStatic(JdbcOperationsUtil.class)) {
            jdbcOperationsUtilMockedStatic.when(() -> JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);

            assertEquals(jdbcOperationsUtil, sessionExtension.getJdbcOperationsUtil(connection));
        }
    }

    private String getCurrentSchema(Connection connection) {
        try {
            return ((OBMySQLSessionExtension) sessionExtension).getCurrentSchema(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SessionExtensionPoint sessionExtension;

    @BeforeClass
    public void setup() {
        sessionExtension = new OBMySQLSessionExtension();
    }
}