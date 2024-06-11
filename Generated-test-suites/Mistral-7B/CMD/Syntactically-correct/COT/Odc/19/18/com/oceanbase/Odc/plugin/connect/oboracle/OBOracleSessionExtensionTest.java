package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.LazyClassLoader;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.CurrentSchema;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SchemaSwitchException;
import com.oceanbase.odc.plugin.connect.oboracle.test.mock.MockJdbcOperationsUtil;

/**
 * Test class for OBOracleSessionExtension
 */
@Slf4j
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connection;
    private MockJdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() {
        LazyClassLoader classLoader = new LazyClassLoader();
        extensionManager = new TestExtensionManager(classLoader);
        extensionManager.loadExtensions(OBOracleSessionExtension.class);
        obOracleSessionExtension = extensionManager.getExtension(OBOracleSessionExtension.class);
        connection = mock(Connection.class);
        jdbcOperationsUtil = new MockJdbcOperationsUtil();
        when(connection.createStatement()).thenReturn(jdbcOperationsUtil.getStatement());
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        // Add a verify statement to check if the method is called correctly
        // This depends on the implementation of JdbcOperationsUtil.execute()
        // Add assertions if necessary to check the impact of the method call
    }

    @Test
    public void testSwitchSchema_success() {
        String newSchema = "newSchema";
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(newSchema, currentSchema);
    }

    @Test
    public void testSwitchSchema_currentSchemaIsSame() {
        String schemaName = "schemaName";
        String currentSchema = schemaName;
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // No assertions needed as the method does not perform any action
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_sqlException() {
        when(jdbcOperationsUtil.update(anyString())).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, "schemaName"));
    }

    @Test(expected = SchemaSwitchException.class)
    public void testSwitchSchema_schemaSwitchException() {
        when(jdbcOperationsUtil.update(anyString())).thenReturn(0);
        assertThrows(SchemaSwitchException.class, () -> obOracleSessionExtension.switchSchema(connection, "schemaName"));
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "currentSchema";
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_connection() {
        String connectionId = "connectionId";
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_null() {
        when(connection.getClass()).thenReturn(Connection.class);
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}