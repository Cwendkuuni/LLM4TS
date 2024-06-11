import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointFactory;
import org.pf4j.impl.DefaultExtensionPointWrapper;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.impl.DefaultModule;
import org.pf4j.impl.DefaultModuleRegistry;
import org.pf4j.impl.impl.DefaultExtensionPointFactoryImpl;
import org.pf4j.impl.impl.DefaultExtensionRegistryImpl;
import org.pf4j.impl.impl.DefaultModuleImpl;
import org.pf4j.impl.impl.DefaultModuleRegistryImpl;
import org.pf4j.impl.test.AbstractExtensionPointTest;
import org.pf4j.impl.test.ExtensionPointTestHelper;
import org.pf4j.impl.test.TestExtensionPointFactory;
import org.pf4j.impl.util.ClassUtils;
import org.pf4j.util.ClasspathScanner;
import org.pf4j.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {

 private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
 private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/your_database";
 private static final String USER = "your_username";
 private static final String PASSWORD = "your_password";

 private ExtensionManager extensionManager;
 private OBMySQLSessionExtension obMySQLSessionExtension;

 @Before
 public void setup() throws Exception {
 Properties properties = new Properties();
 properties.setProperty("pf4j.classpath.mainClass", OBMySQLSessionExtensionTest.class.getName());
 properties.setProperty("pf4j.classpath.classpath.0", ClassUtils.classpathElement(OBMySQLSessionExtension.class));
 properties.setProperty("pf4j.classpath.classpath.1", ClassUtils.classpathElement(JdbcOperationsUtil.class));
 properties.setProperty("pf4j.classpath.classpath.2", ClassUtils.classpathElement(Connection.class));
 properties.setProperty("pf4j.classpath.classpath.3", ClassUtils.classpathElement(ResultSet.class));
 ExtensionPointTestHelper helper = new ExtensionPointTestHelper(properties);
 DefaultModule module = new DefaultModuleImpl();
 DefaultModuleRegistry moduleRegistry = new DefaultModuleRegistryImpl();
 DefaultExtensionRegistry extensionRegistry = new DefaultExtensionRegistryImpl();
 extensionRegistry.registerExtensions(new DefaultExtensionPointFactoryImpl(), helper.getExtensionFactory());
 moduleRegistry.registerExtensions(new TestExtensionPointFactory(), extensionRegistry);
 module.setName("OBMySQLSessionExtensionTestModule");
 moduleRegistry.addModule(module);
 extensionManager = new DefaultExtensionManager(helper.getExtensionLoader(), moduleRegistry);
 extensionManager.start();
 obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);

 ClassUtils.forName(DRIVER_CLASS);
 try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
 obMySQLSessionExtension.switchSchema(connection, "test_schema");
 }
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(null);
 assertNotNull(connectionId);
 obMySQLSessionExtension.killQuery(null, connectionId);
 fail("Expected exception not thrown");
 }

 @Test
 @Ignore("Requires a running OceanBase database to test")
 public void testSwitchSchema() throws SQLException {
 Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
 try {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 String testSchema = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, testSchema);
 String newSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(testSchema, newSchema);
 } finally {
 connection.close();
 }
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 connection.close();
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 connection.close();
 }

 @After
 public void teardown() throws Exception {
 obMySQLSessionExtension.switchSchema(null, null);
 extensionManager.stop();
 }
}