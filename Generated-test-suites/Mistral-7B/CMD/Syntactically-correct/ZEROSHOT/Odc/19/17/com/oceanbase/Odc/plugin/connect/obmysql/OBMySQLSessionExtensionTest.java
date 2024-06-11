import org.junit.After;
import org.junit.Before;
import org.junit.Rule;import org.junit.rules.TestName;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultServiceRegistry;
import org.pf4j.impl. Pf4jClassPathScanner;
import org.pf4j.impl.annoreader.AnnotationReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private DefaultServiceRegistry serviceRegistry;
 private ExtensionManager extensionManager;

 @Rule
 public TestName testName = new TestName();

 @Before
 public void setUp() throws Exception {
 serviceRegistry = new DefaultServiceRegistry();
 extensionManager = new DefaultExtensionManager(serviceRegistry, new Pf4jClassPathScanner(AnnotationReader.class));
 extensionManager.scan();
 connection = OBMySQLSessionExtensionTest.getConnection(); // Replace this with your connection setup code if necessary
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null) {
 connection.close();
 }
 }

 @org.junit.Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_1";
 OBMySQLSessionExtension extension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 extension.killQuery(connection, connectionId);
 Statement statement = connection.createStatement();
 String query = "SELECT COUNT(*) FROM information_schema.processlist WHERE id = " + connectionId;
 ResultSet resultSet = statement.executeQuery(query);
 assertFalse("Query was not killed", resultSet.next());
 }

 @org.junit.Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 OBMySQLSessionExtension extension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 extension.switchSchema(connection, schemaName);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals("Switched schema to " + schemaName, schemaName, currentSchema);
 }

 @org.junit.Test
 public void testGetCurrentSchema() throws SQLException {
 OBMySQLSessionExtension extension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 String currentSchema = extension.getCurrentSchema(connection);
 assertNotNull("Current schema should not be null", currentSchema);
 }

 @org.junit.Test
 public void testGetConnectionId() throws SQLException {
 OBMySQLSessionExtension extension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 String connectionId = extension.getConnectionId(connection);
 assertNotNull("Connection ID should not be null", connectionId);
 }

 private static Connection getConnection() throws SQLException {
 // Your connection setup code here
 return null;
 }
}