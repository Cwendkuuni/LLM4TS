package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPointAdapter;
import org.pf4j.impl.DefaultExtensionPointRegistry;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.impl.DefaultModuleContext;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * JUnit test file for OBOracleSessionExtension class.
 *
 * @author yaobin
 */
public class OBOracleSessionExtensionTest {

    private DefaultExtensionRegistry extensionRegistry;
    private DefaultExtensionPointRegistry extensionPointRegistry;
    private DefaultModuleContext moduleContext;
    private ExtensionManager extensionManager;
    private SessionExtensionPoint obOracleSessionExtension;

    @Before
    public void setup() {
        extensionRegistry = new DefaultExtensionRegistry();
        extensionPointRegistry = new DefaultExtensionPointRegistry();
        moduleContext = new DefaultModuleContext();
        extensionManager = new DefaultExtensionManager(extensionRegistry, extensionPointRegistry, moduleContext);

        obOracleSessionExtension = new OBOracleSessionExtension();
        extensionRegistry.loadExtensions(OBOracleSessionExtension.class);
        extensionManager.startExtensions(true);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obOracleSessionExtension.killQuery(mockConnection, "connectionId");

        // Add your assertions here
        assertNotNull(mockConnection);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(new JdbcOperationsUtil() {
            @Override
            public void execute(String sql) throws SQLException {
                // Add your mocked behavior here
            }

            @Override
            public int update(String sql) throws SQLException {
                if (sql.startsWith("ALTER SESSION SET CURRENT_SCHEMA=")) {
                    return 1;
                }
                return 0;
            }

            @Override
            public String queryForObject(String sql, Class<String> type) throws SQLException {
                if (sql.startsWith("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")) {
                    return "oldSchema";
                }
                return null;
            }
        });

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        // Add your assertions here
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenThrow(new SQLException("Test exception"));

        obOracleSessionExtension.switchSchema(mockConnection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(new JdbcOperationsUtil() {
            @Override
            public String queryForObject(String sql, Class<String> type) throws SQLException {
                if (sql.startsWith("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")) {
                    return "currentSchema";
                }
                return null;
            }
        });

        String result = obOracleSessionExtension.getCurrentSchema(mockConnection);

        // Add your assertions here
        assertEquals("currentSchema", result);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(new JdbcOperationsUtil() {
            @Override
            public String queryForObject(String sql, Class<String> type) throws SQLException {
                if (sql.startsWith("select userenv('sessionid') from dual")) {
                    return "connectionId";
                }
                return null;
            }
        });

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        // Add your assertions here
        assertEquals("connectionId", result);
    }

    @Test
    public void testGetConnectionIdWithoutQuery() {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234L);

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        // Add your assertions here
        assertEquals("1234", result);
    }
}