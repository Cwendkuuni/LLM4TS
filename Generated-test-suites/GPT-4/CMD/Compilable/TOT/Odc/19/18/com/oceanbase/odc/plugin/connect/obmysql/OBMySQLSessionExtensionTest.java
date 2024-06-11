package com.oceanbase.odc.plugin.connect.obmysql;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.Test;
import org.mockito.Matchers;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

	@Test
    public void testSwitchSchema() throws Exception {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection, never()).setCatalog(Matchers.eq(schemaName));

        when(connection.getCatalog()).thenReturn("anotherSchema");
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection).setCatalog(Matchers.eq(schemaName));
    }


    @Test
    public void testGetCurrentSchema() throws Exception {
        Connection connection = mock(Connection.class);
        
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assert result != null;
    }

    @Test
    public void testGetConnectionId() throws Exception {
        Connection connection = mock(OceanBaseConnection.class);
        
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assert result != null;
        
        when(connection.getClass()).thenReturn((Class)Connection.class);
        result = obMySQLSessionExtension.getConnectionId(connection);
        assert result != null;
    }

    @Test
    public void testKillQuery() throws Exception {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

}