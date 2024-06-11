import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp(){
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery(){
        String connectionId = "testId";
        sessionExtension.killQuery(mockConnection,connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test(expected=NullPointerException.class)
    public void testKillQueryWithNullConnection(){
        String connectionId = "testId";
        sessionExtension.killQuery(null,connectionId);
    }

    @Test
    public void testSwitchSchema(){
        String schema = "testSchema";
        sessionExtension.switchSchema(mockConnection, schema);
        verify(mockConnection, times(1)).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schema));
    }

    @Test(expected=SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schema = "testSchema";
        doThrow(SQLException.class).when(mockConnection).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schema));
        sessionExtension.switchSchema(mockConnection, schema);
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schema);
        String result = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(schema, result);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "testId";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        String result = sessionExtension.getConnectionId(mockConnection);
        assertEquals(connectionId, result);
    }
}