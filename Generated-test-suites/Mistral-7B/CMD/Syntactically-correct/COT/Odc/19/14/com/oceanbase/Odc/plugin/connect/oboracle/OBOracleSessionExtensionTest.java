package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.util.StringUtils;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "connectionId");
        // Add assertions or verification here if needed
    }

    @Test
    public void testKillQuery_WithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertThrows(NullPointerException.class, () -> extension.killQuery(null, "connectionId"));
    }

    @Test
    public void testKillQuery_WithEmptyConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertThrows(IllegalArgumentException.class, () -> extension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.getMetaData()).thenReturn(jdbcOperations);

        String schemaName = "testSchema";
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_WithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(null, "testSchema");
    }

    @Test
    public void testSwitchSchema_WithEmptySchemaName() {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.getMetaData()).thenReturn(jdbcOperations);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertThrows(IllegalArgumentException.class, () -> extension.switchSchema(connection, ""));
    }

    @Test
    public void testSwitchSchema_WithCurrentSchemaEqualsTargetSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String currentSchema = "testSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        extension.switchSchema(connection, currentSchema);
        // Add assertions or verification here if needed
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        String schemaName = "testSchema";
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);

        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_WithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertThrows(NullPointerException.class, () -> extension.getCurrentSchema(null));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchema_WithEmptyQueryResult() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(null);

        extension.getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(obConnection);

        String connectionId = extension.getConnectionId(connection);
        assertEquals(connectionId, obConnection.getServerThreadId() + "");
    }

    @Test
    public void testGetConnectionId_WithNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertThrows(NullPointerException.class, () -> extension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_WithNoOceanBaseConnection() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        String connectionId = extension.getConnectionId(connection);
        assertNull(connectionId);
    }

}