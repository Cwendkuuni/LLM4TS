
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    @Mock
    Connection connection;

    @Mock
    OceanBaseConnection oceanBaseConnection;

    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    // Test for public method 'killQuery'
    @Test
    public void killQuery_shouldSuccess() {
        doNothing().when(connection).execute("KILL QUERY " + "testId");
        obMySQLSessionExtension.killQuery(connection, "testId");
    }

    // Test for public method 'switchSchema'
    @Test
    public void switchSchema_shouldNotExecuteWhenSchemaIsSameAsCurrent() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("testSchema");
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void switchSchema_shouldExecuteWhenSchemaIsNotSameAsCurrent() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("anotherSchema");
        doNothing().when(connection).setCatalog("testSchema");
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    // Test for public method 'getCurrentSchema'
    @Test
    public void getCurrentSchema_shouldSuccess() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");
        String schema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", schema);
    }

    // Test for public method 'getConnectionId'
    @Test
    public void getConnectionId_shouldReturnNullWhenServerError() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(null);
        String id = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(null, id);
    }

    @Test
    public void getConnectionId_shouldNotReturnNullWhenServerError() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn((long) 1);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String id = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull(id);
    }
}
