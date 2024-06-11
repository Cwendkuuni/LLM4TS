package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

 private SessionExtensionPoint sessionExtension;
 private Connection connection;

 @Before
 public void setup() throws Exception {
 ExtensionManager manager = new ExtensionManager();
 PluginWrapper wrapper = manager.loadPlugin("com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension");
 sessionExtension = (OBOracleSessionExtension) wrapper.getImplementedClass(SessionExtensionPoint.class);
 connection = sessionExtension.getConnection();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = sessionExtension.getConnectionId(connection);
 sessionExtension.killQuery(connection, connectionId);
 String result = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT COUNT(*) FROM DUAL WHERE ID = '" + connectionId + "'", Long.class);
 assertEquals(0, result);
 }

 @Test
 public void testSwitchSchemaSuccess() throws SQLException {
 String schemaName = "test_schema";
 sessionExtension.switchSchema(connection, schemaName);
 String currentSchema = sessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaFailed() throws SQLException {
 String schemaName = null;
 sessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = sessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = sessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionIdWhenConnectionIsNull() {
 assertNull(sessionExtension.getConnectionId(null));
 }
}