package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {
    
    OBOracleSessionExtension sessionExtension; 
    
    @Mock
    Connection connection;

    @Mock
    OceanBaseConnection oceanBaseConnection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection, "101");
        verify(connection).execute("KILL QUERY 101");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("testDB");
        sessionExtension.switchSchema(connection, "anotherDB");
        verify(connection).update("ALTER SESSION SET CURRENT_SCHEMA='anotherDB'");
    }
    
    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("testDB");
        sessionExtension.switchSchema(connection, "testDB");
        verify(connection, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("mySchema");
        String schema = sessionExtension.getCurrentSchema(connection);
        assertEquals("mySchema", schema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123");
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }

    @Test
    public void testGetConnectionId_exceptionFlow() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(456L);
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("456", connectionId);
    }
}