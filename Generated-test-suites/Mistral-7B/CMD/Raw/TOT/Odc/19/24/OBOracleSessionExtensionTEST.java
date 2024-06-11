package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.exception.OdcPluginException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBOracleSessionExtensionTest {

 private Connection connection;
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setUp() throws Exception {
 connection = DriverManager.getConnection("jdbc:oceanbase:observer://localhost:10011/oceanbase:1527", "username", "password");
 obOracleSessionExtension = new OBOracleSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "123";
 obOracleSessionExtension.killQuery(connection, connectionId);
 // Verify no exception is thrown
 }

 @Test(expected = OdcPluginException.class)
 public void testKillQueryWithInvalidConnection() {
 Connection invalidConnection = null;
 obOracleSessionExtension.killQuery(invalidConnection, "connectionId");
 }

 @Test(expected = OdcPluginException.class)
 public void testKillQueryWithInvalidConnectionId() {
 obOracleSessionExtension.killQuery(connection, "invalidConnectionId");
 }

 @Test
 public void testSwitchSchemaToSameSchema() throws SQLException {
 String schemaName = "testSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
 String invalidSchemaName = "";
 obOracleSessionExtension.switchSchema(connection, invalidSchemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
 assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
 OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
 String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
 assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(null));
 assertEquals(connectionId, obOracleSessionExtension.getConnectionId(oceanBaseConnection));
 }

 @Test
 public void testGetConnectionIdFromOtherConnection() throws SQLException {
 String connectionId = obOracleSessionExtension.getConnectionId(DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", "username", "password"));
 assertNull(connectionId);
 }
}