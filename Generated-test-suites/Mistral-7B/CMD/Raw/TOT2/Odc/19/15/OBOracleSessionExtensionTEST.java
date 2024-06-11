import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtensionTest;
import com.oceanbase.odc.plugin.connect.oboracle.testutil.TestConnectionFactory;

public class OBOracleSessionExtensionTest {

    private static Connection getMockConnection() {
        Connection mockConnection = Mockito.mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(false);
        return mockConnection;
    }

    @Test
    public void killQueryTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();
        String connectionId = "testConnectionId";

        // Act
        obj.killQuery(connection, connectionId);

        // Assert
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void switchSchemaTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();
        String schemaName = "testSchema";

        // Act and Assert
        try {
            obj.switchSchema(connection, schemaName);
            assertEquals(schemaName, getCurrentSchema(connection));
        } catch (SQLException e) {
            // In case of schema not found exception
        }
    }

    @Test
    public void getCurrentSchemaTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();

        // Act
        String currentSchema = obj.getCurrentSchema(connection);

        // Assert
        assertNotNull(currentSchema);
        assertEquals("currentSchema", currentSchema);
    }

    @Test
    public void getConnectionIdTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();

        // Act
        String connectionId = obj.getConnectionId(connection);

        // Assert
        assertNotNull(connectionId);
        assertEquals("connectionId", connectionId);
    }

    @Test
    public void killQueryEdgeCasesTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();
        String connectionId = null;
        Objects.requireNonNull(connection);

        // Act and Assert
        obj.killQuery(connection, connectionId);
        verify(connection, never()).createStatement();
        verify(connection, never()).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void switchSchemaEdgeCasesTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = getMockConnection();
        String schemaName = null;
        Objects.requireNonNull(connection);

        // Act and Assert
        try {
            obj.switchSchema(connection, schemaName);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected SQLException
        }
    }

    @Test
    public void getCurrentSchemaEdgeCasesTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = Mockito.spy(getMockConnection());
        doThrow(new SQLException("Error when querying the current schema")).when(connection).createStatement();

        // Act and Assert
        try {
            obj.getCurrentSchema(connection);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected SQLException
        }
    }

    @Test
    public void getConnectionIdEdgeCasesTest() {
        // Arrange
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        doReturn(null).when(connection).isClosed();
        doReturn(null).when(connection).getAutoCommit();
        doReturn(null).when(connection).getClientInfo("ob.version.server");

        // Act and Assert
        String connectionId = obj.getConnectionId(connection);
        assertNull(connectionId);
    }

    private String getCurrentSchema(Connection connection) {
        OBOracleSessionExtension obj = new OBOracleSessionExtension();
        return obj.getCurrentSchema(connection);
    }
}