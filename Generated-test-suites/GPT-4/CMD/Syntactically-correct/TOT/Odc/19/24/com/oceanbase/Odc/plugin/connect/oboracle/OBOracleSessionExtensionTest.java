package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension sessionExt;
    private Connection connection;
    private JdbcOperationsUtil util;

    @Before
    public void setUp() {
        sessionExt = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
        util = Mockito.mock(JdbcOperationsUtil.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(util);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        sessionExt.killQuery(connection, connectionId);
        Mockito.verify(util).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(util.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);
        sessionExt.switchSchema(connection, schemaName);
        Mockito.verify(util, Mockito.never()).update("ALTER SESSION SET CURRENT_SCHEMA="+schemaName);
    }

    @Test
    public void testSwitchSchemaWithDifferentSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(util.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("differentSchema");
        sessionExt.switchSchema(connection, schemaName);
        Mockito.verify(util).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetConnectionIdSucceeds() throws Exception {
        String querySql = "select userenv('sessionid') from dual";
        String expectedId = "1";
        Mockito.when(util.queryForObject(querySql, String.class)).thenReturn(expectedId);
        String connectionId = sessionExt.getConnectionId(connection);
        Assert.assertEquals(expectedId, connectionId);
    }

    @Test
    public void testGetConnectionIdFailsButRecovers() throws Exception {
        String querySql = "select userenv('sessionid') from dual";
        String expectedId = "1";
        Mockito.when(util.queryForObject(querySql, String.class)).thenThrow(new Exception());
        Connection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedId));
        String connectionId = sessionExt.getConnectionId(oceanBaseConnection);
        Assert.assertEquals(expectedId, connectionId);
    }
}