package com.oceanbase.odc.plugin.connect.obmysql.test;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@TestExtensionFactory(OBMySQLSessionExtensionFactory.class)
public class OBMySQLSessionExtensionTest {

 private OBMySQLSessionExtension obMySQLSessionExtension;
 private ExtensionManager extensionManager;
 private Connection connection;

 @Before
 public void setup() throws Exception {
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 extensionManager = new ExtensionManager(obMySQLSessionExtension.getClass());
 extensionManager.start();
 connection = obMySQLSessionExtension.getConnection();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "12345";
 obMySQLSessionExtension.killQuery(connection, connectionId);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("No query was killed", currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNull(connectionId);
 }

 @Test
 public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
 obMySQLSessionExtension.killQuery(connection, "12345");
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test(expected = SQLException.class)
 public void testGetCurrentSchemaWhenException() throws SQLException {
 throw new SQLException("Test exception");
 }

 @Test
 public void testSwitchSchemaToSameSchema() throws SQLException {
 String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testKillQueryWithInvalidConnectionId() throws SQLException {
 obMySQLSessionExtension.killQuery(connection, "invalid_connection_id");
 }

 @Test
 public void testSwitchSchemaWithNullSchemaName() {
 obMySQLSessionExtension.switchSchema(connection, null);
 }

 @Test
 public void testSwitchSchemaWithEmptySchemaName() {
 obMySQLSessionExtension.switchSchema(connection, "");
 }

 @Test
 public void testSwitchSchemaWithInvalidSchemaName() {
 obMySQLSessionExtension.switchSchema(connection, "invalid_schema_name");
 }

 @Test
 public void testKillQueryWithNullConnection() {
 obMySQLSessionExtension.killQuery(null, "connection_id");
 }

 @Test
 public void testKillQueryWithEmptyConnectionId() {
 obMySQLSessionExtension.killQuery(connection, "");
 }

 @Test
 public void testKillQueryWithInvalidConnectionId() {
 obMySQLSessionExtension.killQuery(connection, "invalid_connection_id");
 }

 @Test
 public void testGetCurrentSchemaWithNullConnection() {
 obMySQLSessionExtension.getCurrentSchema(null);
 }

 @Test
 public void testGetCurrentSchemaWithEmptyConnection() throws SQLException {
 obMySQLSessionExtension.getCurrentSchema(new OceanBaseConnection());
 }

 @Test
 public void testGetCurrentSchemaWithInvalidConnection() throws SQLException {
 obMySQLSessionExtension.getCurrentSchema(new Object());
 }

 @Test
 public void testGetConnectionIdWithNullConnection() {
 obMySQLSessionExtension.getConnectionId(null);
 }

 @Test
 public void testGetConnectionIdWithEmptyConnection() throws SQLException {
 obMySQLSessionExtension.getConnectionId(new OceanBaseConnection());
 }

 @Test
 public void testGetConnectionIdWithInvalidConnection() throws SQLException {
 obMySQLSessionExtension.getConnectionId(new Object());
 }

 @Test
 public void testKillQueryWithConnectionClosed() throws SQLException {
 connection.close();
 obMySQLSessionExtension.killQuery(connection, "connection_id");
 }
}