package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Extension
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint sessionExtensionPoint;
    private Connection connectionMock;

    @Before
    public void setUp() {
        connectionMock = Mockito.mock(Connection.class);
        sessionExtensionPoint = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        sessionExtensionPoint.killQuery(connectionMock, connectionId);
        Mockito.verify(connectionMock).createStatement();
        Mockito.verify(connectionMock).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        sessionExtensionPoint.killQuery(null, "testConnectionId");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        sessionExtensionPoint.killQuery(connectionMock, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        sessionExtensionPoint.switchSchema(connectionMock, schemaName);
        Mockito.verify(connectionMock).createStatement();
        Mockito.verify(connectionMock).execute("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String schemaName = null;
        sessionExtensionPoint.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testCurrentSchema";
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connectionMock.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(currentSchema);
        assertEquals(currentSchema, sessionExtensionPoint.getCurrentSchema(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() throws SQLException {
        sessionExtensionPoint.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        Mockito.when(connectionMock.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, sessionExtensionPoint.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String oceanBaseThreadId = "testThreadId";
        Mockito.when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);
        Mockito.when(connectionMock.hasMetaDataAccess()).thenReturn(false);
        assertEquals(oceanBaseThreadId, sessionExtensionPoint.getConnectionId(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() throws SQLException {
        sessionExtensionPoint.getConnectionId(null);
    }
}