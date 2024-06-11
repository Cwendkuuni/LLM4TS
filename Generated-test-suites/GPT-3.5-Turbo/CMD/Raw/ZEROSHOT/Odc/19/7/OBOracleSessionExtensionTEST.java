import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");

        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(connection.prepareStatement(anyString()).executeQuery()).thenReturn(mock(ResultSet.class));
        
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
    }
}