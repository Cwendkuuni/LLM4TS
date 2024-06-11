package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection connectionMock;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        // Test killQuery method
        String connectionId = "12345";
        // Mock JdbcOperationsUtil and execute to return expected value
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        doReturn(jdbcOperationsUtilMock).when(JdbcOperationsUtil.class);
        doReturn().when(jdbcOperationsUtilMock.execute("KILL QUERY " + connectionId)).thenReturn();

        sessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test switchSchema method
        String schemaName = "test_schema";
        String currentSchema = "default_schema";
        // Mock getCurrentSchema method to return currentSchema
        doReturn(currentSchema).when(sessionExtension).getCurrentSchema(connectionMock);
        // Mock JdbcOperationsUtil update method
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        doReturn(jdbcOperationsUtilMock).when(JdbcOperationsUtil.class);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        doReturn().when(jdbcOperationsUtilMock.update(alterSql)).thenReturn();

        sessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        // Test getCurrentSchema method
        String expectedSchema = "schema_test";
        // Mock JdbcOperationsUtil queryForObject method
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        doReturn(jdbcOperationsUtilMock).when(JdbcOperationsUtil.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        doReturn(expectedSchema).when(jdbcOperationsUtilMock.queryForObject(querySql, String.class));

        assertEquals(expectedSchema, sessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() {
        // Test getConnectionId method
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "12345";
        // Mock JdbcOperationsUtil queryForObject method
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        doReturn(jdbcOperationsUtilMock).when(JdbcOperationsUtil.class);
        doReturn(connectionId).when(jdbcOperationsUtilMock.queryForObject(querySql, String.class));

        assertEquals(connectionId, sessionExtension.getConnectionId(connectionMock));
    }
}