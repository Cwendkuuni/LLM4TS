package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Add assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "test_schema";
        // Mock getCurrentSchema method
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Add assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String expectedSchema = "test_schema";
        // Mock queryForObject method
        // when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String expectedConnectionId = "456";
        // Mock queryForObject method
        // when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);        
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}