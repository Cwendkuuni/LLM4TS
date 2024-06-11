package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.test.api.PF4JTest;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@PF4JTest
@ExtendWith(OBOracleSessionExtension.class)
public class OBOracleSessionExtensionTest {
    private static Connection connection;
    private static SessionExtensionPoint obOracleSessionExtension;

    @BeforeAll
    public static void init() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
        obOracleSessionExtension = extension(OBOracleSessionExtension.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Check if the query has been killed
        // Add your assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Check if the schema has been switched
        // Add your assertion here
    }

    @Test
    public void testSwitchWithInvalidSchema() throws SQLException {
        String schemaName = null;
        try {
            obOracleSessionExtension.switchSchema(connection, schemaName);
            Assertions.fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected exception, no need to assert anything
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        // Check if the current schema is correct
        // Add your assertion here
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        // Check if the connection id is correct
        // Add your assertion here
    }
}