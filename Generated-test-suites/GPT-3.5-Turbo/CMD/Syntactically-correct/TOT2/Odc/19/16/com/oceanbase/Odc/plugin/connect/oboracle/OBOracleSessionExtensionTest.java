import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        obOracleSessionExtension.killQuery(connection, connectionId);
        
        verify(connection).prepareStatement("KILL QUERY " + connectionId);
        verifyNoMoreInteractions(connection);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("other");
        
        obOracleSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='test'");
        verifyNoMoreInteractions(connection);
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("testSchema");
        
        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        
        assertEquals("123", obOracleSessionExtension.getConnectionId(connection));
        
        verifyNoMoreInteractions(connection);
    }
}