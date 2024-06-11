package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        // Add assertions here to verify the method's functionality
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "schemaName";
        String currentSchema = "currentSchema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Add assertions here to verify there is no change
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "newSchemaName";
        String currentSchema = "currentSchema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(connection.setCatalog(schemaName)).thenReturn(true);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Add assertions here to verify the method's functionality
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "newSchemaName";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("currentSchema");
        when(connection.setCatalog(schemaName)).thenThrow(new SQLException());
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // No need to add assertions, the test will automatically verify the exception is thrown
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String queryResult = "currentSchema";
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(queryResult);
        String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, queryResult);
    }

    @Test
    public void testGetConnectionId_Null() throws SQLException {
        Connection connection = mock(Connection.class);
        String queryResult = null;
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenReturn(queryResult);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        // Add assertions here to verify the method's functionality
    }
}