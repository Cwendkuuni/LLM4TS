package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.impl.DefaultExtensionFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.exception.ODCException;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.util.StringUtils;

/**
 * Test class for {@link OBOracleSessionExtension} class
 *
 * @author yaobin
 * @since 4.2.0
 */
@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private static OBOracleSessionExtension obOracleSessionExtension;
    private static Connection connection;

    @Before
    public void setup() throws SQLException, Exception {
        ExtensionFactory factory = new DefaultExtensionFactory();
        obOracleSessionExtension = (OBOracleSessionExtension) factory.getExtensions(SessionExtensionPoint.class).stream()
                .filter(ext -> ext instanceof OBOracleSessionExtension)
                .findFirst().get();
        connection = obOracleSessionExtension.getExtensionManager().getApplicationContext().getBean(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() {
        String connectionId = "12345";
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(null, connectionId));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        String schemaName = "test_schema";
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.switchSchema(null, schemaName));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        String connectionId = obOracleSessionExtension.getConnectionId(null);
        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        assertThrows(ODCException.class, () -> obOracleSessionExtension.getConnectionId(connection));
    }

    private String getCurrentSchema(Connection connection) {
        return obOracleSessionExtension.getCurrentSchema(connection);
    }
}