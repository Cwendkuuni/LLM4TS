
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    
    private OBOracleSessionExtension extension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        extension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1");
        verify(connection, times(1)).execute(anyString());
    }

    @Test
    public void testGetConnectionId() {
        when(connection.queryForObject(anyString(), eq(String.class))).thenReturn("1");
        Assert.assertEquals("1", extension.getConnectionId(connection));
        
        when(connection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        
        Assert.assertEquals("123", extension.getConnectionId(oceanBaseConnection));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.queryForObject(anyString(), eq(String.class))).thenReturn("testSchema");
        extension.switchSchema(connection, "testSchema");
        verify(connection, times(0)).update(anyString());
        
        when(connection.queryForObject(anyString(), eq(String.class))).thenReturn("oldSchema");
        extension.switchSchema(connection, "newSchema");
        verify(connection, times(1)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(connection.queryForObject(anyString(), eq(String.class))).thenReturn("testSchema");
        Assert.assertEquals("testSchema", extension.getCurrentSchema(connection));
    }
}
