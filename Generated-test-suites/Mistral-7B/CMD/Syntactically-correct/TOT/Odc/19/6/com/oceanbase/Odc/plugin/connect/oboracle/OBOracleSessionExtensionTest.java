import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.util.ClassPathScanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

 private OBOracleSessionExtension obOracleSessionExtension;
 private ExtensionManager extensionManager;

 @BeforeEach
 public void setup() throws Exception {
 ClassPathScanner classPathScanner = new ClassPathScanner();
 PluginWrapper wrapper = classPathScanner.findExtension(OBOracleSessionExtension.class);
 extensionManager = mock(ExtensionManager.class);
 when(extensionManager.getExtension(OBOracleSessionExtension.class)).thenReturn(wrapper.getLoaded());
 obOracleSessionExtension = new OBOracleSessionExtension();
 obOracleSessionExtension.setExtensionManager(extensionManager);
 }

 @Test
 public void testKillQuery() throws Exception {
 Connection connection = Mockito.mock(Connection.class);
 String connectionId = "123";
 obOracleSessionExtension.killQuery(connection, connectionId);
 Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchema_Success() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 String schemaName = "testSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
 Assertions.assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testSwitchSchema_SameSchema() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 String schemaName = "testSchema";
 String currentSchema = "testSchema";
 obOracleSessionExtension.switchSchema(connection, schemaName);
 String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
 Assertions.assertEquals(currentSchema, newSchema);
 }

 @Test
 public void testGetCurrentSchema_NullConnection() {
 Connection connection = null;
 Assertions.assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(connection));
 }

 @Test
 public void testGetConnectionId_Normal() throws SQLException {
 Connection connection = DriverManager.getConnection("jdbc:obcl:local:1527/oceanbase:10.0.0.1:33060/testDatabase", new Properties());
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 Assertions.assertNotNull(connectionId);
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 Connection connection = Mockito.mock(OceanBaseConnection.class);
 when(connection.getServerThreadId()).thenReturn(1L);
 String connectionId = obOracleSessionExtension.getConnectionId(connection);
 Assertions.assertEquals("1", connectionId);
 }
}