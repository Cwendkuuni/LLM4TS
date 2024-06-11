package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.killQuery(connection, "12345");

        // Add assertions here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.switchSchema(connection, "schema");

        // Add assertions here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // Mocking JdbcOperationsUtil.getJdbcOperations(connection).queryForObject method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("schema");

        assertEquals("schema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // Mocking JdbcOperationsUtil.getJdbcOperations(connection).queryForObject method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("12345");

        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }
}