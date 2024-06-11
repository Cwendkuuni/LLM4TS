package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
    Connection mock_connection = mock(Connection.class);

    // Test case 1 for method killQuery
    @Test
    public void testKillQuery() {
        String connectionId = "123";
        sessionExtension.killQuery(mock_connection, connectionId);
    }

    // Test case 2 for method switchSchema
    @Test(expected = SQLException.class)
    public void testSwitchSchema() throws SQLException {
        String schemaName = "Employee";
        sessionExtension.switchSchema(mock_connection, schemaName);
    }

    // Test case 3 for method getCurrentSchema
    @Test
    public void testGetCurrentSchema() {
        String schema = sessionExtension.getCurrentSchema(mock_connection);
        assert (schema != null);
    }

    // Test case 4 for method getConnectionId
    @Test
    public void testGetConnectionId() {
        String connectionId = sessionExtension.getConnectionId(mock_connection);
        assert (connectionId != null);
    }
}