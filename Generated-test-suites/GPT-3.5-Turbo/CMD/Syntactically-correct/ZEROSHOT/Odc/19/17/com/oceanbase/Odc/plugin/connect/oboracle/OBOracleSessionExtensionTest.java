import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        obOracleSessionExtension.killQuery(connection, "12345");
        
        verify(preparedStatement).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        
        verify(connection, times(1)).prepareStatement(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        ResultSet resultSet = mock(ResultSet.class);
        
        when(connection.prepareCall(anyString())).thenReturn(resultSet);
        when(resultSet.getString(1)).thenReturn("schemaName");
        
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        
        assertEquals("schemaName", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        ResultSet resultSet = mock(ResultSet.class);
        
        when(connection.prepareCall(anyString())).thenReturn(resultSet);
        when(resultSet.getString(1)).thenReturn("connectionId");
        
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        
        assertEquals("connectionId", connectionId);
    }
}