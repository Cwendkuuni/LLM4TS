
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private OceanBaseConnection oBaseConnectionMock;

    private OBMySQLSessionExtension sessionExtension;

    @Before
    public void setup() {
        sessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId="1";
        sessionExtension.killQuery(connectionMock, connectionId);
        verify(connectionMock,times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName="some-schema";
        when(sessionExtension.getCurrentSchema(connectionMock)).thenReturn("other-schema");
        sessionExtension.switchSchema(connectionMock, schemaName);
        verify(connectionMock).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName="some-schema";
        when(connectionMock.prepareStatement("SELECT DATABASE()")).thenReturn(schemaName);
        String resultSchema = sessionExtension.getCurrentSchema(connectionMock);
        assertEquals(schemaName,resultSchema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId="1";
        when(oBaseConnectionMock.getServerThreadId()).thenReturn(connectionId);
        String resultId = sessionExtension.getConnectionId(oBaseConnectionMock);
        assertEquals(connectionId,resultId);
    }
}
