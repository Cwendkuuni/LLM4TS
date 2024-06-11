import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private OBMySQLSessionExtension obMySQLSessionExtension;
 private JdbcOperationsUtil jdbcOperationsUtilMock;

 @Before
 public void setUp() {
 connection = Mockito.mock(Connection.class);
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 jdbcOperationsUtilMock = Mockito.mock(JdbcOperationsUtil.class);
 when(jdbcOperationsUtilMock.getJdbcOperations(connection)).thenReturn(new JdbcOperationsUtil() {
 @Override
 public void execute(String sql) throws SQLException {
 // Do nothing
 }
 });
 obMySQLSessionExtension.setJdbcOperationsUtil(jdbcOperationsUtilMock);
 }

 @Test
 public void testKillQuerySuccess() throws SQLException {
 String connectionId = "connection_123";
 obMySQLSessionExtension.killQuery(connection, connectionId);
 verify(connection).createStatement();
 verify(connection).executeUpdate("KILL QUERY " + connectionId);
 }

 @Test(expected = SQLException.class)
 public void testKillQueryFailure() throws SQLException {
 String connectionId = "connection_123";
 when(connection.createStatement()).thenThrow(new SQLException());
 obMySQLSessionExtension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchemaSameSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = schemaName;
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 verify(connection, never()).setCatalog(schemaName);
 }

 @Test
 public void testSwitchSchemaDifferentSchema() throws SQLException {
 String schemaName = "test_schema_2";
 String currentSchema = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 verify(connection).setCatalog(schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaFailure() throws SQLException {
 String schemaName = "test_schema";
 when(connection.createStatement()).thenThrow(new SQLException());
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String expectedSchema = "test_schema";
 when(jdbcOperationsUtilMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(expectedSchema);
 String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(expectedSchema, actualSchema);
 }

 @Test
 public void testGetConnectionIdNull() throws SQLException {
 when(connection.createStatement()).thenThrow(new SQLException());
 String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNull(actualConnectionId);
 }

 @Test
 public void testGetConnectionIdOceanBaseConnection() throws SQLException {
 String expectedConnectionId = "12345";
 when(connection instanceof OceanBaseConnection).thenReturn(true);
 String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertEquals(expectedConnectionId, actualConnectionId);
 }

 @Test
 public void testGetConnectionIdOtherConnection() throws SQLException {
 String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNull(actualConnectionId);
 }
}