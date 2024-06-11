package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

 private SessionExtensionPoint sessionExtensionPoint;
 private ExtensionManager extensionManager;

 @Before
 public void setUp() throws Exception {
 extensionManager = new ExtensionManager();
 extensionManager.scanClasspath("com.oceanbase.odc.plugin.connect.obmysql");
 PluginWrapper<SessionExtensionPoint> pluginWrapper = extensionManager.getPlugin("OBMySQLSessionExtension");
 sessionExtensionPoint = pluginWrapper.getImplementedObject();
 }

 @Test
 public void testKillQuery() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String connectionId = "test_connection_id";

 // When
 sessionExtensionPoint.killQuery(connection, connectionId);

 // Then
 // In a real environment, we cannot verify the result directly
 // Instead, we can check that no exception is thrown
 // Add assertions here if you have a way to verify the result
 }

 @Test
 public void testKillQuery_InvalidConnectionId() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String connectionId = null;

 // When & Then
 expectException(SQLException.class);
 sessionExtensionPoint.killQuery(connection, connectionId);
 }

 @Test
 public void testKillQuery_NullConnection() throws SQLException {
 // Given
 Connection connection = null;
 String connectionId = "test_connection_id";

 // When & Then
 expectException(NullPointerException.class);
 sessionExtensionPoint.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String schemaName = "test_schema";
 String currentSchema = "existing_schema";

 // When
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);

 // Then
 assertEquals(schemaName, actualSchema);
 }

 @Test
 public void testSwitchSchema_SameSchema() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String schemaName = "existing_schema";

 // When
 sessionExtensionPoint.switchSchema(connection, schemaName);
 String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);

 // Then
 assertEquals(schemaName, actualSchema);
 }

 @Test
 public void testSwitchSchema_InvalidSchema() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String schemaName = null;

 // When & Then
 expectException(SQLException.class);
 sessionExtensionPoint.switchSchema(connection, schemaName);
 }

 @Test
 public void testSwitchSchema_NullConnection() throws SQLException {
 // Given
 Connection connection = null;
 String schemaName = "test_schema";

 // When & Then
 expectException(NullPointerException.class);
 sessionExtensionPoint.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String expectedSchema = "existing_schema";

 // When
 String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);

 // Then
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetCurrentSchema_NullConnection() throws SQLException {
 // Given
 Connection connection = null;

 // When & Then
 expectException(NullPointerException.class);
 sessionExtensionPoint.getCurrentSchema(connection);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 // Given
 Connection connection = getMockConnection();
 String expectedConnectionId = "test_connection_id";

 // When
 String actualConnectionId = sessionExtensionPoint.getConnectionId(connection);

 // Then
 assertEquals(expectedConnectionId, actualConnectionId);
 }

 @Test
 public void testGetConnectionId_NullConnection() throws SQLException {
 // Given
 Connection connection = null;

 // When & Then
 expectException(NullPointerException.class);
 sessionExtensionPoint.getConnectionId(connection);
 }

 private Connection getMockConnection() throws SQLException {
 return new OceanBaseConnection(null, "jdbc:oceanbase:local:1527", "user", "password");
 }
}