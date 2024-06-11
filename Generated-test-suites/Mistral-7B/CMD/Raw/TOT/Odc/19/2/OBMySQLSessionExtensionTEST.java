package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPointAdvertiser;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

class OBMySQLSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint sessionExtensionPoint;
 private ExtensionManager extensionManager;
 private ExtensionPointAdvertiser extensionPointAdvertiser;
 private ExtensionFactory extensionFactory;

 @Before
 public void setUp() throws Exception {
 connection = JdbcOperationsUtil.getDataSource().getConnection();
 extensionManager = new ExtensionManager();
 extensionPointAdvertiser = new ExtensionPointAdvertiser(SessionExtensionPoint.class);
 extensionFactory = new ExtensionFactory();
 extensionManager.addExtension(extensionFactory.getInstance(OBMySQLSessionExtension.class));
 sessionExtensionPoint = (SessionExtensionPoint) extensionManager.getExtension(SessionExtensionPoint.class);
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
 // Here you can add assertions or checks to verify the query was killed
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_SameSchema() throws SQLException {
 String schemaName = "testSchema";
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 if (Objects.equals(currentSchema, schemaName)) {
 return;
 }
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String newCurrentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertEquals(schemaName, newCurrentSchema);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
 assertThrows(SQLException.class, () -> sessionExtensionPoint.getCurrentSchema(null));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = sessionExtensionPoint.getConnectionId(connection);
 String connectionId2 = sessionExtensionPoint.getConnectionId(null);
 assertEquals(connectionId, sessionExtensionPoint.getConnectionId(connection));
 assertThrows(SQLException.class, () -> sessionExtensionPoint.getConnectionId(null));
 sessionExtensionPoint.killQuery(connection, connectionId);
 if (connection instanceof OceanBaseConnection) {
 Connection newConnection = JdbcOperationsUtil.getDataSource().getConnection();
 String newConnectionId = sessionExtensionPoint.getConnectionId(newConnection);
 assertNotEquals(connectionId, newConnectionId);
 }
 }
}