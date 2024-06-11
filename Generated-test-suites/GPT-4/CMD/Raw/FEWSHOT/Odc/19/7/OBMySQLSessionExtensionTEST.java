package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension oBMySQLSessionExtension;

    @Mock
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        oBMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        oBMySQLSessionExtension.killQuery(connection, connectionId);

        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        oBMySQLSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedResult = "expectedResult";
        when(oBMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(expectedResult);

        String result = oBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedResult = "expectedResult";
        when(oBMySQLSessionExtension.getConnectionId(connection)).thenReturn(expectedResult);

        String result = oBMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        oceanBaseConnection = mock(OceanBaseConnection.class);
        String expectedResult = "expectedResult";
        when(oBMySQLSessionExtension.getConnectionId(oceanBaseConnection)).thenReturn(expectedResult);

        String result = oBMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedResult, result);
    }
}