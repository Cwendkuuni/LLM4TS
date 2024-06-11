package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Tests for {@link OBOracleSessionExtension}.
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obOracleSessionExtension;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @After
    public void tearDown() throws SQLException {
        JdbcOperationsUtil.closeConnection(connection);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        String killQuerySql = "KILL QUERY " + connectionId;

        Mockito.doNothing().when(connection).createStatement();
        Mockito.doNothing().when(connection).executeUpdate(killQuerySql);

        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection).executeUpdate(killQuerySql);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "old_schema";
        String alterSchemaSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);

        Mockito.doReturn(currentSchema).when(obOracleSessionExtension).getCurrentSchema(connection);
        Mockito.doNothing().when(connection).createStatement();
        Mockito.doNothing().when(connection).executeUpdate(alterSchemaSql);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(obOracleSessionExtension).getCurrentSchema(connection);
        verify(connection).createStatement();
        verify(connection).executeUpdate(alterSchemaSql);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = schemaName;

        Mockito.doReturn(currentSchema).when(obOracleSessionExtension).getCurrentSchema(connection);

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String querySchemaSql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "current_schema";

        Mockito.doReturn(currentSchema).when(connection).createStatement();
        Mockito.doReturn(currentSchema).when(connection).queryForObject(querySchemaSql, String.class);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "test_connection_id";
        String queryConnectionIdSql = "select userenv('sessionid') from dual";

        Mockito.doReturn(connectionId).when(connection).queryForObject(queryConnectionIdSql, String.class);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNoUserEnv() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        Mockito.doReturn(oceanBaseConnection).when(connection).unwrap(OceanBaseConnection.class);
        String connectionId = oceanBaseConnection.getServerThreadId();

        Mockito.doReturn(connectionId).when(obOracleSessionExtension).getConnectionId(connection);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }
}