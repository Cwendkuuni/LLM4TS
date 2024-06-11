import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testKillQuery() throws Exception {
        obMySQLSessionExtension.killQuery(connection, "1234");
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema() throws Exception {
        when(connection.getCatalog()).thenReturn("abc");
        obMySQLSessionExtension.switchSchema(connection, "abc");
        verify(connection, times(1)).getCatalog();
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testGetConnectionId() throws Exception {
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
    }
}