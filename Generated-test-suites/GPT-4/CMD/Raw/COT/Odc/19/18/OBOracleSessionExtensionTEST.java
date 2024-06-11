import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension extension = new OBOracleSessionExtension();
    Connection connection = mock(Connection.class);

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1");
        verify(connection, times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        extension.switchSchema(connection, "new_schema");
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='new_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = extension.getCurrentSchema(connection);
        Assert.assertEquals(schema, "current_schema");
    }

    @Test
    public void testGetConnectionId() {
        String id = extension.getConnectionId(connection);
        Assert.assertEquals(id, "connection_id");
    }

    @Test(expected = SQLException.class)
    public void testSQLExceptionOnSwitchSchema() throws SQLException {
        doThrow(new SQLException()).when(connection).update(any());
        extension.switchSchema(connection, "new_schema");
    }

}