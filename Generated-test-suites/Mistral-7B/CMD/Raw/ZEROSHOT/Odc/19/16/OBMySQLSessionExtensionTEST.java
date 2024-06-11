package com.oceanbase.odc.plugin.connect.obmysql.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultServiceFactory;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtensionTest;
import com.oceanbase.odc.plugin.connect.obmysql.mock.MockConnection;
import com.oceanbase.odc.plugin.connect.obmysql.mock.MockSessionExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private DefaultServiceFactory serviceFactory;
    private List<SessionExtensionPoint> sessionExtensionPointList;

    @Before
    public void setUp() {
        extensionManager = new DefaultExtensionManager();
        serviceFactory = new DefaultServiceFactory();
        sessionExtensionPointList = new LinkedList<>();
        extensionManager.setServiceFactory(serviceFactory);
        extensionManager.loadExtensions(".");
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();

        Connection connection = mockConnection.getConnection();
        String connectionId = "123";
        when(connection.isValid()).thenReturn(true);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        obMySQLSessionExtension.killQuery(connection, connectionId);

        assertEquals("KILL QUERY 123", mockConnection.getQueryLog().get(0));
    }

    @Test
    public void testKillQueryWithInvalidConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();
        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(false);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        assertThrows(SQLException.class, () -> obMySQLSessionExtension.killQuery(connection, "123"));
    }

    @Test
    public void testSwitchSchema() throws SQLException, InterruptedException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();
        MockSessionExtensionPoint mockSessionExtensionPoint = mock(MockSessionExtensionPoint.class);
        String schemaName = "test_schema";

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(true);
        when(connection.getCatalog()).thenReturn("old_schema");
        when(connection.setCatalog(schemaName)).thenReturn(true);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);
        when(mockSessionExtensionPoint.switchSchema(connection, schemaName)).thenReturn(null);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, connection.getCatalog());
        assertEquals("test_schema", mockSessionExtensionPoint.getSchemaSwitchLog().get(0));
    }

    @Test
    public void testSwitchSchemaWithInvalidConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();
        MockSessionExtensionPoint mockSessionExtensionPoint = mock(MockSessionExtensionPoint.class);
        String schemaName = "test_schema";

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(false);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);
        when(mockSessionExtensionPoint.switchSchema(connection, schemaName)).thenReturn(null);

        assertThrows(SQLException.class, () -> obMySQLSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();
        String currentSchema = "old_schema";

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(true);
        when(connection.getCatalog()).thenReturn(currentSchema);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithInvalidConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(false);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        assertNull(obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();
        String connectionId = "123";

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(true);
        sessionExtensionPointList.add(obMySQLSessionExtension);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithInvalidConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        MockConnection mockConnection = new MockConnection();

        Connection connection = mockConnection.getConnection();
        when(connection.isValid()).thenReturn(false);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(sessionExtensionPointList);

        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @After
    public void tearDown() {
        extensionManager.shutdown();
    }
}