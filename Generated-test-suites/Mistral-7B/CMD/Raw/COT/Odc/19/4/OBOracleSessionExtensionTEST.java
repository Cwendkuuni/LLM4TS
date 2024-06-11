package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private Connection connection;

 @Mock
 private JdbcOperations jdbcOperations;

 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtension;

 @Test
 public void testKillQuery() {
 JdbcOperationsUtil.setJdbcOperations(this.jdbcOperations);
 when(this.connection.createStatement()).thenReturn(this.jdbcOperations.createStatement());
 obOracleSessionExtension.killQuery(this.connection, "connectionId");
 // Add assertions here to verify the functionality of the killQuery method
 }

 @Test
 public void testSwitchSchema_Success() throws SQLException {
 String newSchema = "testSchema";
 when(this.connection.createStatement()).thenReturn(this.jdbcOperations.createStatement());
 when(this.jdbcOperations.update(anyString())).thenReturn(1);
 obOracleSessionExtension.switchSchema(this.connection, newSchema);
 // Add assertions here to verify the functionality of the switchSchema method
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_Exception() throws SQLException {
 String newSchema = "testSchema";
 when(this.connection.createStatement()).thenThrow(new SQLException("Test Exception"));
 obOracleSessionExtension.switchSchema(this.connection, newSchema);
 // Add assertions here to verify the exception handling of the switchSchema method
 }

 @Test
 public void testGetCurrentSchema_Success() {
 when(this.jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("currentSchema");
 assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(this.connection));
 }

 @Test(expected = SQLException.class)
 public void testGetCurrentSchema_Exception() {
 when(this.jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException("Test Exception"));
 assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(this.connection));
 }

 @Test
 public void testGetConnectionId_Success() throws SQLException {
 when(this.jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("connectionId");
 assertEquals("connectionId", obOracleSessionExtension.getConnectionId(this.connection));
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 when(this.connection instanceof OceanBaseConnection).thenReturn(true);
 when(this.connection.getServerThreadId()).thenReturn("threadId");
 assertEquals("threadId", obOracleSessionExtension.getConnectionId(this.connection));
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionId_Exception() throws SQLException {
 when(this.jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException("Test Exception"));
 assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(this.connection));
 }
}