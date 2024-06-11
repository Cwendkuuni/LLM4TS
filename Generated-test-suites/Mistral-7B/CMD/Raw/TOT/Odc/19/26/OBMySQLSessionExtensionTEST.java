package com.oceanbase.odc.plugin.connect.obmysql.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint sessionExtensionPoint;
 private ExtensionManager extensionManager;

 @Before
 public void setup() throws Exception {
 connection = JdbcOperationsUtil.getNewConnection();
 extensionManager = new DefaultExtensionManager();
 extensionManager.loadExtensions("classpath:META-INF/extensions.xml");
 sessionExtensionPoint = (SessionExtensionPoint) extensionManager.getExtension(OBMySQLSessionExtension.class);
 }

 @After
 public void teardown() throws Exception {
 JdbcOperationsUtil.closeConnection(connection);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 String schemaName = "testSchema";
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals("testSchema", currentSchema);
 sessionExtensionPoint.killQuery(connection, connectionId);
 String currentSchemaAfterKill = sessionExtensionPoint.getCurrentSchema(connection);
 assertNull(currentSchemaAfterKill);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithInvalidSchema() throws SQLException {
 sessionExtensionPoint.switchSchema(connection, null);
 }

 @Test
 public void testSwitchSchemaWithValidSchema() throws SQLException {
 String schemaName = "testSchema";
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(null, currentSchema);
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String newSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(schemaName, newSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 assertEquals(connectionId, sessionExtensionPoint.getConnectionId(connection));
 }

 @Test
 public void testGetCurrentSchemaWithConnection() throws SQLException {
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertNull(currentSchema);
 }

 @Test
 public void testGetCurrentSchemaWithNullConnection() {
 String currentSchema = sessionExtensionPoint.getCurrentSchema(null);
 assertNull(currentSchema);
 }
}