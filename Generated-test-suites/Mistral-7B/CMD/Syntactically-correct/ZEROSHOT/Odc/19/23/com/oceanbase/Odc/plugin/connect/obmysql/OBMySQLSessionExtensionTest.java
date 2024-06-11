package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtManagerWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionFactory;
import org.powermock.api.mockito.annotation.MockAnnotation;
import org.powermock.api.mockito.annotation.PrepareForTest;
import org.powermock.core.classloader.annotations.PrepareMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPointImpl;
import com.oceanbase.jdbc.OceanBaseConnection;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class, JdbcOperationsUtil.class})
@PrepareMock(SessionExtensionPoint.class)
@MockAnnotation(classes = {ExtensionManager.class, ExtManagerWrapper.class, TestExtensionFactory.class, TestExtension.class})
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtensionPoint;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private ExtensionManager extensionManager;
    private ExtManagerWrapper extManagerWrapper;
    private TestExtension testExtension;

    @Before
    public void setUp() throws Exception {
        sessionExtensionPoint = new SessionExtensionPointImpl();
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = createMock(Connection.class);
        extensionManager = createMock(ExtensionManager.class);
        extManagerWrapper = createMock(ExtManagerWrapper.class);
        testExtension = createMock(TestExtension.class);

        when(sessionExtensionPoint.killQuery(connection, anyString())).thenCallRealMethod();
        when(sessionExtensionPoint.switchSchema(connection, anyString())).thenCallRealMethod();
        when(sessionExtensionPoint.getCurrentSchema(connection)).thenCallRealMethod();
        when(sessionExtensionPoint.getConnectionId(connection)).thenCallRealMethod();

        when(extensionManager.getExtensionByPoint(eq("com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint"), eq(OBMySQLSessionExtension.class))).thenReturn(obMySQLSessionExtension);
        when(extManagerWrapper.getExtensionManager()).thenReturn(extensionManager);
        when(testExtension.getName()).thenReturn("TestExtensionName");
        when(testExtension.getExtension()).thenReturn(obMySQLSessionExtension);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(sessionExtensionPoint).killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(sessionExtensionPoint).switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(sessionExtensionPoint);
        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
        verify(sessionExtensionPoint).getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "12345";
        when(connection.getCatalog()).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
        verify(sessionExtensionPoint).getConnectionId(connection);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals("67890", obMySQLSessionExtension.getConnectionId(connection));
        verifyNoMoreInteractions(sessionExtensionPoint);
    }
}