import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(PowerMockRunner.class)
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";

        extension.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testKillQuery_Exception() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));

        Assertions.assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        extension.switchSchema(connection, schemaName);

        verify(connection, times(0)).createStatement();
        verify(connection, times(0)).update(anyString());
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String currentSchema = "test_current_schema";
        String schemaName = "test_schema";

        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.update(anyString())).thenReturn(1);

        extension.switchSchema(connection, schemaName);

        verify(connection).createStatement();
        verify(connection).update(containsString("ALTER SESSION SET CURRENT_SCHEMA=test_schema"));
    }

    @Test
    public void testGetCurrentSchema_NullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = null;

        String schemaName = extension.getCurrentSchema(connection);

        Assertions.assertNull(schemaName);
    }

    @Test
    public void testGetCurrentSchema_EmptySchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.queryForObject(querySql, String.class)).thenReturn(null);

        String schemaName = extension.getCurrentSchema(connection);

        Assertions.assertNull(schemaName);
    }

    @Test
    public void testGetCurrentSchema_ValidSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String schemaName = "test_schema";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.queryForObject(querySql, String.class)).thenReturn(schemaName);

        String currentSchema = extension.getCurrentSchema(connection);

        Assertions.assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId_ValidConnection() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";

        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(connectionId.toLong());

        String id = extension.getConnectionId(connection);

        Assertions.assertEquals(connectionId, id);
    }

    @Test
    public void testGetConnectionId_NullConnectionId() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);

        String id = extension.getConnectionId(connection);

        Assertions.assertNull(id);
    }

    @Test
    public void testGetConnectionId_NoOceanBaseConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Connection connection = mock(Connection.class);

        String id = extension.getConnectionId(connection);

        Assertions.assertNull(id);
    }

    @Test
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}