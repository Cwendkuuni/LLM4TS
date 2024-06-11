package com.oceanbase.odc.plugin.connect.obmysql.test;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.OceanBaseConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private OBMySQLSessionExtension extension;
 private ExtensionManager extensionManager;

 @Before
 public void setup() throws SQLException, ClassNotFoundException {
 Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
 Properties props = new Properties();
 props.setProperty("user", "user");
 props.setProperty("password", "password");
 props.setProperty("url", "jdbc:oceanbase:localhost:2880/testdb");
 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
 extensionManager = new ExtensionManager(OBMySQLSessionExtension.class.getPackage().getName(), "META-INF/extensions.xml");
 extension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class)[0];
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = extension.getConnectionId(connection);
 extension.killQuery(connection, connectionId);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(currentSchema, extension.getCurrentSchema(connection));
 }

 @Test(expected = SQLException.class)
 public void testKillQueryWithInvalidConnectionId() throws SQLException {
 String connectionId = "invalid_id";
 assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 extension.switchSchema(connection, schemaName);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
 String schemaName = "";
 assertThrows(SQLException.class, () -> extension.switchSchema(connection, schemaName));
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(currentSchema, extension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = extension.getConnectionId(connection);
 assertEquals(connectionId, extension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
 connection.close();
 OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) DriverManager.getConnection("jdbc:oceanbase:localhost:2880/testdb", "user", "password");
 String connectionId = extension.getConnectionId(oceanBaseConnection);
 assertEquals(connectionId, oceanBaseConnection.getServerThreadId() + "");
 }
}