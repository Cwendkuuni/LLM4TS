package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("anotherSchema");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA="
                + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(expectedSchema);

        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1234";

        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn(expectedConnectionId);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
