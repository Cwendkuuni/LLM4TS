import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.pf4j.ExtensionManager;
import org.pf4j.DefaultExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.Extensions;
import org.pf4j.PluginWrapper.Builder;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

 private ExtensionManager extensionManager;
 private OBMySQLSessionExtension obMySQLSessionExtension;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 extensionManager = new DefaultExtensionManager();
 PluginWrapper wrapper = new Builder("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension")
 .setClassLoader(getClass().getClassLoader())
 .build();
 extensionManager.addExtension(Extensions.newExtension(wrapper));
 obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension("OBMySQLSessionExtension");
 }

 @After
 public void tearDown() {
 extensionManager.shutdown();
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 String connectionId = "test_connection_id";
 obMySQLSessionExtension.killQuery(connection, connectionId);
 // You can write assertions or verification here if needed
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 String schemaName = "test_schema";
 obMySQLSessionExtension.switchSchema(connection, schemaName);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testGetCurrentSchema() {
 Connection connection = Mockito.mock(Connection.class);
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertNotNull(currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = Mockito.mock(Connection.class);
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNotNull(connectionId);
 }

}