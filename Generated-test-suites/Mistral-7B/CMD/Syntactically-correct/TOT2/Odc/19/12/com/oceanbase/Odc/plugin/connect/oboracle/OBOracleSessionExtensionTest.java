package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "connectionId");
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        try {
            extension.killQuery(connection, null);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        verify(connection).createStatement();
        verify(connection).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String schemaName = null;
        try {
            extension.switchSchema(connection, schemaName);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(schemaName);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNull() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(null);
        String result = extension.getCurrentSchema(connection);
        assertEquals(null, result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(obConnection);
        when(obConnection.getServerThreadId()).thenReturn(123);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertEquals("123", extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNull() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertEquals(null, extension.getConnectionId(connection));
    }
}