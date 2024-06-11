import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.util.ClasspathScanner;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class OBMySQLSessionExtensionTest {

 private Connection connection;
 private SessionExtensionPoint extension;
 private JdbcOperationsUtil jdbcOperationsUtil;
 private ExtensionManager extensionManager;

 @Before
 public void setup() {
 connection = mock(OceanBaseConnection.class);
 extension = new OBMySQLSessionExtension();
 jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
 extensionManager = mock(ExtensionManager.class);
 ClasspathScanner scanner = new ClasspathScanner();
 scanner.scanAnnotatedClasses(SessionExtensionPoint.class.getPackage().getName(), Extension.class);
 extensionManager.loadExtensions(scanner.getExtensions());
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "test_connection_id";
 extension.killQuery(connection, connectionId);
 Mockito.verify(jdbcOperationsUtil).execute(eq(connection), eq("KILL QUERY " + connectionId));
 }

 @Test
 public void testSwitchSchema_sameSchema() throws SQLException {
 String schemaName = "test_schema";
 extension.switchSchema(connection, schemaName);
 Mockito.verifyNoMoreInteractions(jdbcOperationsUtil);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_differentSchema_sqlException() throws SQLException {
 String schemaName = "test_schema";
 Mockito.when(jdbcOperationsUtil.queryForObject(eq(connection), eq("SELECT 1"))).thenThrow(new SQLException("Test SQLException"));
 extension.switchSchema(connection, schemaName);
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchSchema_differentSchema_illegalStateException() throws SQLException {
 String schemaName = "test_schema";
 Mockito.when(jdbcOperationsUtil.queryForObject(eq(connection), eq("SELECT DATABASE()"))).thenReturn("old_schema");
 extension.switchSchema(connection, schemaName);
 Mockito.verify(connection).setCatalog(schemaName);
 Mockito.verify(connection).setCatalog("old_schema"); // IllegalStateException should be thrown here
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String currentSchema = extension.getCurrentSchema(connection);
 Mockito.verify(jdbcOperationsUtil).queryForObject(eq(connection), eq("SELECT DATABASE()"));
 assertEquals("test_schema", currentSchema);
 }

 @Test
 public void testGetConnectionId_default() throws SQLException {
 String connectionId = extension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionId_oceanbaseConnection() {
 String connectionId = extension.getConnectionId(connection);
 if (connection instanceof OceanBaseConnection) {
 assertEquals(((OceanBaseConnection) connection).getServerThreadId(), connectionId);
 }
 }
}