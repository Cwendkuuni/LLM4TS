import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {
    @Mock
    private Connection connection;

    private OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "1001";

        extension.killQuery(connection, connectionId);

        verify(connection).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "test_schema";

        when(extension.getCurrentSchema(connection)).thenReturn("other_name");

        extension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        extension.getCurrentSchema(connection);

        verify(connection).prepareStatement("SELECT DATABASE()");
    }

    @Test
    public void testGetConnectionId() throws Exception {
        extension.getConnectionId(connection);

        verify(connection).prepareStatement("select connection_id()");
    }
}