import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    private Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "1");
        // Add assertions here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("existingSchema");
        obMySQLSessionExtension.switchSchema(connection, "newSchema");
        // Add assertions here
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
            .thenReturn("testSchema");
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        // Add assertions here
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenReturn("1");
        String result = obMySQLSessionExtension.getConnectionId(connection);
        // Add assertions here
    }

    @Test
    public void testGetConnectionId_WithException() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        // Add assertions here
    }

    @Test
    public void testGetConnectionId_WithOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        String result = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        // Add assertions here
    }
}