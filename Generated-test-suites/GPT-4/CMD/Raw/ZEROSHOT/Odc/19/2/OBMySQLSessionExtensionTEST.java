
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;
    private Connection connectionMock;
    private JdbcOperationsUtil jdbcOperationsUtilMock;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        connectionMock = Mockito.mock(Connection.class);
        jdbcOperationsUtilMock = Mockito.mock(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() {
        String connId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock)).thenReturn(jdbcOperationsUtilMock);
        sessionExtension.killQuery(connectionMock, connId);
        verify(jdbcOperationsUtilMock).execute("KILL QUERY " + connId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(sessionExtension.getCurrentSchema(connectionMock)).thenReturn(schemaName);
        sessionExtension.switchSchema(connectionMock, schemaName);

        schemaName = "newSchema";
        doNothing().when(connectionMock).setCatalog(schemaName);
        sessionExtension.switchSchema(connectionMock, schemaName);
        verify(connectionMock).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock)).thenReturn(jdbcOperationsUtilMock);
        when(jdbcOperationsUtilMock.queryForObject(querySql, String.class)).thenReturn(null);
        assertNull(sessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock)).thenReturn(jdbcOperationsUtilMock);
        when(jdbcOperationsUtilMock.queryForObject(querySql, String.class)).thenReturn(null);

        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1000L);
        assertNull(sessionExtension.getConnectionId(connectionMock));
        assertNotNull(sessionExtension.getConnectionId(oceanBaseConnection));
    }
}
