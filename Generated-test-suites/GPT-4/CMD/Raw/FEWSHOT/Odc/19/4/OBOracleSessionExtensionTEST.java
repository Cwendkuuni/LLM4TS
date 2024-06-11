
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Mock
    Connection connection;
    OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        obOracleSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        obOracleSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }
}

