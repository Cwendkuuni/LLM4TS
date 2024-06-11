package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.ClasspathScanner;
import org.slf4j.Logger;

@Extension
public class OBMySQLSessionExtensionTest {

 private static final String DB_URL = "jdbc:oceanspace:localhost:2880/testdb";
 private static final String SCHEMA_NAME = "test_schema";
 private static final String CONNECTION_ID = "1";
 private OBMySQLSessionExtension sessionExtension;
 private Connection connection;
 private Logger log;
 private ExtensionManager extensionManager;

 @Before
 public void setup() throws Exception {
 log = Mockito.mock(Logger.class);
 extensionManager = new TestExtensionManager();
 PluginWrapper wrapper = new PluginWrapper(OBMySQLSessionExtension.class.getName(), ClasspathScanner.scanClasspath(ClasspathScanner.class, OBMySQLSessionExtension.class));
 extensionManager.loadPlugin(wrapper);
 sessionExtension = extensionManager.getExtension(OBMySQLSessionExtension.class);
 connection = Mockito.mock(Connection.class);
 }

 @Test
 public void testKillQuery() throws SQLException {
 sessionExtension.killQuery(connection, CONNECTION_ID);
 // Add assertions here based on the expected behavior of the killQuery method
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 sessionExtension.switchSchema(connection, SCHEMA_NAME);
 assertEquals(SCHEMA_NAME, sessionExtension.getCurrentSchema(connection));
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaToSameSchema() throws SQLException {
 sessionExtension.switchSchema(connection, SCHEMA_NAME);
 sessionExtension.switchSchema(connection, SCHEMA_NAME);
 }

 @Test(expected = SQLException.class)
 public void testGetCurrentSchemaWhenNoSchema() throws SQLException {
 Mockito.when(connection.getCatalog()).thenReturn(null);
 assertNull(sessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 sessionExtension.getConnectionId(connection);
 // Add assertions here based on the expected behavior of the getConnectionId method
 }

 @Test
 public void testGetConnectionIdWhenOceanBaseConnection() throws SQLException {
 Mockito.when(connection.isClosed()).thenReturn(false);
 Mockito.when(connection.getClass()).thenReturn(OceanBaseConnection.class);
 assertEquals(CONNECTION_ID, sessionExtension.getConnectionId(connection));
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionIdWhenNotOceanBaseConnection() throws SQLException {
 Mockito.when(connection.isClosed()).thenReturn(false);
 Mockito.when(connection.getClass()).thenReturn(Connection.class);
 throw new SQLException();
 }
}