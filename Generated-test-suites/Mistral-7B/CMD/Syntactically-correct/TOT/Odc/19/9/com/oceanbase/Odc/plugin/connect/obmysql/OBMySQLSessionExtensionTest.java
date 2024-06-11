package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtensionPoint;
    private Connection connectionMock;

    @Before
    public void setup() {
        ExtensionManager extensionManager = Mockito.mock(ExtensionManager.class);
        connectionMock = Mockito.mock(Connection.class);
        sessionExtensionPoint = new OBMySQLSessionExtension();
        sessionExtensionPoint.activate(extensionManager);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        String expectedQuery = "KILL QUERY " + connectionId;

        Mockito.doNothing().when(connectionMock).createStatement();
        Mockito.doNothing().when(sessionExtensionPoint).killQuery(connectionMock, connectionId);

        sessionExtensionPoint.killQuery(connectionMock, connectionId);

        Mockito.verify(connectionMock).createStatement();
        Mockito.verify(connectionMock).execute(expectedQuery);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        sessionExtensionPoint.killQuery(null, "test_connection_id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryWithNullConnectionId() {
        sessionExtensionPoint.killQuery(connectionMock, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        Mockito.doReturn(currentSchema).when(sessionExtensionPoint).getCurrentSchema(connectionMock);
        Mockito.doNothing().when(connectionMock).setCatalog(schemaName);

        sessionExtensionPoint.switchSchema(connectionMock, schemaName);

        Mockito.verify(connectionMock).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        sessionExtensionPoint.switchSchema(null, "test_schema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        sessionExtensionPoint.switchSchema(connectionMock, null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "test_schema";

        Mockito.doReturn(expectedSchema).when(sessionExtensionPoint).queryForObject(connectionMock, "SELECT DATABASE()", String.class);

        assertEquals(expectedSchema, sessionExtensionPoint.getCurrentSchema(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        sessionExtensionPoint.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "test_connection_id";

        Mockito.doReturn(expectedConnectionId).when(connectionMock).queryForObject("select connection_id()", String.class);

        assertEquals(expectedConnectionId, sessionExtensionPoint.getConnectionId(connectionMock));
    }

    @Test(expected = Exception.class)
    public void testGetConnectionIdWithNullConnection() {
        sessionExtensionPoint.getConnectionId(null);
    }
}