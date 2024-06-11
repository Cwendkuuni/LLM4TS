package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtensionFactory;
import org.powermock.api.mockito.ann.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.common.exception.ODCException;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.util.StringUtils;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.jdbc.proxy.jdbc4.JdbcConnectionProxyFactory;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*", "javax.crypto.*"})
public class OBOracleSessionExtensionTest {

 private Connection connection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Test
 @org.junit.Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 ExtensionManager extensionManager = new ExtensionManager();
 extensionManager.start();
 obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
 connection = JdbcConnectionProxyFactory.getProxy(new OceanBaseConnection(null));
 }

 @Test
 public void testKillQuery() {
 String connectionId = "test_connection_id";
 obOracleSessionExtension.killQuery(connection, connectionId);
 // Add assertion or verification here if needed
 }

 @Test
 public void testKillQuery_NullConnection() {
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(null, "test_connection_id"));
 }

 @Test
 public void testKillQuery_NullConnectionId() {
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_SameSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_Error() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 String errorMessage = "Test error message";
 when(JdbcOperationsUtil.getJdbcOperations(connection).update(anyString()))
 .thenThrow(new SQLException(errorMessage));
 obOracleSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 // Add assertion or verification here if needed
 }

 @Test
 public void testGetCurrentSchema_NullConnection() {
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
 }

 @Test
 public void testGetConnectionId() {
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 // Add assertion or verification here if needed
 }

 @Test
 public void testGetConnectionId_NullConnection() {
 assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(null));
 }

 @Test
 public void testGetConnectionId_ConnectionException() {
 when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
 .thenThrow(new ODCException("Test exception message"));
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNull(connectionId);
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() {
 String oceanBaseConnectionId = obOracleSessionExtension.getConnectionId(connection);
 String expectedOceanBaseConnectionId = "test_ocean_base_connection_id";
 assertEquals(expectedOceanBaseConnectionId, oceanBaseConnectionId);
 }
}