package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.StringJoiner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.Extensible;
import org.pf4j.PluginWrapper;
import org.pf4j.util.ClasspathScanner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBOracleSessionExtensionTest {

    private static final String CLASSPATH = System.getProperty("classpath");
    private static final String DRIVER_CLASS_NAME = "com.oceanbase.jdbc.OceanBaseDriver";
    private static final String URL = "jdbc:obnew:ob://localhost:28810/oceanbase:1521/orcl:1521@127.0.0.1:1521/orcl:1521";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() throws Exception {
        ClasspathScanner scanner = new ClasspathScanner();
        PluginWrapper wrapper = scanner.findExtension(Extension.class, OBOracleSessionExtension.class.getPackage().getName());
        ExtensionManager manager = new ExtensionManager(wrapper);
        manager.init();
        obOracleSessionExtension = (OBOracleSessionExtension) manager.getInstance(OBOracleSessionExtension.class);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        obOracleSessionExtension.killQuery(connection, connectionId);
        assertFalse(connection.isOpen());
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithWrongId() throws SQLException {
        String connectionId = "wrong_id";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String schemaName = "123456";
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
        if (!(connection instanceof OceanBaseConnection)) {
            throw new SQLException("Connection is not an instance of OceanBaseConnection.");
        }
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        String id = ((OceanBaseConnection) connection).getServerThreadId();
        assertEquals(id, connectionId);
    }
}