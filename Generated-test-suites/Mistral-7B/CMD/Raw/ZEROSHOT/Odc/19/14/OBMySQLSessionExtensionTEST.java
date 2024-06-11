import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestPluginManager;
import org.pf4j.TestPluginManagerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(TestPluginManager.class)
public class OBMySQLSessionExtensionTest {

 private ExtensionManager extensionManager;
 private PluginWrapper<OBMySQLSessionExtension> pluginWrapper;

 @Before
 public void setup() {
 extensionManager = TestPluginManagerFactory.getTestPluginManager();
 extensionManager.loadPlugin("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
 pluginWrapper = (PluginWrapper<OBMySQLSessionExtension>) extensionManager.getExtension("OBMySQLSessionExtension");
 }

 @After
 public void tearDown() {
 extensionManager.stop();
 }

 @Test
 public void testKillQuery() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createConnection();
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 obMySQLSessionExtension.killQuery(connection, connectionId);
 verifyKilledQuery(connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createConnection();
 String schemaName = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaToSameSchema() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createConnection();
 String schemaName = getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createConnection();
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createConnection();
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionIdForOceanBaseConnection() throws SQLException {
 OBMySQLSessionExtension obMySQLSessionExtension = pluginWrapper.getImplementationInstance();
 Connection connection = createOceanBaseConnection();
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 private Connection createConnection() throws SQLException {
 return TestPluginManagerFactory.createTestConnection();
 }

 private Connection createOceanBaseConnection() throws SQLException {
 return TestPluginManagerFactory.createOceanBaseTestConnection();
 }

 private void verifyKilledQuery(String connectionId) throws SQLException {
 List<String> queryResults = getQueryResults("SELECT QUERY_ID() AS query_id, QUERY_STATE() AS query_state FROM information_schema.processlist WHERE CONNECTION_ID = ?", connectionId);
 assertEquals(1, queryResults.size());
 String queryState = queryResults.get(0);
 assertEquals("dead", queryState);
 }

 private List<String> getQueryResults(String querySql, String... parameters) throws SQLException {
 return JdbcOperationsUtil.getJdbcOperations(createConnection()).queryForList(querySql, String.class, parameters);
 }

}