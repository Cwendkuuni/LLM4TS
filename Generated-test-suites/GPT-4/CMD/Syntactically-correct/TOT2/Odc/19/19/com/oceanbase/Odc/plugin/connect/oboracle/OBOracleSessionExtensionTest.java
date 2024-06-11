package com.oceanbase.odc.plugin.connect.oboracle;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension OBOracleSessionExtensionInstance;
    private Connection connection;

    @Before
    public void setUp() {
        OBOracleSessionExtensionInstance = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    //Verify if killQuery function works
    @Test
    public void testKillQuery() {
        try {
            String connectionId = "testConnectionId";
            OBOracleSessionExtensionInstance.killQuery(connection, connectionId);
            verify(connection, times(1)).createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        OBOracleSessionExtensionInstance.switchSchema(connection, schemaName);
        verify(connection, times(1)).createStatement();
    }

    //Normal case
    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtensionInstance.getCurrentSchema(connection);
        verify(connection, times(1)).createStatement();
    }
    
    //Edge-case which validates that an exception is thrown
    @Test(expected = SQLException.class)
    public void testGetCurrentSchema_ThrowsException() {
        OBOracleSessionExtensionInstance.getCurrentSchema(null);
    }

     //Normal case
    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtensionInstance.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
    }

    //Edge-case which validates that an exception is thrown
    @Test(expected = SQLException.class)
    public void testGetConnectionId_ThrowsException() {
        OBOracleSessionExtensionInstance.getConnectionId(null);
    }
}