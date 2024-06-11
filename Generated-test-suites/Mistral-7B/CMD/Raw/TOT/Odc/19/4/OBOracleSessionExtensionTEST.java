package com.oceanbase.odc.plugin.connect.oboracle.test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.impl.DefaultExtensionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;
    private SessionExtensionPoint sessionExtensionPoint;
    private ExtensionManager extensionManager;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:oceanbase:observer://localhost:2880/odp.test", "username", "password");
        extensionManager = new DefaultExtensionManager(OBOracleSessionExtension.class);
        sessionExtensionPoint = extensionManager.getExtension(SessionExtensionPoint.class);
        obOracleSessionExtension = (OBOracleSessionExtension) sessionExtensionPoint;
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_ValidSchemaName() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assertions.assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        Assertions.assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_NullConnection() {
        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetCurrentSchema_InvalidConnection() throws SQLException {
        Connection invalidConnection = Mockito.mock(Connection.class);
        Mockito.when(invalidConnection.isClosed()).thenReturn(true);
        Assertions.assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(invalidConnection));
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        Assertions.assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_ValidConnection() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Properties props = new Properties();
        props.put("serverThreadId", "123");
        connection = new OceanBaseConnection(connection, props);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        Assertions.assertEquals("123", connectionId);
    }
}