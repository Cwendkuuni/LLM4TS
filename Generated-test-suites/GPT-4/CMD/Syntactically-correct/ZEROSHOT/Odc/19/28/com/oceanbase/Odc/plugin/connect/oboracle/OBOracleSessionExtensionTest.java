package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(JUnit4.class)
public class OBOracleSessionExtensionTest {
    
    private OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "1";
        sessionExtension.killQuery(connection, connectionId);

        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "OldDb";
        String newSchema = "NewDb";

        Mockito.when(sessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        sessionExtension.switchSchema(connection, newSchema);

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + newSchema;
        Mockito.verify(connection, Mockito.times(1)).update(alterSql);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        String expectedConnectionId = "1";

        Mockito.when(((OceanBaseConnection)connection).getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertNotNull(actualConnectionId);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection conn = Mockito.mock(Connection.class);
        String expectedSchema = "TestDb";

        Mockito.when(sessionExtension.getCurrentSchema(conn)).thenReturn(expectedSchema);

        String actualSchema = sessionExtension.getCurrentSchema(conn);

        assertNotNull(actualSchema);
        assertEquals(expectedSchema, actualSchema);
    }
}
