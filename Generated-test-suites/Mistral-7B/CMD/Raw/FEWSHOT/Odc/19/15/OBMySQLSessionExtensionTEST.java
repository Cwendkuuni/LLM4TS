package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.AbstractExtension;
import org.pf4j.impl. europa.AbstractServiceLoader;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "connectionId");
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY connectionId")).thenReturn(1);
        assertEquals(1, extension.killQuery(connection, "connectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.getMetaData()).thenThrow(new SQLException());
        extension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("previousSchema");
        when(connection.setCatalog(schemaName)).thenReturn(true);
        assertEquals("testSchema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);
        assertNull(extension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.getMetaData()).thenThrow(new SQLException());
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        when(connection.getMetaData()).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(connection, "SELECT DATABASE()", String.class))
                .thenReturn(schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123);
        assertEquals("123", extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_NullConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(null);
        assertNull(extension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenThrow(new SQLException());
        extension.getConnectionId(connection);
    }

    @Test
    public void testExtensionRegistration() {
        AbstractServiceLoader serviceLoader = new AbstractServiceLoader();
        Extension extension = new AbstractExtension() {
            @Override
            public String getInterfaceClass() {
                return SessionExtensionPoint.class.getName();
            }

            @Override
            public SessionExtensionPoint create() {
                return new OBMySQLSessionExtension();
            }
        };
        serviceLoader.loadServices(SessionExtensionPoint.class, this.getClass().getResource("/"));
        SessionExtensionPoint sessionExtensionPoint = serviceLoader.getService(SessionExtensionPoint.class);
        assertEquals(extension, sessionExtensionPoint);
    }
}