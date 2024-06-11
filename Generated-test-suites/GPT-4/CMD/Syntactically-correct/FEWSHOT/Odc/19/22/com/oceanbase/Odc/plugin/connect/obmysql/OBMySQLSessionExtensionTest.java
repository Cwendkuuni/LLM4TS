
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;
    @Mock
    private OceanBaseConnection obConnection;
    @Mock
    private JdbcOperationsUtil operationsUtil;

    private OBMySQLSessionExtension extension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        extension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        extension.killQuery(connection, connectionId);
        verify(operationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);
        extension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = "test_schema";
        when(operationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(schema);
        assertEquals(schema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "123";
        when(operationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(obConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        assertEquals(connectionId, extension.getConnectionId(obConnection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(operationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(extension.getConnectionId(connection));
    }
}
