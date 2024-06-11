package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionFactory;
import org.pf4j.TestRunner;

@RunWith(TestRunner.class)
public class OBMySQLSessionExtensionTest {

 private ExtensionManager extensionManager;
 private OBMySQLSessionExtension obMySQLSessionExtension;
 private Connection connection;

 @Before
 public void setup() throws Exception {
 extensionManager = new ExtensionManager(new TestExtensionFactory());
 PluginWrapper wrapper = extensionManager.getPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
 obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(wrapper);
 Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
 connection = DriverManager.getConnection("jdbc:obcds://localhost:2881/test", "user", "password");
 }

 @After
 public void teardown() throws Exception {
 if (connection != null && !connection.isClosed()) {
 connection.close();
 }
 extensionManager.shutdown();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 String query = "SELECT * FROM test_table";
 connection.createStatement().execute(query);
 Thread.sleep(100); // give enough time for query to run
 obMySQLSessionExtension.killQuery(connection, connectionId);
 assertFalse(connection.createStatement().executeQuery(query).next());
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 connection.createStatement().execute("CREATE SCHEMA " + schemaName);
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 connection.createStatement().execute("DROP SCHEMA " + schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchToSameSchema() throws SQLException {
 String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionIdForOceanBaseConnection() throws SQLException {
 Optional<String> connectionIdOptional = Optional.ofNullable(obMySQLSessionExtension.getConnectionId(connection));
 assertTrue(connectionIdOptional.isPresent());
 String connectionId = connectionIdOptional.get();
 assertTrue(connectionId.matches("\\d+"));
 }
}