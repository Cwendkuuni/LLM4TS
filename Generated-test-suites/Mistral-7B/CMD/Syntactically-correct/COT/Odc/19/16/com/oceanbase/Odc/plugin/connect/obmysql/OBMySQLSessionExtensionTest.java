import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private JdbcOperations jdbcOperations;
 private SessionExtensionPoint obMySQLSessionExtension;

 @Before
 public void setUp() {
 jdbcOperations = Mockito.mock(JdbcOperations.class);
 connection = Mockito.mock(Connection.class);
 obMySQLSessionExtension = new OBMySQLSessionExtension() {
 {
 setJdbcOperations(jdbcOperations);
 }
 };
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_connection_id";
 when(jdbcOperations.execute(eq("KILL QUERY " + connectionId))).thenReturn(1);
 obMySQLSessionExtension.killQuery(connection, connectionId);
 verify(jdbcOperations).execute(eq("KILL QUERY " + connectionId));
 }

 @Test(expected = SQLException.class)
 public void testKillQueryWithException() throws SQLException {
 when(jdbcOperations.execute(eq("KILL QUERY test_connection_id"))).thenThrow(new SQLException());
 obMySQLSessionExtension.killQuery(connection, "test_connection_id");
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 when(connection.getCatalog()).isInstanceOf(String.class);
 doReturn("current_schema").when(connection).getCatalog();
 when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(schemaName);
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 verify(connection).setCatalog(eq(schemaName));
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchemaWithException() throws SQLException {
 String schemaName = "test_schema";
 doReturn("current_schema").when(connection).getCatalog();
 when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn("different_schema");
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 verify(jdbcOperations, times(2)).queryForObject(anyString(), eq(String.class));
 verify(connection).setCatalog(eq(schemaName));
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn("test_schema");
 assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 when(connection.getCatalog()).isInstanceOf(String.class);
 doReturn("current_catalog").when(connection).getCatalog();
 doReturn("test_connection_id").when(connection).getConnectionId();
 assertEquals("test_connection_id", obMySQLSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
 when(connection.getCatalog()).isInstanceOf(String.class);
 doReturn("current_catalog").when(connection).getCatalog();
 doReturn(1234567890L).when(connection).getServerThreadId();
 assertEquals("1234567890", obMySQLSessionExtension.getConnectionId(connection));
 }

 @Test
 public void testGetConnectionIdWithoutConnectionId() {
 assertEquals(null, obMySQLSessionExtension.getConnectionId(connection));
 }
}