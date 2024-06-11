
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Before
    public void setup() {
        extension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        extension.killQuery(connection, connectionId);

        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        extension.switchSchema(connection, "diffSchema");

        verify(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        extension.getCurrentSchema(connection);

        verify(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId(){
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);

        assertEquals("1", extension.getConnectionId(connection));
    }
}
