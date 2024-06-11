
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private OceanBaseConnection obConnection;

    @Mock
    private JdbcOperationsUtil mockJdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockConnection, "123");
        verify(mockJdbcOperationsUtil, times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(mockConnection, "schema2");
        verify(mockConnection, times(1)).setCatalog("schema2");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(mockConnection, "schema1");
        verify(mockConnection, times(0)).setCatalog("schema1");
    }

    @Test
    public void testGetCurrentSchema() {
        when(mockJdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn("schema1");
        String schema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("schema1", schema);
        verify(mockJdbcOperationsUtil, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        when(mockJdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn("123");
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("123", connectionId);
        verify(mockJdbcOperationsUtil, times(1)).queryForObject("select connection_id()", String.class);
    }

    @Test
    public void testGetConnectionIdException() {
        when(mockJdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(obConnection.getServerThreadId()).thenReturn(123L);
        String connectionId = obMySQLSessionExtension.getConnectionId(obConnection);
        assertNotNull(connectionId);
        assertEquals("123", connectionId);
    }
}
