package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("static-access")
public class OBMySQLSessionExtensionTest {

    @org.junit.Before
    public void setUp() throws Exception {
        Class<?> clazz = Class.forName("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        PluginWrapper pluginWrapper = new PluginWrapper(clazz);
        Extension extension = (Extension) pluginWrapper.getLoadedInstance();
        OBMySQLSessionExtension obMySQLSessionExtension = (OBMySQLSessionExtension) extension;
        obMySQLSessionExtension.setUp();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.tearDown();
    }

    @org.junit.Test
    public void testKillQuery() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        assertEquals("KILL QUERY testConnectionId", JdbcOperationsUtil.getLastQueryString(connection));
    }

    @org.junit.Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, "nonExistentConnectionId");
    }

    @org.junit.Test
    public void testSwitchSchema() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        assertEquals("testSchema", connection.getCatalog());
    }

    @org.junit.Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, "nonExistentSchema");
    }

    @org.junit.Test
    public void testGetCurrentSchema() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testDB", currentSchema);
    }

    @org.junit.Test
    public void testGetConnectionId() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = DriverManager.getConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1", connectionId);
    }

    @org.junit.Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String url = "jdbc:oceanbase:observer:127.0.0.1:4000/root:root@/testDB";
        Properties props = new Properties();
        Connection connection = new OceanBaseConnection(url, props);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("threadId", connectionId);
    }
}