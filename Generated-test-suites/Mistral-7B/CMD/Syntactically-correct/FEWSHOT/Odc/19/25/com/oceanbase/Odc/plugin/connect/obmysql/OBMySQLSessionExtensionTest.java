package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.JdbcOperationsUtilTest;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtilMock;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
        connection = DriverManager.getConnection("jdbc:oceanbase:ob://localhost:28801/testdatabase", "user", "password");
        jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtilMock.getJdbcOperations(connection)).thenReturn(new JdbcOperationsUtilTest.TestJdbcOperations());
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperationsUtil(jdbcOperationsUtilMock);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:ob://localhost:28801/testdatabase", "user", "password");
        obMySQLSessionExtension.setJdbcOperationsUtil(new JdbcOperationsUtilTest.TestJdbcOperations());
        obMySQLSessionExtension.killQuery(connection, "12345");
        verify(jdbcOperationsUtilMock).execute("KILL QUERY 12345");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(null, "12345");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testschema";
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(null, "testschema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaWithEmptySchemaName() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, "");
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testdatabase";
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testdatabase", currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        String currentSchema = OBMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() {
        String connectionId = OBMySQLSessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
        String expectedConnectionId = "1234";
        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullQuerySqlException() throws SQLException {
        String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
        when(jdbcOperationsUtilMock.queryForObject(eq("select connection_id()"), any(Class.class))).thenThrow(new SQLException());
        String expectedConnectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
        assertEquals(expectedConnectionId, connectionId);
    }
}