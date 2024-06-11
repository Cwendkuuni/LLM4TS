
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        this.obOracleSessionExtension = new OBOracleSessionExtension();
        this.connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "123");
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "newSchema");
        Mockito.verify(connection, Mockito.times(1)).execute("ALTER SESSION SET CURRENT_SCHEMA=newSchema");
    }

    @Test
    public void testGetCurrenSchema() throws SQLException {
        String expectedSchema = "currentSchema";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
        .thenReturn("currentSchema");
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "123";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
        .thenReturn("123");
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        Assert.assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        Connection obConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(obConnection).queryForObject("select userenv('sessionid') from dual", String.class))
        .thenThrow(new SQLException());
        Mockito.when(((OceanBaseConnection)obConnection).getServerThreadId()).thenReturn(123L);
        String actualConnectionId = obOracleSessionExtension.getConnectionId(obConnection);
        Assert.assertEquals("123", actualConnectionId);
    }

    @After
    public void tearDown() {
        obOracleSessionExtension = null;
        connection = null;
    }

}
