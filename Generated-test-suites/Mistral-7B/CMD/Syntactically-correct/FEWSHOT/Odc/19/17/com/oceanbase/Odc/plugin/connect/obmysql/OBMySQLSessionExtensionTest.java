package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j. Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.io.Factory;
import org.pf4j.util.ClassUtil;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@ Factory
public class OBMySQLSessionExtensionTest {

 private ExtensionFactory extensionFactory;

 @Before
 public void setUp() {
 extensionFactory = new ExtensionFactory();
 }

 @Test
 public void testKillQuery() throws SQLException {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String connectionId = "123";
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 extension.killQuery(connection, connectionId);
 }

 @Test(expected = SQLException.class)
 public void testKillQuery_exception() throws SQLException {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String connectionId = "123";
 when(connection.createStatement()).thenThrow(new SQLException("Error message"));
 extension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String schemaName = "test_schema";
 String currentSchema = "test_schema_current";
 when(connection.getCatalog()).thenReturn(currentSchema);
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 extension.switchSchema(connection, schemaName);
 assertEquals(schemaName, currentSchema);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_exception() throws SQLException, Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String schemaName = "test_schema";
 when(connection.getCatalog()).thenReturn("test_schema_current");
 when(connection.createStatement()).thenThrow(new SQLException("Error message"));
 extension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String querySql = "SELECT DATABASE()";
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connection.createStatement().executeQuery(querySql)).thenReturn(mock(java.sql.ResultSet.class));
 when(connection.createStatement().executeQuery(querySql).next()).thenReturn(true);
 when(connection.createStatement().executeQuery(querySql).getString("DATABASE")).thenReturn("test_schema");
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals("test_schema", currentSchema);
 }

 @Test
 public void testGetCurrentSchema_null() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String querySql = "SELECT DATABASE()";
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connection.createStatement().executeQuery(querySql)).thenReturn(mock(java.sql.ResultSet.class));
 when(connection.createStatement().executeQuery(querySql).next()).thenReturn(false);
 String currentSchema = extension.getCurrentSchema(connection);
 assertNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 String connectionId = "123";
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connection.createStatement().executeQuery(PropertiesUtil.readProperty("ob.connectionIdQuery"))).thenReturn(mock(java.sql.ResultSet.class));
 when(connection.createStatement().executeQuery(PropertiesUtil.readProperty("ob.connectionIdQuery")).next()).thenReturn(true);
 when(connection.createStatement().executeQuery(PropertiesUtil.readProperty("ob.connectionIdQuery")).getString("CONNECTION_ID")).thenReturn(connectionId);
 String id = extension.getConnectionId(connection);
 assertEquals("123", id);
 }

 @Test
 public void testGetConnectionId_oceanbase() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection oceanBaseConnection = mock(OceanBaseConnection.class);
 String connectionId = "123";
 when(oceanBaseConnection.getServerThreadId()).thenReturn(Integer.valueOf(connectionId));
 when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(mock(JdbcOperationsUtil.class));
 when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection).execute("KILL QUERY " + connectionId)).thenReturn(1);
 extension.killQuery(oceanBaseConnection, connectionId);
 }

 @Test
 public void testGetConnectionId_noConnectionIdQuery() throws Exception {
 OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
 Connection connection = mock(Connection.class);
 when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connection.createStatement().executeQuery(PropertiesUtil.readProperty("ob.connectionIdQuery"))).thenReturn(null);
 String connectionId = extension.getConnectionId(connection);
 assertNull(connectionId);
 }
}