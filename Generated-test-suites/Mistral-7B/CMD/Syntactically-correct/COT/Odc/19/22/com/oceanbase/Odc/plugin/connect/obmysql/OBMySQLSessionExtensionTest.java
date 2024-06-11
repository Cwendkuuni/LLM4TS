import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointManager;
import org.pf4j.util.ClasspathScanner;
import org.pf4j.util.PropertiesUtil;
import org.powermock.api.mockito.annotation.Mockanno;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({OBMySQLSessionExtension.class, DefaultExtensionPointManager.class, DefaultExtensionPoint.class})
public class OBMySQLSessionExtensionTest {

    @Mockanno(clazz = Connection.class)
    Connection connectionMock;

    @Mockanno(clazz = DefaultExtensionManager.class)
    DefaultExtensionManager extensionManagerMock;

    @Mockanno(clazz = DefaultExtensionPointManager.class)
    DefaultExtensionPointManager extensionPointManagerMock;

    @Mockanno(clazz = DefaultExtensionPoint.class)
    DefaultExtensionPoint extensionPointMock;

    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("extension.class", OBMySQLSessionExtension.class.getName());
        PropertiesUtil.setProperties(PropertiesUtil.class, properties);
        ClasspathScanner classpathScanner = new ClasspathScanner();
        DefaultExtensionManager extensionManager = classpathScanner.getExtensions(Extension.class);
        when(extensionManagerMock.getExtensionPointManager()).thenReturn(extensionPointManagerMock);
        when(extensionPointManagerMock.getExtensionPoint(SessionExtensionPoint.class)).thenReturn(extensionPointMock);
        when(extensionPointMock.getExtensions()).thenReturn(extensionManager);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.init(extensionManager);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "testConnectionId";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.setAutoCommit(false)).thenReturn(true);
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
        assertEquals("KILL QUERY " + connectionId, JdbcOperationsUtil.getJdbcOperations(connectionMock).queryExecuted);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_ConnectionClosed() throws Exception {
        when(connectionMock.createStatement()).thenThrow(new SQLException("Connection is closed."));
        obMySQLSessionExtension.killQuery(connectionMock, "testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "testSchemaName";
        String currentSchema = "testCurrentSchema";
        when(connectionMock.getCatalog()).thenReturn(currentSchema);
        when(connectionMock.setCatalog(schemaName)).thenReturn(true);
        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
        assertEquals(schemaName, connectionMock.getCatalog());
        assertNull(connectionMock.getSchema());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_CurrentSchemaEquals() throws Exception {
        String schemaName = "testSchemaName";
        when(connectionMock.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testSwitchSchema_ConnectionClosed() throws Exception {
        when(connectionMock.getCatalog()).thenThrow(new SQLException("Connection is closed."));
        obMySQLSessionExtension.switchSchema(connectionMock, "testSchemaName");
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        String schemaName = "testSchemaName";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.query(Mockito.eq("SELECT DATABASE()"))).thenReturn(mock(java.sql.ResultSet.class));
        when(connectionMock.query(Mockito.eq("SELECT DATABASE()"))).thenReturn(mock(java.sql.ResultSet.class)).thenReturn(null);
        when(connectionMock.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(schemaName);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        String connectionId = "testConnectionId";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.queryForObject(Mockito.eq("select connection_id()"), Mockito.eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionId_ConnectionClosed() throws Exception {
        when(connectionMock.createStatement()).thenThrow(new SQLException("Connection is closed."));
        when(connectionMock.getInstance(OceanBaseConnection.class)).thenReturn(connectionMock);
        String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertEquals(connectionMock.getServerThreadId() + "", connectionId);
    }

    @Test
    public void testGetConnectionId_NoQuerySupport() throws Exception {
        when(connectionMock.createStatement()).thenThrow(new SQLFeatureNotSupportedException());
        assertEquals(null, obMySQLSessionExtension.getConnectionId(connectionMock));
    }
}