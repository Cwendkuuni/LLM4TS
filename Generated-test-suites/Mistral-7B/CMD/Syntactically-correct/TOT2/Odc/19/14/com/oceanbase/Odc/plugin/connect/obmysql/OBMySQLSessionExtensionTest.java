import static org.junit.Assert.assertNoThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        SessionExtensionPoint extensionPoint = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";

        extensionPoint.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
        assertNoThrow(SQLException.class, () -> extensionPoint.killQuery(connection, connectionId));
    }
}