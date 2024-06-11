package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint obMySQLSessionExtension;

 @Before
 public void setUp() throws Exception {
 Connection dbConnection = DriverManager.getConnection("jdbc:obmysql://localhost:3306/testdb", "root", "password");
 connection = dbConnection.createStatement().executeQuery("create extension if not exists pf4j").getConnection();
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 }

 @After
 public void tearDown() throws Exception {
 connection.close();
 }

 @Test
 public void testKillQuery() {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 obMySQLSessionExtension.killQuery(connection, connectionId);
 assertThrows(SQLException.class, () -> obMySQLSessionExtension.killQuery(connection, connectionId));
 }

 @Test
 public void testKillQuery_Success() {
 String connectionId1 = obMySQLSessionExtension.getConnectionId(connection);
 String connectionId2 = obMySQLSessionExtension.getConnectionId(connection);
 obMySQLSessionExtension.killQuery(connection, connectionId1);
 assertEquals(connectionId1, obMySQLSessionExtension.getConnectionId(connection));
 assertEquals(connectionId2, obMySQLSessionExtension.getConnectionId(connection));
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_WrongSchema() throws SQLException {
 obMySQLSessionExtension.switchSchema(connection, "nonexistent_schema");
 }

 @Test
 public void testSwitchSchema_Success() throws SQLException {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 obMySQLSessionExtension.switchSchema(connection, "testdb");
 assertEquals("testdb", obMySQLSessionExtension.getCurrentSchema(connection));
 obMySQLSessionExtension.switchSchema(connection, currentSchema);
 assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
 }

 @Test(expected = SQLException.class)
 public void testGetCurrentSchema_NoConnection() {
 assertThrows(SQLException.class, () -> obMySQLSessionExtension.getCurrentSchema(null));
 }

 @Test
 public void testGetCurrentSchema_Success() throws SQLException {
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("testdb", currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionId_NoConnection() {
 assertThrows(SQLException.class, () -> obMySQLSessionExtension.getConnectionId(null));
 }

 @Test
 public void testGetConnectionId_Success() throws SQLException {
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertEquals(connectionId.length() > 0, true);
 }
}