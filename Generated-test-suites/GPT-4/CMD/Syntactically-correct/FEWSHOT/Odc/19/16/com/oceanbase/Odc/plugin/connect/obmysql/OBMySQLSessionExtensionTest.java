package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBMySQLSessionExtensionTest {
    @Mock
    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "1");
        // RabbitMQ messages or logs can be verified to check if the query has been killed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(connection, "schema2");
        assertEquals("schema2", connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test_schema";
        obMySQLSessionExtension.getCurrentSchema(connection);
        // verify if query was executed
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1";
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}