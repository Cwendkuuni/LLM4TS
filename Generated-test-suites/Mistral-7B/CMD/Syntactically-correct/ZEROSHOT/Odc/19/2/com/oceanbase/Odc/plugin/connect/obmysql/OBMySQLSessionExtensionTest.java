package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.test.api.ExtensionTest;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@Extension
public class OBMySQLSessionExtensionTest extends ExtensionTest {

    private Connection connection;
    private SessionExtensionPoint obMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configure("log4j.xml");
        System.setProperty("logback.configurationFile", "logback.xml");
        Class.forName("com.oceanbase.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:obcds://localhost:28810/TestDB", "user", "password");
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        registerExtension(obMySQLSessionExtension);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TestSchema";
        String currentSchema = "TestDB";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
        verify(connection).createStatement();
        verify(connection.createStatement()).execute(eq("SELECT DATABASE()"));
        String newSchema = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
        assertEquals(schemaName, newSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "TestDB";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(schemaName);
        verify(connection, never()).createStatement();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection).createStatement();
        verify(connection.createStatement()).execute(eq("SELECT DATABASE()"));
        String schemaName = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, connection.getServerThreadId() + "");
    }

    @Test
    public void testGetConnectionIdWithoutOceanBaseConnection() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, never()).createStatement();
        assertEquals(null, connectionId);
    }

    @After
    public void tearDown() throws Exception {
        if (Objects.nonNull(connection)) {
            connection.close();
        }
    }
}