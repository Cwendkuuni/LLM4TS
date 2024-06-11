package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@PrepareForTest({OBMySQLSessionExtension.class, JdbcOperationsUtil.class})
@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws SQLException {
        connection = PowerMockito.mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() throws SQLException {
        obMySQLSessionExtension.killQuery(null, "connectionId");
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY connectionId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryWithEmptyConnectionId() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() throws SQLException {
        obMySQLSessionExtension.switchSchema(null, "testSchema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "testSchema";
        PowerMockito.whenNew(JdbcOperationsUtil.class).withAnyArguments().thenReturn(new JdbcOperationsUtil() {
            @Override
            public String queryForObject(String sql, Class<String> type) throws SQLException {
                return expectedSchema;
            }
        });
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() throws SQLException {
        PowerMockito.whenNew(JdbcOperationsUtil.class).withAnyArguments().thenThrow(new SQLException());
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "123456";
        PowerMockito.whenNew(JdbcOperationsUtil.class).withAnyArguments().thenReturn(new JdbcOperationsUtil() {
            @Override
            public String queryForObject(String sql, Class<String> type) throws SQLException {
                return expectedConnectionId;
            }
        });
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        PowerMockito.whenNew(JdbcOperationsUtil.class).withAnyArguments().thenThrow(new SQLException());
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(null, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = PowerMockito.mock(OceanBaseConnection.class);
        PowerMockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        connection = oceanBaseConnection;
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", actualConnectionId);
    }
}