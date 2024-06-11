
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;
import org.powermock.api.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    private Connection connection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testKillQuery() {
        doNothing().when(jdbcOperationsUtil).execute(anyString());
        obMySQLSessionExtension.killQuery(connection, "testConnectionID");
        verify(jdbcOperationsUtil).execute("KILL QUERY testConnectionID");
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("testSchema");
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        verify(connection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchemaWithDifferentSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(connection, "schema2");
        verify(connection).setCatalog("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("testSchema");
        String schema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", schema);
    }

    @Test
    public void testGetConnectionId() {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("testConnectionId");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }
}
