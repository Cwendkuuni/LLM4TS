package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBOracleSessionExtensionTest {

 private SessionExtensionPoint obOracleSessionExtension;
 private Connection connectionMock;

 @Before
 public void setUp() {
 connectionMock = mock(Connection.class);
 obOracleSessionExtension = new OBOracleSessionExtension();
 when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connectionMock.getSchema()).thenReturn("test_schema");
 when(connectionMock.getAutoCommit()).thenReturn(true);
 when(connectionMock.isClosed()).thenReturn(false);
 when(connectionMock.isClosed()).thenAnswer(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 JdbcOperations util = JdbcOperationsUtil.getJdbcOperations(connectionMock);
 util.execute("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
 return false;
 }
 });
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_connection_id";
 obOracleSessionExtension.killQuery(connectionMock, connectionId);
 verifyQuery("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchemaSuccess() throws SQLException {
 String schemaName = "test_schema_2";
 obOracleSessionExtension.switchSchema(connectionMock, schemaName);
 verifyQuery("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaFailed() throws SQLException {
 String schemaName = null;
 obOracleSessionExtension.switchSchema(connectionMock, schemaName);
 verifyQuery("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String expectedSchema = "test_schema";
 when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().next()).thenReturn(true);
 when(connectionMock.createStatement().next()).thenReturn(false);
 String actualSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = "test_connection_id";
 when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().next()).thenReturn(true);
 when(connectionMock.createStatement().next()).thenReturn(false);
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
 when(connectionMock.createStatement().next()).thenReturn(false);
 when(connectionMock.createStatement().next()).thenReturn(true);
 when(connectionMock.createStatement().next()).thenReturn(false);
 when(connectionMock.createStatement().next()).thenReturn(true);
 when(connectionMock.createStatement().next()).thenReturn(false);
 when(connectionMock.createStatement().next()).thenReturn(true);
 when(connectionMock.createStatement().next()).thenReturn(false);
 when(connectionMock.createStatement().next()).thenReturn(true);
 String actualConnectionId = obOracleSessionExtension.getConnectionId(connectionMock);
 assertEquals(connectionId, actualConnectionId);
 }

 @Test
 public void testGetConnectionIdWithoutQuery() {
 String connectionId = "test_connection_id";
 when(connectionMock.createStatement()).thenReturn(null);
 assertNull(obOracleSessionExtension.getConnectionId(connectionMock));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() {
 Connection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(oceanBaseConnection.getServerThreadId()).thenReturn("test_thread_id");
 String actualConnectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
 assertEquals("test_thread_id", actualConnectionId);
 }

 private void verifyQuery(String query) throws SQLException {
 JdbcOperations util = JdbcOperationsUtil.getJdbcOperations(connectionMock);
 util.update(query);
 }

}