import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointFactory;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.util.JdbcUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private OBMySQLSessionExtension obMySQLSessionExtension;

 @Before
 public void setup() throws Exception {
 JdbcUtils.loadJDBCDriver("com.oceanbase.jdbc.OceanBaseDriver");
 String url = "jdbc:obnew:observer:localhost:10001/testdb";
 Properties props = new Properties();
 props.setProperty("useSSL", "false");
 connection = DriverManager.getConnection(url, props);
 ExtensionManager extensionManager = new ExtensionManager();
 DefaultExtensionPointFactory factory = new DefaultExtensionPointFactory();
 DefaultExtensionRegistry registry = new DefaultExtensionRegistry();
 registry.registerExtensions(OBMySQLSessionExtension.class, factory);
 extensionManager.setExtensionRegistry(registry);
 obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 String schemaName = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 String query = "SELECT * FROM test_table";
 connection.createStatement().executeUpdate(query);
 Thread.sleep(1000); // give query a chance to run
 obMySQLSessionExtension.killQuery(connection, connectionId);
 String result = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT COUNT(*) FROM test_table", Integer.class);
 assertEquals(0, result);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, "test_schema");
 String newSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("test_schema", newSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaToInvalidSchema() throws SQLException {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, "non_existent_schema");
 }

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(currentSchema, "testdb");
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertEquals(connectionId, connection.getCatalog());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionIdOnInvalidConnection() {
 String querySql = "select connection_id()";
 try {
 JdbcOperationsUtil.getJdbcOperations(null).queryForObject(querySql, String.class);
 } catch (Exception e) {
 throw new SQLException("Unexpected exception: " + e.getMessage());
 }
 }
}