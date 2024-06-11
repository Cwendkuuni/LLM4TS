package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pf4j.Extension;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
@SuppressWarnings("java:S1452")
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        JdbcOperationsUtil jdbcOperationsUtil = new JdbcOperationsUtil();
        jdbcOperationsUtil.setJdbcOperations(jdbcOperations);
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenCallRealMethod();
        when(obOracleSessionExtension.switchSchema(connection, "testSchema"))
                .thenCallRealMethod();
        when(obOracleSessionExtension.killQuery(connection, "testConnectionId"))
                .thenCallRealMethod();
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("originalSchema");
        when(jdbcOperations.update(anyString())).thenAnswer(invocation -> invocation.callRealMethod());
        when(jdbcOperations.queryForObject(anyString(), any()))
                .thenAnswer(invocation -> invocation.getArgumentAt(1, 0));
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "testConnectionId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        assertNull("No exception was thrown",
                obOracleSessionExtension.findException());
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        String connectionId = "testConnectionId";
        when(jdbcOperations.execute(anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "originalSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals("No schema was switched", schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithDifferentSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals("Schema was switched", schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        obOracleSessionExtension.switchSchema(connection, null);
        assertEquals("No schema was switched", "originalSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithValidConnection() throws SQLException {
        String connectionId = "testConnectionId";
        when(connection instanceof OceanBaseConnection)
                .thenReturn(mock(OceanBaseConnection.class));
        obOracleSessionExtension.getConnectionId(connection);
        assertEquals("Connection id was retrieved", connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        obOracleSessionExtension.getConnectionId(null);
        assertNull("Null connection returned null connection id", obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithNonOceanBaseConnection() throws SQLException {
        obOracleSessionExtension.getConnectionId(connection);
        assertNull("Non OceanBaseConnection returned null connection id", obOracleSessionExtension.getConnectionId(connection));
    }

}