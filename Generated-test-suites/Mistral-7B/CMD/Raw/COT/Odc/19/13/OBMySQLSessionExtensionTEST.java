package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

@TestExtension
public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private OBMySQLSessionExtension extension;

 @Before
 public void setup() throws SQLException {
 connection = new OceanBaseConnection(null, null, null, null);
 extension = new OBMySQLSessionExtension();
 }

 @Test
 public void testKillQuery() {
 String connectionId = "123456";
 extension.killQuery(connection, connectionId);
 // Add verification code here if needed
 }

 @Test
 public void testKillQuery_Exception() {
 Connection connection = null;
 assertThrows(NullPointerException.class, () -> extension.killQuery(connection, "123456"));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 extension.switchSchema(connection, schemaName);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_SameSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = extension.getCurrentSchema(connection);
 extension.switchSchema(connection, schemaName);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_InvalidSchema() throws SQLException {
 String schemaName = null;
 extension.switchSchema(connection, schemaName);
 }

 @Test
 public void testgetCurrentSchema() {
 String currentSchema = extension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testgetCurrentSchema_NullConnection() {
 assertNull(extension.getCurrentSchema(null));
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = extension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionId_NullConnection() {
 assertNull(extension.getConnectionId(null));
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 String connectionId = extension.getConnectionId(connection);
 // Add verification code here if needed
 }
}