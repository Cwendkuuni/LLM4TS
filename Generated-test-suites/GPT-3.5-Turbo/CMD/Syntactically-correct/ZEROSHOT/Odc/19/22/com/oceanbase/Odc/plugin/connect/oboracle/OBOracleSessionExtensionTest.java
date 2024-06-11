package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.sun.scenario.effect.impl.prism.PrDrawable;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connection, connectionId);
        String expectedSql = "KILL QUERY 123";
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute(expectedSql);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String expectedSql = "ALTER SESSION SET CURRENT_SCHEMA='test_schema'";
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update(expectedSql);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn("test_schema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String querySql = "select userenv('sessionid') from dual";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenThrow(new SQLException());
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }
}