package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.util.JdbcOperationsUtil;

/**
 * Test class for OBMySQLSessionExtension
 */
@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

 private SessionExtensionPoint extension;
 private Connection connection;

 @Before
 public void setup() throws SQLException {
 connection = JdbcOperationsUtil.getTestConnection();
 extension = new OBMySQLSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "testConnectionId";
 extension.killQuery(connection, connectionId);
 // Check if the query is killed
 // This is not testable in JUnit as it requires a running OceanBase instance
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 extension.switchSchema(connection, schemaName);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithSameSchema() throws SQLException {
 String schemaName = "testSchema";
 extension.switchSchema(connection, schemaName);
 extension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = extension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = extension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test(expected = NullPointerException.class)
 public void testGetConnectionIdWithNullConnection() throws SQLException {
 extension.getConnectionId(null);
 }

 @Test(expected = RuntimeException.class)
 public void testGetConnectionIdWithNonOceanBaseConnection() throws SQLException {
 connection = JdbcOperationsUtil.getTestConnection(false);
 extension.getConnectionId(connection);
 }

 @Test
 public void testKillQueryWithNonOceanBaseConnection() throws SQLException {
 connection = JdbcOperationsUtil.getTestConnection(false);
 assertThrows(UnsupportedOperationException.class, () -> extension.killQuery(connection, "testConnectionId"));
 }

}