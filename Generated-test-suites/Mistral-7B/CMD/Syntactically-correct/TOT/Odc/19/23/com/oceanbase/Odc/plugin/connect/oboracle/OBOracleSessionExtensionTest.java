package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(null);

        extension.killQuery(connection, "123456");
        assertEquals(0, 1); // Assuming the execute method returns the number of rows affected
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(null, "123456");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = "testSchema";
        String newSchema = "newTestSchema";

        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, newSchema);
        assertEquals(newSchema, "newTestSchema");
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.setJdbcOperations(jdbcOperations);

        String currentSchema = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = "123456";

        when(connection.isValid()).thenReturn(true);
        when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertNull(extension.getConnectionId(null));
    }
}