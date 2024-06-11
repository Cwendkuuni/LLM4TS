
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)
            .queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("Test_Schema");
        assertEquals("Test_Schema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("Test_Schema");
        obOracleSessionExtension.switchSchema(mockConnection, "New_Schema");
        assertEquals("New_Schema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)
            .queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("1234");
        assertEquals("1234", obOracleSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(mockConnection, "1234");
        // A more complex setup would be required for testing the effect of this method
        // since it involves database interactions directly. For now, this test just checks that no exception is thrown.
    }
}
