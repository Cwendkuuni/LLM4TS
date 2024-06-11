package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Unit tests for {@link OBOracleSessionExtension} class
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setUp() {
        jdbcOperationsMock = mock(JdbcOperations.class);
        connection = Mockito.spy(mock(OceanBaseConnection.class));
        when(connection.getJdbcOperations()).thenReturn(jdbcOperationsMock);
    }

    @After
    public void tearDown() {
        connection = null;
        jdbcOperationsMock = null;
    }

    @Test
    public void testKillQuery_ValidConnectionId() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        obOracleSessionExtension.killQuery(connection, "connectionId");

        // then
        Mockito.verify(jdbcOperationsMock).execute("KILL QUERY connectionId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQuery_NullConnection() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        obOracleSessionExtension.killQuery(null, "connectionId");

        // then
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(null, "connectionId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQuery_NullConnectionId() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        obOracleSessionExtension.killQuery(connection, null);

        // then
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema_ValidSchemaName() {
        // given
        String schemaName = "testSchema";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // then
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        Mockito.verify(jdbcOperationsMock).update(alterSql);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchema_NullSchemaName() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        obOracleSessionExtension.switchSchema(connection, null);

        // then
        assertThrows(IllegalArgumentException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testGetCurrentSchema_ValidConnection() {
        // given
        String currentSchema = "testSchema";
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        String schema = obOracleSessionExtension.getCurrentSchema(connection);

        // then
        assertEquals(currentSchema, schema);
    }

    @Test
    public void testGetCurrentSchema_NullConnection() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        String schema = obOracleSessionExtension.getCurrentSchema(null);

        // then
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId_ValidConnection() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // then
        assertEquals("", connectionId);
    }

    @Test
    public void testGetConnectionId_ValidOceanBaseConnection() {
        // given
        long threadId = 123;
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        when(connection.getServerThreadId()).thenReturn(threadId);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // then
        assertEquals(Long.toString(threadId), connectionId);
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        // given
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // when
        String connectionId = obOracleSessionExtension.getConnectionId(null);

        // then
        assertEquals("", connectionId);
    }
}