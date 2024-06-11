package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.DefaultPfExtension;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@ExtendWith(TestExtension.class)
public class OBOracleSessionExtensionTest {

    private static SessionExtensionPoint sessionExtension;
    private static Connection connection;

    @BeforeAll
    public static void setup() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        ExtensionFactory extensionFactory = extensionManager.getExtensionFactory();
        Class<? extends DefaultPfExtension> extensionClazz = OBOracleSessionExtension.class;
        sessionExtension = (SessionExtensionPoint) extensionFactory.createExtension(extensionClazz);

        Properties properties = new Properties();
        String url = "jdbc:oceanbase:ob://localhost:28800/test";
        properties.put("user", "testUser");
        properties.put("password", "testPassword");
        connection = DriverManager.getConnection(url, properties);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        sessionExtension.killQuery(connection, connectionId);
        Assertions.assertThrows(SQLException.class, () -> JdbcOperationsUtil.getJdbcOperations(connection).execute("SELECT COUNT(*) FROM DUAL"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        sessionExtension.switchSchema(connection, "test_schema");
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        Assertions.assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        Assertions.assertNotEquals("", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }
}