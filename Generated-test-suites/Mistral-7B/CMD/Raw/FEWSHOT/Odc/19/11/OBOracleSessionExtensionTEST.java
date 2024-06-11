package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.PropertiesHelper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@TestExtension
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, connectionId);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY " + connectionId)).thenReturn(true);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        sessionExtension.init(PropertiesHelper.emptyProperties());
        sessionExtension.start();
        sessionExtension.killQuery(connection, connectionId);
        sessionExtension.stop();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenReturn(1);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        sessionExtension.init(PropertiesHelper.emptyProperties());
        sessionExtension.start();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
        sessionExtension.switchSchema(connection, "anotherSchema");
        String currentSchema2 = sessionExtension.getCurrentSchema(connection);
        assertNotEquals(schemaName, currentSchema2);
        sessionExtension.stop();
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        String expectedSchema = "expectedSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(connection, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        String connectionId = "123";
        sessionExtension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
            when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.parseInt(connectionId));
        }
        String actualConnectionId = sessionExtension.getConnectionId(connection);
        assertEquals(connectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        String connectionId = null;
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(connection, "select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException("Error"));
        sessionExtension.getConnectionId(connection);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
            connectionId = oceanBaseConnection.getServerThreadId() + "";
        }
        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        assertNull(sessionExtension.getConnectionId(null));
    }

    @Test
    public void testInit() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        TestExtensionManager testExtensionManager = new TestExtensionManager();
        TestExtension testExtension = new TestExtension();
        testExtensionManager.addExtension(sessionExtension);
        sessionExtension.init(PropertiesHelper.emptyProperties());
        sessionExtension.stop();
        testExtensionManager.shutdown();
    }

    @Test
    public void testStart() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        TestExtensionManager testExtensionManager = new TestExtensionManager();
        TestExtension testExtension = new TestExtension();
        testExtensionManager.addExtension(sessionExtension);
        sessionExtension.init(PropertiesHelper.emptyProperties());
        sessionExtension.start();
        assert(sessionExtension.isActive());
        sessionExtension.stop();
        testExtensionManager.shutdown();
    }

    @Test
    public void testStop() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        TestExtensionManager testExtensionManager = new TestExtensionManager();
        TestExtension testExtension = new TestExtension();
        testExtensionManager.addExtension(sessionExtension);
        sessionExtension.init(PropertiesHelper.emptyProperties());
        sessionExtension.start();
        sessionExtension.stop();
        assert(!sessionExtension.isActive());
        testExtensionManager.shutdown();
    }

}