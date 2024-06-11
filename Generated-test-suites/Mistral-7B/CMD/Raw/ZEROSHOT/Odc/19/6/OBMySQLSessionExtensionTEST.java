import org.junit.Before;
import org.junit.Extension;
import org.junit.Test;
import org.pf4j.ExtensionFactory;
import org.powermock.api.mockito.annotates.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@PrepareForTest(OBMySQLSessionExtension.class)
@Extension
public class OBMySQLSessionExtensionTest {

 private OBMySQLSessionExtension obMySQLSessionExtension;
 private Connection connectionMock;

 @Before
 public void setUp() throws Exception {
 connectionMock = mock(Connection.class);
 obMySQLSessionExtension = new OBMySQLSessionExtension();
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testKillQuery() throws SQLException {
 String connectionId = "connection_id_1";
 when(connectionMock.createStatement()).thenReturn(mock(Statement.class));
 obMySQLSessionExtension.killQuery(connectionMock, connectionId);
 verify(connectionMock.createStatement()).execute("KILL QUERY " + connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = "current_schema";
 when(connectionMock.getCatalog()).thenReturn(currentSchema);
 obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
 verify(connectionMock).setCatalog(schemaName);
 }

 @Test(expected = SQLException.class)
 public void testSwitchSchema_sameSchema() throws SQLException {
 String schemaName = "test_schema";
 String currentSchema = "test_schema";
 when(connectionMock.getCatalog()).thenReturn(currentSchema);
 obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
 verifyZeroInteractions(connectionMock);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 String expectedSchema = "test_schema";
 when(connectionMock.createStatement()).thenReturn(mock(Statement.class));
 when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(ResultSet.class));
 when(connectionMock.createStatement().executeQuery(anyString()).next()).thenReturn(true);
 when(connectionMock.createStatement().executeQuery(anyString()).getString("DATABASE_NAME")).thenReturn(expectedSchema);
 assertEquals(expectedSchema, obMySQLSessionExtension.getCurrentSchema(connectionMock));
 }

 @Test
 public void testGetConnectionId_OceanBaseConnection() throws SQLException {
 when(connectionMock.createStatement()).thenThrow(new SQLException());
 when(connectionMock.getMetaData()).thenReturn(mock(DatabaseMetaData.class));
 when(connectionMock.getMetaData().getDatabaseProductName()).thenReturn("OceanBase");
 String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
 assertEquals("connection_id", connectionId);
 }

 @Test
 public void testGetConnectionId_OtherDatabase() throws SQLException {
 when(connectionMock.createStatement()).thenThrow(new SQLException());
 when(connectionMock.getMetaData()).thenReturn(mock(DatabaseMetaData.class));
 when(connectionMock.getMetaData().getDatabaseProductName()).thenReturn("MySQL");
 String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
 assertNull(connectionId);
 }

 @Test
 public void testGetConnectionId_NoMethod() throws SQLException {
 when(connectionMock.createStatement()).thenThrow(new SQLException());
 when(connectionMock.getMetaData()).thenReturn(mock(DatabaseMetaData.class));
 when(connectionMock.getMetaData().getDatabaseProductName()).thenReturn(null);
 String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
 assertNull(connectionId);
 }
}