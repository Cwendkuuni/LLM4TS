package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OBOracleSessionExtension.class)
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obOracleSessionExtension;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(connection.createStatement()).thenReturn(jdbcOperationsUtil);
        obOracleSessionExtension = new OBOracleSessionExtension();
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuerySuccess() throws SQLException {
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryFail() throws SQLException {
        String connectionId = "12345";
        PowerMockito.doThrow(new SQLException("Test error")).when(jdbcOperationsUtil).execute(Mockito.anyString());
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFail() throws SQLException {
        String schemaName = "test_schema";
        PowerMockito.doThrow(new SQLException("Test error")).when(jdbcOperationsUtil).update(Mockito.anyString());
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        when(jdbcOperationsUtil.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdFromConnection() throws SQLException {
        String connectionId = "12345";
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFromJdbcOperationsUtil() throws SQLException {
        String connectionId = "67890";
        when(jdbcOperationsUtil.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);
        Optional<String> connectionIdOptional = Optional.ofNullable(obOracleSessionExtension.getConnectionId(connection));
        assertEquals(connectionId, connectionIdOptional.get());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdFromJdbcOperationsUtilFail() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException("Test error"));
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));
    }

}