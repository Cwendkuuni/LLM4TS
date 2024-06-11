package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtensionManager;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unchecked")
@Slf4j
public class OBMySQLSessionExtensionTest {

    private static final String TEST_SCHEMA = "test_schema";
    private static final String TEST_CONNECTION_ID = "test_connection_id";

    private ExtensionManager extensionManager;
    private PluginWrapper<OBMySQLSessionExtension> pluginWrapper;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        extensionManager = new TestExtensionManager();
        pluginWrapper = new PluginWrapper<>();
        pluginWrapper.setName("OBMySQLSessionExtensionTest");
        pluginWrapper.setImplementation(OBMySQLSessionExtension.class);
        extensionManager.addExtension(pluginWrapper);

        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class)[0];
        jdbcOperationsMock = mock(JdbcOperations.class);
        when(jdbcOperationsMock.execute(anyString())).thenReturn(null);
        when(jdbcOperationsMock.queryForObject(anyString(), eq(String.class))).thenAnswer(i -> TEST_SCHEMA);
    }

    @Test
    public void testKillQuery() {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connection_id";

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);

        verifyKillQueryCall(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(connectionMock.setCatalog(anyString())).thenReturn(connectionMock);

        obMySQLSessionExtension.switchSchema(connectionMock, TEST_SCHEMA);

        assertEquals(TEST_SCHEMA, getCurrentSchema(connectionMock));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(connectionMock.setCatalog(anyString())).thenReturn(connectionMock);

        obMySQLSessionExtension.switchSchema(connectionMock, TEST_SCHEMA);

        assertEquals(TEST_SCHEMA, getCurrentSchema(connectionMock));

        obMySQLSessionExtension.switchSchema(connectionMock, TEST_SCHEMA);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connectionMock = mock(Connection.class);

        assertEquals(TEST_SCHEMA, obMySQLSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() {
        Connection connectionMock = mock(Connection.class);
        String connectionId = null;

        assertEquals(TEST_CONNECTION_ID, obMySQLSessionExtension.getConnectionId(connectionMock));

        connectionMock = mock(OceanBaseConnection.class);
        when(connectionMock.getServerThreadId()).thenReturn(123L);

        assertEquals("123", obMySQLSessionExtension.getConnectionId(connectionMock));

        connectionId = obMySQLSessionExtension.getConnectionId(null);

        assertNull(connectionId);
    }

    private void verifyKillQueryCall(Connection connectionMock, String connectionId) {
        String killQuerySql = "KILL QUERY " + connectionId;

        when(jdbcOperationsMock.execute(killQuerySql)).thenReturn(null);

        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    private String getCurrentSchema(Connection connectionMock) {
        return obMySQLSessionExtension.getCurrentSchema(connectionMock);
    }
}