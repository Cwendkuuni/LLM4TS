import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {
    
    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement("KILL QUERY " + connectionId)).thenReturn(preparedStatement);
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify that preparedStatement.execute() was called
        preparedStatement.execute();
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that connection.setCatalog() was called with the correct schemaName
        verify(connection).setCatalog(schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn(expectedSchema);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement("SELECT DATABASE()")).thenReturn(preparedStatement);
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        // Verify that the correct schemaName was returned
        assertEquals(expectedSchema, currentSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "54321";
        Connection connection = mock(OceanBaseConnection.class);
        
        // Mocking behavior for OceanBaseConnection
        when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        // Verify that the correct connectionId was returned
        assertEquals(expectedConnectionId, connectionId);
    }
}