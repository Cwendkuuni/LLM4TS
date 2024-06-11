package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {
    
    @Mock
    private Connection mockConnection;
    
    private OBOracleSessionExtension obOracleSessionExtension;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(mockConnection, "1");
        Mockito.verify(mockConnection).execute("KILL QUERY " + "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("previousSchema");
        obOracleSessionExtension.switchSchema(mockConnection, "newSchema");
        Mockito.verify(mockConnection).update("ALTER SESSION SET CURRENT_SCHEMA=" + "newSchema");
        assertEquals("newSchema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetCurrentSchema() {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("mySchema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("mySchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(obOracleSessionExtension.getConnectionId(mockConnection)).thenReturn("1");
        String id = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals("1", id);
    }
}