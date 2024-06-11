package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private OceanBaseConnection connection;

    @Mock
    private JdbcOperations jdbcOperations;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);
    }

    // Remember to update your code according your methods or variable's functionality
    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "123");
        verify(jdbcOperations, times(1)).execute(anyString());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(jdbcOperations.queryForObject(anyString(), String.class)).thenReturn("schemaName");
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        verify(jdbcOperations, times(2)).queryForObject(anyString(), String.class);
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperations.queryForObject(anyString(), String.class)).thenReturn("schemaName");
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("schemaName", result);
    }
    
    @Test
    public void testGetConnectionId() {
        when(jdbcOperations.queryForObject(anyString(), String.class)).thenReturn("serverThreadId");
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("serverThreadId", result);
    }

    @After
    public void tearDown() {
        obOracleSessionExtension = null;
        jdbcOperations = null;
        connection = null;
    }
}
//