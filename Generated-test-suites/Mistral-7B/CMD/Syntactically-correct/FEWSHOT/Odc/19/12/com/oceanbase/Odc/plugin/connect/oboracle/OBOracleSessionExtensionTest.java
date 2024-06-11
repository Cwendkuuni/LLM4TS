package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    private Connection connection;

    @Before
    public void setup() {
        try {
            Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
            connection = DriverManager.getConnection("jdbc:oceanbase:obcds://localhost:28810/testdb", "user", "password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(1);

        extension.killQuery(connection, "12345");

        Mockito.verify(jdbcOperations).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        StringUtils stringUtils = mock(StringUtils.class);
        when(jdbcOperations.update(Mockito.anyString())).thenReturn(1);
        when(stringUtils.quoteOracleIdentifier(Mockito.anyString())).thenReturn("'schema_name'");

        extension.switchSchema(connection, "schema_name");

        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA='schema_name'");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaCurrentSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        String currentSchema = "schema_name";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "schema_name";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "12345";
        when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));

        connection.close();
        JdbcOperations oceanBaseJdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn((OceanBaseConnection) oceanBaseJdbcOperations);
        when(oceanBaseJdbcOperations.getServerThreadId()).thenReturn(6789);

        assertEquals("6789", extension.getConnectionId(connection));
    }
}