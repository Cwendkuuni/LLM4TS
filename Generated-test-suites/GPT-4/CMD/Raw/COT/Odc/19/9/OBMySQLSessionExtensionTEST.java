import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        connection = Mockito.mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "test");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Case when current and new schema are the same
        Mockito.when(connection.getCatalog()).thenReturn("test");
        obMySQLSessionExtension.switchSchema(connection, "test");

        // Case when current and new schema are different
        Mockito.when(connection.getCatalog()).thenReturn("test1");
        obMySQLSessionExtension.switchSchema(connection, "test2");
    }

    @Test
    public void testGetCurrentSchema() {
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        Assert.assertNull(result);
    }

    @Test
    public void testGetConnectionId() {
        String result = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertNull(result);
    }
}