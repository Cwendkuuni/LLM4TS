package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@TestExtension
public class OBOracleSessionExtensionTest {

 private OBOracleSessionExtension obOracleSessionExtension;
 private Connection connection;

 @Before
 public void setup() throws SQLException {
 obOracleSessionExtension = new OBOracleSessionExtension();
 connection = JdbcOperationsUtil.getConnection();
 }

 // Test killQuery method
 @Test
 public void testKillQuery() {
 obOracleSessionExtension.killQuery(connection, "testConnectionId");
 // Check if the query has been killed
 // Add assertion statements here based on the expected behavior of "KILL QUERY"
 }

 // Test switchSchema method
 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 // Test getCurrentSchema method
 @Test
 public void testGetCurrentSchema() {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 // Test getConnectionId method
 @Test
 public void testGetConnectionId() {
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 // Test getConnectionId method when connection is not an OceanBaseConnection
 @Test(expected = IllegalStateException.class)
 public void testGetConnectionIdWithNonOceanBaseConnection() {
 obOracleSessionExtension.getConnectionId(new Connection() {});
 }

 // Test switchSchema method with existing schema
 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithExistingSchema() throws SQLException {
 String existingSchema = obOracleSessionExtension.getCurrentSchema(connection);
 obOracleSessionExtension.switchSchema(connection, existingSchema);
 }

 // Test switchSchema method with null schemaName
 @Test(expected = NullPointerException.class)
 public void testSwitchSchemaWithNullSchemaName() {
 obOracleSessionExtension.switchSchema(connection, null);
 }

 // Test getCurrentSchema method with null connection
 @Test(expected = NullPointerException.class)
 public void testGetCurrentSchemaWithNullConnection() {
 obOracleSessionExtension.getCurrentSchema(null);
 }

 // Test killQuery method with null connectionId
 @Test(expected = NullPointerException.class)
 public void testKillQueryWithNullConnectionId() {
 obOracleSessionExtension.killQuery(null, "testConnectionId");
 }
}