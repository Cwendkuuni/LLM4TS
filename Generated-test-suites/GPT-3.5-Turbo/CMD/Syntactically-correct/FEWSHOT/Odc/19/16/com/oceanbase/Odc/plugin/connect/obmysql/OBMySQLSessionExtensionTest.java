package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {
    
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection).setCatalog(schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        assertEquals(expectedSchema, currentSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        String mysqlConnectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(mysqlConnectionId);
        
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        assertEquals(mysqlConnectionId, connectionId);
        
        // Simulating exception for coverage
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(7890L);
        
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        assertEquals("7890", connectionId);
    }
}