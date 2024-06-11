package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("deprecation")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obMySQLSessionExtension;
    private PluginWrapper pluginWrapper;

    @Before
    public void setup() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(obMySQLSessionExtension = new OBMySQLSessionExtension());
        when(pluginWrapper.getExtensionContext()).thenReturn(new Object() {});
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenAnswer(i -> "test_schema");
        when(jdbcOperations.execute(anyString())).thenAnswer(i -> {});

        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery_ValidConnectionId() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "connection_id_1");
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_InvalidConnectionId() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "");
    }

    @Test
    public void testSwitchSchema_ValidSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "test_schema");
        assertEquals("test_schema", getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "");
    }

    @Test
    public void testGetCurrentSchema_ValidConnection() throws SQLException {
        assertEquals("test_schema", getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_InvalidConnection() throws SQLException {
        assertEquals(null, getCurrentSchema(mock(Connection.class)));
    }

    @Test
    public void testGetConnectionId_ValidConnection() throws SQLException {
        assertEquals("connection_id", getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_InvalidConnection() throws SQLException {
        assertThrows(Exception.class, () -> getConnectionId(mock(Connection.class)));
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return obMySQLSessionExtension.getCurrentSchema(connection);
    }

    private String getConnectionId(Connection connection) throws SQLException {
        return obMySQLSessionExtension.getConnectionId(connection);
    }
}