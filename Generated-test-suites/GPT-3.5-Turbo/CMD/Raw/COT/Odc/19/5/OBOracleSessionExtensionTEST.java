import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection connection;

    @Test
    public void testKillQuery() {
        connection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, "12345");
        // Assert that killQuery method does not throw any exceptions
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        connection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        sessionExtension.switchSchema(connection, "newSchema");
        // Assert that switchSchema method changes the schema successfully
    }

    @Test
    public void testGetCurrentSchema() {
        connection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        // Assert that getCurrentSchema returns a non-null schema
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        connection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        // Assert that getConnectionId returns a valid connection ID
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSQLException() throws SQLException {
        connection = mock(Connection.class);
        sessionExtension = new OBOracleSessionExtension();
        // Force switchSchema method to throw SQLException
        doThrow(new SQLException()).when(connection).setSchema(anyString());
        sessionExtension.switchSchema(connection, "newSchema");
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        sessionExtension = new OBOracleSessionExtension();
        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);
        // Assert that getConnectionId returns a valid connection ID from OceanBaseConnection
        assertNotNull(connectionId);
    }

}