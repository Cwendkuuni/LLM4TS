
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection connection;
    private String connectionId;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
        connectionId = "testConnectionId";
    }

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection, connectionId);
        verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(sessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        sessionExtension.switchSchema(connection, schemaName);

        // Verifying ALTER SESSION SQL call
        verify(connection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + sessionFactory.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaQuery = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";

        sessionExtension.getCurrentSchema(connection);

        verify(connection).createStatement().executeQuery(schemaQuery);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionIdQuery = "select userenv('sessionid') from dual";

        sessionExtension.getConnectionId(connection);

        verify(connection).createStatement().executeQuery(connectionIdQuery);
    }
}
