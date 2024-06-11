import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.RestorableDatabaseRule;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.impl.DefaultServiceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Rule
    public RestorableDatabaseRule databaseRule = new RestorableDatabaseRule();

    private Connection connection;
    private ExtensionManager extensionManager;
    private DefaultServiceFactory serviceFactory;
    private DefaultExtensionRegistry extensionRegistry;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", "root", "password");

        serviceFactory = new DefaultServiceFactory();
        extensionRegistry = new DefaultExtensionRegistry();
        extensionManager = new ExtensionManager(Collections.emptyList(), extensionRegistry, serviceFactory);
        extensionRegistry.registerExtensionPoint(SessionExtensionPoint.class);
    }

    @After
    public void tearDown() throws Exception {
        if (null != connection) {
            connection.close();
        }

        if (extensionManager != null) {
            extensionManager.shutdown();
        }

        databaseRule.restore();
    }

    @org.junit.Test
    public void testKillQuery() throws SQLException {
        String connectionId = getConnectionId(connection);
        extensionManager.loadExtension(OBMySQLSessionExtension.class);
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        SessionExtensionPoint sessionExtensionPoint = (SessionExtensionPoint) extensionPoint;
        sessionExtensionPoint.killQuery(connection, connectionId);

        String querySql = "SELECT COUNT(*) FROM information_schema.processlist WHERE PROCESSID IN (SELECT PROCESSID FROM information_schema.processlist WHERE INFO = 'query id = " + connectionId + "')";
        int count = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, Integer.class);
        assertEquals(0, count);
    }

    @org.junit.Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        extensionManager.loadExtension(OBMySQLSessionExtension.class);
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        SessionExtensionPoint sessionExtensionPoint = (SessionExtensionPoint) extensionPoint;
        sessionExtensionPoint.switchSchema(connection, schemaName);

        String currentSchema = getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @org.junit.Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = getCurrentSchema(connection);
        String schemaName = "test_schema";
        extensionManager.loadExtension(OBMySQLSessionExtension.class);
        ExtensionPoint extensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class).get(0);
        SessionExtensionPoint sessionExtensionPoint = (SessionExtensionPoint) extensionPoint;
        sessionExtensionPoint.switchSchema(connection, schemaName);

        String newCurrentSchema = getCurrentSchema(connection);
        assertEquals(schemaName, newCurrentSchema);

        sessionExtensionPoint.switchSchema(connection, null);

        String newCurrentSchema2 = getCurrentSchema(connection);
        assertEquals(currentSchema, newCurrentSchema2);
    }

    @org.junit.Test
    public void testGetConnectionId() throws SQLException {
        String connectionId1 = getConnectionId(connection);
        String connectionId2 = getConnectionId(DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", "root", "password"));

        assertEquals(connectionId1, connectionId1);
        assertNotEquals(connectionId1, connectionId2);
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
    }

    private String getConnectionId(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select connection_id()");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}