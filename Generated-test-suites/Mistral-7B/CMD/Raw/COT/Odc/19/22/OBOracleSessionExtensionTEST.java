package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.extensions.FactoryExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(Extension.class)
public class OBOracleSessionExtensionTest {

 private ExtensionManager extensionManager;
 private SessionExtensionPoint obOracleSessionExtension;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 extensionManager = new ExtensionManager(OBOracleSessionExtension.class.getPackage().getName(), null, null);
 obOracleSessionExtension = (SessionExtensionPoint) extensionManager.getExtension(OBOracleSessionExtension.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String connectionId = "test_connection_id";
 obOracleSessionExtension.killQuery(connection, connectionId);
 // Add assertions or verification codes here
 }

 @Test
 public void testKillQuery_NoConnection() {
 String connectionId = "test_connection_id";
 assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(null, connectionId));
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(new OceanBaseConnection(), null));
 }

 @Test
 public void testKillQuery_NullConnectionId() {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_CurrentSchemaIsSame() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String schemaName = "test_schema";
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_InvalidSchemaName() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String schemaName = "test_schema'";
 assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetCurrentSchema_NoConnection() {
 assertNull(obOracleSessionExtension.getCurrentSchema(null));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionId_NoConnection() {
 assertNull(obOracleSessionExtension.getConnectionId(null));
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 Connection connection = new FactoryExtension(OceanBaseConnection.class, null).createExtension();
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionId_NoConnectionId() {
 Connection connection = JdbcOperationsUtil.getTestConnection();
 assertNull(obOracleSessionExtension.getConnectionId(connection));
 }
}