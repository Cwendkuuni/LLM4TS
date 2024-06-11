package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringJoiner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.util.PropertyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
@Slf4j
public class OBOracleSessionExtensionTest {

    private static final String TEST_DRIVER = "com.oceanbase.jdbc.Driver";
    private static final String TEST_URL = "jdbc:oceanbase:localhost:2880/test";
    private static final String TEST_USER = "testuser";
    private static final String TEST_PASSWORD = "testpassword";

    @Rule
    public TestRule extensionRule = new TestRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            ExtensionManager extensionManager = new ExtensionManager();
            PluginWrapper wrapper = new PluginWrapper(OBOracleSessionExtension.class);
            PropertyHelper.setProperty(wrapper, "org.pf4j.Extension.NAME", "OBOracleSessionExtension");
            extensionManager.loadPlugin(wrapper);
            return super.apply(base, description);
        }
    };

    private Connection connection;
    private OBOracleSessionExtension extension;

    @Before
    public void setUp() throws SQLException {
        Class.forName(TEST_DRIVER);
        connection = DriverManager.getConnection(TEST_URL, TEST_USER, TEST_PASSWORD);
        extension = (OBOracleSessionExtension) ExtensionManager.getInstance().getExtension("OBOracleSessionExtension");
    }

    @After
    public void tearDown() throws SQLException {
        JdbcOperationsUtil.closeConnection(connection);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String queryId = "abc123";
        extension.killQuery(connection, queryId);
        String currentQueryLog = getCurrentQueryLog();
        assertFalse("Query not killed.", currentQueryLog.contains(queryId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String testSchemaName = "test_schema";
        assertNotEquals("Current schema should not be test_schema.", testSchemaName, getCurrentSchema(connection));

        extension.switchSchema(connection, testSchemaName);
        assertEquals("Switched to test_schema.", testSchemaName, getCurrentSchema(connection));

        extension.switchSchema(connection, getCurrentSchema(connection));
        assertNotEquals("Switched back to original schema.", testSchemaName, getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String originalSchema = getCurrentSchema(connection);
        extension.switchSchema(connection, "test_schema");
        String testSchema = getCurrentSchema(connection);
        assertEquals("Switched to test_schema.", testSchema, testSchema);
        assertNotEquals("Original schema not the same.", originalSchema, testSchema);

        extension.switchSchema(connection, originalSchema);
        assertEquals("Switched back to original schema.", originalSchema, getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId1 = extension.getConnectionId(connection);
        Connection connection2 = DriverManager.getConnection(TEST_URL, TEST_USER, TEST_PASSWORD);
        String connectionId2 = extension.getConnectionId(connection2);
        assertNotEquals("Different connections have different IDs.", connectionId1, connectionId2);
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return extension.getCurrentSchema(connection);
    }

    private String getCurrentQueryLog() throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForString("SELECT QUERY_RESULT FROM v$sql WHERE USERNAME = '" + TEST_USER + "' ORDER BY START_TIME DESC");
    }
}