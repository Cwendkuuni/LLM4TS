package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@org.mockito.MockAnnotations.InitMocks
public class OBMySQLSessionExtensionTest {

 private SessionExtensionPoint obMySQLSessionExtension;
 private Connection connectionMock;

 @Before
 public void setUp() {
 connectionMock = Mockito.mock(Connection.class);
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "testConnectionId";
 obMySQLSessionExtension.killQuery(connectionMock, connectionId);
 Mockito.verify(connectionMock).createStatement();
 Mockito.verify(connectionMock).execute("KILL QUERY " + connectionId);
 }

 @Test(expected = SQLException.class)
 public void testKillQueryWithException() throws SQLException {
 String connectionId = "testConnectionId";
 Mockito.when(connectionMock.createStatement()).thenThrow(new SQLException("Error creating statement"));
 obMySQLSessionExtension.killQuery(connectionMock, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "testSchema";
 obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
 Mockito.verify(connectionMock).setCatalog(schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithException() throws SQLException {
 String schemaName = "testSchema";
 Mockito.when(connectionMock.setCatalog(schemaName)).thenThrow(new SQLException("Error setting catalog"));
 obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String expectedSchema = "testSchema";
 Mockito.when(connectionMock.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
 String actualSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 String connectionId = "testConnectionId";
 Mockito.when(connectionMock.queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
 String actualConnectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
 assertEquals(connectionId, actualConnectionId);
 }

 @Test
 public void testGetConnectionIdWhenNoQuerySupported() throws SQLException {
 OceanBaseConnection oceanBaseConnectionMock = Mockito.mock(OceanBaseConnection.class);
 Mockito.when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(123);
 String actualConnectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnectionMock);
 assertEquals("123", actualConnectionId);
 }
}