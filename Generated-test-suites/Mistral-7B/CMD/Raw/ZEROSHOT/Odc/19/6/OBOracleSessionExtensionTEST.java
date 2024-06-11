package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private JdbcOperationsUtil jdbcOperationsUtil;
 @InjectMocks
 private OBOracleSessionExtension obOracleSessionExtension;

 @Before
 public void setup() {
 when(jdbcOperationsUtil.getJdbcOperations(anyConnection())).thenReturn(jdbcOperations);
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = mock(Connection.class);
 obOracleSessionExtension.killQuery(connection, "testConnectionId");
 assertEquals("KILL QUERY testConnectionId", jdbcOperations.execute(anyString()));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = mock(Connection.class);
 when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn("currentSchema");
 obOracleSessionExtension.switchSchema(connection, "newSchema");
 assertEquals("ALTER SESSION SET CURRENT_SCHEMA=newSchema", jdbcOperations.update(anyString()));
 }

 @Test
 public void testSwitchSchemaWithSameSchema() throws SQLException {
 Connection connection = mock(Connection.class);
 when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn("currentSchema");
 obOracleSessionExtension.switchSchema(connection, "currentSchema");
 assertNotNull(obOracleSessionExtension);
 }

 @Test(expected = SQLException.class)
 public void testGetCurrentSchemaNullConnection() throws SQLException {
 obOracleSessionExtension.getCurrentSchema(null);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = mock(Connection.class);
 when(jdbcOperations.queryForObject(anyString(), anyString())).thenReturn("currentSchema");
 assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = mock(Connection.class);
 when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
 assertEquals("1", obOracleSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
 Connection oceanBaseConnection = mock(OceanBaseConnection.class);
 when(jdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
 assertEquals("123456789", obOracleSessionExtension.getConnectionId(oceanBaseConnection));
 }

 @Test
 public void testGetConnectionIdNullConnection() throws SQLException {
 obOracleSessionExtension.getConnectionId(null);
 }
}