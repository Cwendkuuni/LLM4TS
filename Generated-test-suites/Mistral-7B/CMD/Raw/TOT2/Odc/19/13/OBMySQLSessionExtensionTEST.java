package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    Connection connection;
    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obMySQLSessionExtension.getJdbcOperations(connection).execute(queryCaptor.capture(queryCaptor.toString()));
        assertEquals("KILL QUERY 12345", queryCaptor.getValue());
    }

    // Test killQuery() with a null connection, an empty connectionId, and an exception here

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        ArgumentCaptor<String> newSchemaCaptor = ArgumentCaptor.forClass(String.class);
        when(connection.setCatalog(newSchemaCaptor.capture(newSchemaCaptor.toString()))).thenReturn(true);
        assertEquals(schemaName, newSchemaCaptor.getValue());
    }

    // Test switchSchema() with a null connection, an empty schemaName, and an exception here

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(null, schemaName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaWithEmptySchemaName() {
        Connection connection = mock(Connection.class);
        obMySQLSessionExtension.switchSchema(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchemaName() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "testSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    // Test getCurrentSchema() method here

    // Test getCurrentSchema() with a null connection and an exception here

    // Test getConnectionId() method here

    // Test getConnectionId() with a null connection and an exception here

    // Test NULL pointer exception when a method is called on a null connection

    // Test exceptions thrown by JdbcOperationsUtil.getJdbcOperations(connection).execute() method here
}