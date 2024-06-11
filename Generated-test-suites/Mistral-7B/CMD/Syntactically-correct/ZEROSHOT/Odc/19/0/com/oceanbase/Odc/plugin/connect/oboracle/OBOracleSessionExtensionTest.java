package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@RunWith(TestExtensionRunner.class)
public class OBOracleSessionExtensionTest {

 private SessionExtensionPoint extensionPoint;
 private ExtensionManager extensionManager;
 private Extension extension;
 private PluginWrapper wrapper;
 private Connection connection;

 @Before
 public void setUp() throws Exception {
 extensionManager = new ExtensionManager();
 extension = new OBOracleSessionExtension();
 extensionManager.loadExtension(extension);
 extensionPoint = extensionManager.getExtension(SessionExtensionPoint.class);
 wrapper = extensionManager.getPluginWrapper(extension);
 connection = extensionPoint.getTestConnection();
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null) {
 connection.close();
 }
 extensionManager.unloadExtension(wrapper);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String queryId = "query_1";
 extensionPoint.killQuery(connection, queryId);
 Statement statement = connection.createStatement();
 ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM user_objects WHERE object_name = '" + queryId + "'");
 assertFalse("Query was not killed.", resultSet.next());
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String testSchema = "test_schema";
 extensionPoint.switchSchema(connection, testSchema);
 String currentSchema = extensionPoint.getCurrentSchema(connection);
 assertEquals("Schema was not switched.", testSchema, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaToSameSchema() throws SQLException {
 String testSchema = "test_schema";
 extensionPoint.switchSchema(connection, testSchema);
 extensionPoint.switchSchema(connection, testSchema);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = extensionPoint.getCurrentSchema(connection);
 assertNotNull("Current schema is null.", currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = extensionPoint.getConnectionId(connection);
 assertNotNull("Connection ID is null.", connectionId);
 }
}