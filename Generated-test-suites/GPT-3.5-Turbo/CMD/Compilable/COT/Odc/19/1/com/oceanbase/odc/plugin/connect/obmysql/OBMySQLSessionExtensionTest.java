package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, "12345");
    }

    @Test
    public void testSwitchSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        try {
            sessionExtension.switchSchema(connection, "test_schema");
        } catch (SQLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    // Edge cases and exception handling scenarios to be tested:
    // 1. Null connection passed to each method
    // 2. SQLException thrown in switchSchema
    // 3. Exception thrown in getConnectionId

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(null, "12345");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSQLException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("test_schema");
        sessionExtension.switchSchema(connection, "test_schema");
    }

    @Test
    public void testGetConnectionIdException() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(sessionExtension.getConnectionId(connection)).thenThrow(new RuntimeException());
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }
}