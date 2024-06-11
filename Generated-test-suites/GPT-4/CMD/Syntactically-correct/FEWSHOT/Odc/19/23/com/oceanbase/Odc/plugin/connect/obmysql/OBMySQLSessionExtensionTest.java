
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {
  
    @Test
    public void testKillQuery() {
        Connection connectionMock = Mockito.mock(Connection.class);
        String query = "KILL QUERY 123";
        
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connectionMock, "123");

        Mockito.verify(connectionMock).execute(query);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(connectionMock.getCatalog()).thenReturn("original_schema");
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connectionMock, "new_schema");

        Mockito.verify(connectionMock).setCatalog("new_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connectionMock = Mockito.mock(Connection.class);
        String querySql = "SELECT DATABASE()";
        Mockito.when(connectionMock.queryForObject(querySql, String.class)).thenReturn("my_database");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connectionMock);

        assertEquals("my_database", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        OceanBaseConnection obConnectionMock = Mockito.mock(OceanBaseConnection.class);
        
        String querySql = "select connection_id()";
        Mockito.when(connectionMock.queryForObject(querySql, String.class)).thenReturn("111");
        Mockito.when(obConnectionMock.getServerThreadId()).thenReturn(222L);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        String connectionId = extension.getConnectionId(connectionMock);
        assertEquals("111", connectionId);
        
        connectionId = extension.getConnectionId(obConnectionMock);
        assertEquals("222", connectionId);
    }
}
