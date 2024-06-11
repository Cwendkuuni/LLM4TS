package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.ClasspathScanner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@TestExtension
public class OBOracleSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint obOracleSessionExtension;

 @Before
 public void setup() throws Exception {
 ExtensionManager extensionManager = new TestExtensionManager();
 ClasspathScanner classpathScanner = new ClasspathScanner();
 extensionManager.scan(classpathScanner);
 obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension("OBOracleSessionExtension");
 connection = obOracleSessionExtension.createConnection();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_connection_id";
 obOracleSessionExtension.killQuery(connection, connectionId);
 // Verify that the query is killed
 // Add verification code here
 }

 @Test
 public void testSwitchSchema_ValidSchema() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_SameSchema() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_InvalidSchema() throws SQLException {
 String schemaName = "test_schema_invalid";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testgetCurrentSchema() throws SQLException {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionId_Exception() throws SQLException {
 obOracleSessionExtension.getConnectionId(null);
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 obOracleSessionExtension.getConnectionId(connection);
 }

 @Test
 public void testGetConnectionId_NoException() throws SQLException {
 obOracleSessionExtension.getConnectionId(connection);
 }

 @Test
 public void tearDown() throws Exception {
 if (connection != null && !connection.isClosed()) {
 connection.close();
 }
 }

}