
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {
   
    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection obConnection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(connection.getCatalog()).thenReturn("test");
        when(obConnection.getServerThreadId()).thenReturn(1L);
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "1");
        verify(jdbcOperationsUtil).execute("KILL QUERY " + "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "test");
        assertEquals("test", connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn("test");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("test", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn("1");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1", connectionId);
    }

    @Test
    public void testGetConnectionIdWhenExceptionOccured() {
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        String connectionId = obMySQLSessionExtension.getConnectionId(obConnection);
        assertEquals("1", connectionId);
    }
}
