import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.util.StringUtils;

public class OBOracleSessionExtensionTest {

private Connection connection;

@Before
public void setUp() throws Exception {
connection = getConnection();
}

@After
public void tearDown() throws Exception {
JdbcOperationsUtil.getJdbcOperations(connection).close();
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "test_connection_id";
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.killQuery(connection, connectionId);
// Add assertions here to check if the query is killed successfully
}

@Test
public void testSwitchSchema() throws SQLException, Exception {
String currentSchema = OBOracleSessionExtension.this.getCurrentSchema(connection);
String schemaName = "test_schema";
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.switchSchema(connection, schemaName);
String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals(schemaName, newSchema);
}

@Test
public void testGetCurrentSchema() {
String currentSchema = OBOracleSessionExtension.this.getCurrentSchema(connection);
assertNotNull(currentSchema);
}

@Test
public void testGetConnectionId() {
String connectionId = OBOracleSessionExtension.this.getConnectionId(connection);
assertNotNull(connectionId);
}

@Test(expected = SQLException.class)
public void testKillQuery_NullConnection() {
Connection connection = null;
String connectionId = "test_connection_id";
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.killQuery(connection, connectionId);
}

@Test(expected = SQLException.class)
public void testSwitchSchema_NullConnection() {
Connection connection = null;
String schemaName = "test_schema";
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.switchSchema(connection, schemaName);
}

@Test(expected = SQLException.class)
public void testSwitchSchema_NullSchemaName() {
Connection connection = getConnection();
String schemaName = null;
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.switchSchema(connection, schemaName);
}

@Test(expected = SQLException.class)
public void testSwitchSchema_SchemaNotExist() throws SQLException {
Connection connection = getConnection();
String schemaName = "test_schema_not_exist";
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
obOracleSessionExtension.switchSchema(connection, schemaName);
}

@Test(expected = SQLException.class)
public void testGetCurrentSchema_NullConnection() {
Connection connection = null;
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertNull(currentSchema);
}

@Test(expected = SQLException.class)
public void testGetConnectionId_NullConnection() {
Connection connection = null;
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
String connectionId = obOracleSessionExtension.getConnectionId(connection);
}

@Test
public void testGetConnectionId_ConnectionDoesntSupportGettingServerThreadId() {
Connection connection = getConnection(); // Assuming you have a method to get a connection that doesn't support getting server thread id
OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertEquals("", connectionId);
}
}