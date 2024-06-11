package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperations jdbcOperations;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(JdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenReturn(jdbcOperations);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(oceanBaseConnection, "1");
        verify(jdbcOperations).execute("KILL QUERY " + 1);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "testDB";
        String newSchema = "newDB";
        when(obMySQLSessionExtension.getCurrentSchema(oceanBaseConnection)).thenReturn(currentSchema, newSchema);

        obMySQLSessionExtension.switchSchema(oceanBaseConnection, newSchema);
        
        verify(oceanBaseConnection).setCatalog(newSchema);
        assertNotEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(oceanBaseConnection));
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("testDB");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(oceanBaseConnection);
        assertEquals("testDB", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertNotNull(connectionId);
        assertEquals("1", connectionId);
    }
}