package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify that the execute method is called with the correct SQL
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // No direct verification possible for switch schema method
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "test_schema";

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        assertNotNull(result);
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Mocking OceanBaseConnection for the test
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);

        String connectionId = "54321";
        // Mock behavior to return connectionId when getServerThreadId is called
        oceanBaseConnection.when(() -> oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        String result = obOracleSessionExtension.getConnectionId(oceanBaseConnection);

        assertNotNull(result);
        assertEquals(connectionId, result);
    }
}