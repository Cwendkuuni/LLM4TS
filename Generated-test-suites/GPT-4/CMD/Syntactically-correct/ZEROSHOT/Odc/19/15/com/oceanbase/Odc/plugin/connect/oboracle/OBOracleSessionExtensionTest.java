
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @After
    public void tearDown() {
        this.obOracleSessionExtension = null;
    }

    @Test
    public void testKillQuery() {
        String connectionId = "connection1234";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaIfNotEqual() throws SQLException {
        String currentSchema = "currentSchema";
        String newSchema = "newSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(mockConnection, newSchema);

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + newSchema;
        verify(mockConnection, times(1)).update(alterSql);
    }

    @Test
    public void testSwitchSchemaIfEqual() throws SQLException {
        String currentSchema = "currentSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(mockConnection, currentSchema);

        verifyNoMoreInteractions(mockConnection);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(expectedSchema);

        String actualSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);

        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionIdSuccess() {
        String expectedConnectionId = "connection123";
        when(obOracleSessionExtension.getConnectionId(mockConnection)).thenReturn(expectedConnectionId);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(mockConnection);

        Assert.assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdFailure() {
        when(obOracleSessionExtension.getConnectionId(mockConnection)).thenReturn(null);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(mockConnection);

        Assert.assertNull(actualConnectionId);
    }
}
