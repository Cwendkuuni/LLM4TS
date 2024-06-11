import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.TestExtension;
import org.pf4j.TestTools;
import org.pf4j.util.Extensions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

@ExtendWith(TestExtension.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connectionMock;

    @BeforeEach
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connectionMock, "connectionId");
        verify(connectionMock, times(1)).createStatement();
        Statement statement = mock(Statement.class);
        when(connectionMock.createStatement()).thenReturn(statement);
        verify(statement, times(1)).executeUpdate("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsSameAsGivenSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connectionMock, "schemaName");
        verify(connectionMock, never()).createStatement();
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsNotSameAsGivenSchema() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(mock(Statement.class));
        when(connectionMock.getAutoCommit()).thenReturn(false);

        obOracleSessionExtension.switchSchema(connectionMock, "schemaName");

        String currentSchema = "currentSchema";
        String schemaName = "schemaName";
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);

        verify(connectionMock, times(1)).createStatement();
        Statement statement = mock(Statement.class);
        when(connectionMock.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(1);
        verify(statement, times(1)).executeUpdate(alterSql);
        verify(connectionMock, times(1)).commit();

        Assertions.assertEquals("schemaName", obOracleSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetCurrentSchemaWhenConnectionIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNotNull() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsOceanBaseConnection() throws SQLException {
        when(connectionMock.createStatement()).thenThrow(new SQLException("mock exception"));
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        Assertions.assertEquals(((OceanBaseConnection) connectionMock).getServerThreadId(), connectionId);
    }

    @Test
    public void testGetConnectionIdWhenConnectionQueryFailed() throws SQLException {
        when(connectionMock.createStatement()).thenThrow(new SQLException("mock exception"));
        Assertions.assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connectionMock));
    }
}