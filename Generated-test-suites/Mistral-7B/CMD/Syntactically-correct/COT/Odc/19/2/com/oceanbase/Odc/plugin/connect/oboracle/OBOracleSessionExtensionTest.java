package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.StringUtils;

@TestExtension
public class OBOracleSessionExtensionTest {

 private Connection connection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setUp() throws Exception {
 connection = JdbcOperationsUtil.getTestConnection();
 obOracleSessionExtension = new OBOracleSessionExtension();
 obOracleSessionExtension.init(connection);
 }

 @After
 public void tearDown() throws Exception {
 if (connection != null && !connection.isClosed()) {
 connection.close();
 }
 }

 // Test killQuery method
 @Test
 public void testKillQuery() {
 String connectionId = "test_connection_id";
 obOracleSessionExtension.killQuery(connection, connectionId);
 // Add assertions here if needed
 }

 // Test switchSchema method with valid schema name
 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 // Test switchSchema method with same schema name
 @Test
 public void testSwitchSchemaWithSameSchemaName() throws SQLException {
 String schemaName = "test_schema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 // Test switchSchema method with null schema name
 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithNullSchemaName() throws SQLException {
 obOracleSessionExtension.switchSchema(connection, null);
 }

 // Test getCurrentSchema method
 @Test
 public void testGetCurrentSchema() {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 // Test getConnectionId method with OceanBaseConnection
 @Test
 public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 // Test getConnectionId method with null exception
 @Test(expected = SQLException.class)
 public void testGetConnectionIdWithNull() throws SQLException {
 obOracleSessionExtension.getConnectionId(null);
 }

 // Test getConnectionId method with JdbcOperationsUtil query exception
 @Test(expected = SQLException.class)
 public void testGetConnectionIdWithQueryException() throws SQLException {
 String querySql = "select userenv('sessionid') from dual";
 obOracleSessionExtension.getConnectionId(connection);
 JdbcOperationsUtil.getJdbcOperations(connection).expectThrow(SQLException.class, querySql);
 }

}