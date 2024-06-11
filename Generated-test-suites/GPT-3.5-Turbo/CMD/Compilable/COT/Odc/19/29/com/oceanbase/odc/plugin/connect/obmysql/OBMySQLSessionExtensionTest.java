package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        extension.killQuery(connection, "123");
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        extension.switchSchema(connection, "test_schema");
        
        verify(connection).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        String currentSchema = extension.getCurrentSchema(connection);
        
        assertEquals("SELECT DATABASE()", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        String connectionId = extension.getConnectionId(connection);
        
        assertEquals("select connection_id()", connectionId);
    }
    
    @Test
    public void testGetConnectionIdException() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        // Trigger exception
        extension.getConnectionId(connection);
        
        verify(((OceanBaseConnection) connection)).getServerThreadId();
    }

}