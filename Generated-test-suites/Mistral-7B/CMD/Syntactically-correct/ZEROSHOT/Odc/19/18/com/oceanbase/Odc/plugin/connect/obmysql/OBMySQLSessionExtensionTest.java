package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(TestExtensionManager.class)
public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint sessionExtensionPoint;
 private ExtensionManager extensionManager;

 @Before
 public void setUp() throws Exception {
 PropertiesUtil.loadSystemProperties();
 extensionManager = new ExtensionManager();
 ExtensionFactory extensionFactory = new ExtensionFactory();
 PluginWrapper pluginWrapper = extensionFactory.createPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
 extensionManager.addExtension(pluginWrapper);
 sessionExtensionPoint = extensionManager.getExtension(SessionExtensionPoint.class);
 connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:10011/oceanbase:oracle:testdb", "testuser", "testpassword");
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null && !connection.isClosed()) {
 connection.close();
 }
 extensionManager.shutdown();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 sessionExtensionPoint.killQuery(connection, connectionId);
 String queryResult = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT COUNT(*) FROM user WHERE id = " + connectionId, Integer.class);
 assertEquals(0, Integer.parseInt(queryResult));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String newSchema = "test_schema";
 sessionExtensionPoint.switchSchema(connection, newSchema);
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(newSchema, currentSchema);
 }

 @Test
 public void testgetCurrentSchema() throws SQLException {
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaToSameSchema() throws SQLException {
 String newSchema = sessionExtensionPoint.getCurrentSchema(connection);
 sessionExtensionPoint.switchSchema(connection, newSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Optional<String> connectionIdOptional = Optional.ofNullable(sessionExtensionPoint.getConnectionId(connection));
 assertTrue(connectionIdOptional.isPresent());
 assertTrue(connectionIdOptional.get().length() > 0);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionIdWhenConnectionIsNull() throws SQLException {
 sessionExtensionPoint.getConnectionId(null);
 fail("Expected SQLException but got none");
 }

 @Test
 public void testGetConnectionIdWhenConnectionIsNotInstanceOfOceanBaseConnection() throws SQLException {
 Connection dummyConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:testdb", "testuser", "testpassword");
 String connectionId = sessionExtensionPoint.getConnectionId(dummyConnection);
 assertNull(connectionId);
 }

 @Test
 public void testGetConnectionIdWhenConnectionIsInstanceOfOceanBaseConnection() throws SQLException {
 OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
 String connectionId = sessionExtensionPoint.getConnectionId(oceanBaseConnection);
 assertNotNull(connectionId);
 }
}