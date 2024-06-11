package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.TestExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connection;

    @BeforeEach
    public void setup() throws Exception {
        extensionManager = mock(ExtensionManager.class);
        obOracleSessionExtension = mock(OBOracleSessionExtension.class);
        connection = DriverManager.getConnection("jdbc:oceanbase:observer:12345/testDB", "user", "password");

        when(extensionManager.getExtensionsByPoint(any(SessionExtensionPoint.class))).thenReturn(new Object[]{obOracleSessionExtension});
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(extensionManager).callExtension((ExtensionPoint) any(), obOracleSessionExtension, new Object[]{"Connection", connection, "String", connectionId});
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = "testSchema";
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assertions.assertNotEquals(currentSchema, null);
        Assertions.assertNotEquals(currentSchema, "OLD_SCHEMA");
        Assertions.assertEquals(currentSchema, newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assertions.assertNotEquals(currentSchema, null);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        Assertions.assertNotEquals(connectionId, null);
    }

    @Test
    public void testKillQueryException() throws SQLException {
        String connectionId = "12345";
        when(extensionManager.callExtension((ExtensionPoint) any(), obOracleSessionExtension, new Object[]{"Connection", connection, "String", connectionId})).thenThrow(new SQLException("Kill Query exception"));
        Assertions.assertThrows(SQLException.class, () -> obOracleSessionExtension.killQuery(connection, connectionId));
    }
}