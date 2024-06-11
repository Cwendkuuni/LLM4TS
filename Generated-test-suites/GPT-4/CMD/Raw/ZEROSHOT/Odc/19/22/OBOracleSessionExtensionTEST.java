
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection connection;
    private String connectionId;
    private String schemaName;

    @Before
    public void setup() {
        extension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        connectionId = "testConnectionId";
        schemaName = "testSchemaName";
    }

    @Test(expected = SQLException.class)
    public void testKillQuery() {
        doThrow(SQLException.class).when(connection).execute("KILL QUERY " + connectionId);
        extension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
              
        if (!Objects.equals(currentSchema, schemaName)) {
            doThrow(SQLException.class).when(connection).update(alterSql);
            extension.switchSchema(connection, schemaName);
        }
    }

    @Test
    public void testGetCurrentSchema() {
        extension.getCurrentSchema(connection);
        verify(connection).queryForObject(anyString(), eq(String.class));
    }

    @Test
    public void testGetConnectionId() {
        extension.getConnectionId(connection);
        verify(connection).queryForObject(anyString(), eq(String.class));
    }
}
