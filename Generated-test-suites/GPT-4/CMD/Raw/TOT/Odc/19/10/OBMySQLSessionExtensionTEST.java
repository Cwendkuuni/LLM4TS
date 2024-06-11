import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySqlSessionExtension;

    @Mock
    private Connection connection;
    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void shouldKillQuery() {
        obMySqlSessionExtension.killQuery(connection, "1");
        verify(jdbcOperationsUtil, times(1)).execute("KILL QUERY " + "1");
    }

    @Test
    public void shouldSwitchSchema() throws SQLException {
        when(obMySqlSessionExtension.getCurrentSchema(connection)).thenReturn("oldSchema");
        doNothing().when(connection).setCatalog("newSchema");
        obMySqlSessionExtension.switchSchema(connection, "newSchema");
        verify(connection, times(1)).setCatalog("newSchema");
    }

    @Test
    public void shouldReturnCurrentSchema() throws SQLException {
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn("Schema");
        String currentSchema = obMySqlSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, "Schema");
    }

    @Test
    public void shouldReturnConnectionId() throws SQLException {
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn("1");
        String connectionId = obMySqlSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, "1");
    }
}