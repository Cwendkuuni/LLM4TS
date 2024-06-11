package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private String connectionId;
    private String schemaName;
    private JdbcOperationsUtil jdbcOperationsUtil;
    
    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
        connectionId = "12345";
        schemaName = "test_schema";
        jdbcOperationsUtil = Mockito.mock(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() {
        doNothing().when(jdbcOperationsUtil).execute(anyString());
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute(anyString());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("newSchema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn(schemaName);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }
}
