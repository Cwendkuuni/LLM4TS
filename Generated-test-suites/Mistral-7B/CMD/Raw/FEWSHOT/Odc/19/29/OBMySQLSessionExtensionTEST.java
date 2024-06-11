package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.util.ClassUtil;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private ExtensionManager extensionManager;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        extensionManager = mock(ExtensionManager.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        ClassUtil.registerExtension(extensionManager, obMySQLSessionExtension.getClass());
        connection = DriverManager.getConnection("jdbc:mock:mydb");
    }

    @Test
    public void testKillQuery() throws Exception {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(obMySQLSessionExtension.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute(eq("KILL QUERY 12345"))).thenReturn(1);

        obMySQLSessionExtension.killQuery(connection, "12345");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws Exception {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(obMySQLSessionExtension.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute(eq("KILL QUERY 12345"))).thenThrow(new SQLException());

        obMySQLSessionExtension.killQuery(connection, "12345");
    }

    @Test
    public void testSwitchSchema() throws Exception {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute(connection, eq("SELECT DATABASE()"))).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("testSchema");
        when(oceanBaseConnection.setCatalog(eq("testSchema"))).thenReturn(true);

        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws Exception {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute(connection, eq("SELECT DATABASE()"))).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException());

        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(obMySQLSessionExtension.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn("testSchema");

        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() throws Exception {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
}