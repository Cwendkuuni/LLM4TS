package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;

@Slf4j
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private Statement statement;
    private JdbcOperationsUtil jdbcOperationsUtil;
    private DefaultExtension extension;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() throws SQLException, IllegalAccessException, InstantiationException {
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(connection.createStatement()).thenReturn(statement);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new JdbcOperationsUtil() {
            @Override
            public int executeUpdate(String sql) throws SQLException {
                return 1;
            }

            @Override
            public <T> T queryForObject(String sql, Class<T> type) throws SQLException {
                if (type.equals(String.class)) {
                    return (T) "test";
                }
                return null;
            }
        });

        PluginWrapper pluginWrapper = new PluginWrapper();
        pluginWrapper.setName("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        pluginWrapper.setClassName(OBMySQLSessionExtension.class.getName());
        extension = new DefaultExtension(pluginWrapper);
        obMySQLSessionExtension = (OBMySQLSessionExtension) extension.getImplementationInstance();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        when(statement.executeUpdate(eq("KILL QUERY testConnectionId"))).thenReturn(1);
        assertEquals(1, obMySQLSessionExtension.killQuery(connection, "testConnectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryException() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        when(statement.executeUpdate(eq("KILL QUERY testConnectionId"))).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        when(connection.setCatalog("testSchema")).thenReturn(true);
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaCurrentSchemaSame() throws SQLException {
        String currentSchema = "testSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123);
        assertEquals("123", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdNoOceanBaseConnection() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }
}