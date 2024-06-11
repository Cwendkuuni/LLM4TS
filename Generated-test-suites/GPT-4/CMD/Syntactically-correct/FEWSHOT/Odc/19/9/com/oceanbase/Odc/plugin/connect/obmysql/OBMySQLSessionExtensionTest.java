
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension sessionExtension;

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        sessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "mySchema";
        when(connection.getCatalog()).thenReturn(schemaName);
        sessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1234";
        when(connection.queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdOceanBase() {
        String expectedConnectionId = "5678";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));
        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedConnectionId, connectionId);
    }

    @After
    public void cleanup() {
        sessionExtension = null;
        connection = null;
        oceanBaseConnection = null;
    }
}
