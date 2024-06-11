import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        try {
            obOracleSessionExtension.killQuery(connection, connectionId);
            // Verify that execute method is called with correct query
            verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSwitchSchema() {
        String schemaName = "test_schema";
        try {
            obOracleSessionExtension.switchSchema(connection, schemaName);
            // Verify that update method is called with correct alterSql query
            verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test_schema";
        try {
            when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
            String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
            // Verify that queryForObject method is called with correct query sql
            verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
            assertEquals(expectedSchema, currentSchema);
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "98765";
        try {
            when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);
            String connectionId = obOracleSessionExtension.getConnectionId(connection);
            // Verify that queryForObject method is called with correct query sql
            verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("select userenv('sessionid') from dual", String.class);
            assertEquals(expectedConnectionId, connectionId);
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }
}