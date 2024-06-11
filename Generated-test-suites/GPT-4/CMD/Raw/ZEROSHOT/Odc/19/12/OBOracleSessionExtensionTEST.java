
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockedConnection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        extension.killQuery(mockedConnection, "123456");
        verify(mockedConnection, times(1)).execute("KILL QUERY 123456");
    }

    @Test
    public void switchSchemaShouldNotSwitchIfSame() throws SQLException {
        Connection mockedConnection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        when(extension.getCurrentSchema(mockedConnection)).thenReturn("someSchema");
        extension.switchSchema(mockedConnection, "someSchema");
        verify(mockedConnection, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockedConnection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        String schema = extension.getCurrentSchema(mockedConnection);
        verify(mockedConnection, times(1)).queryForObject(anyString(), eq(String.class));
        Assert.assertNotNull(schema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection mockedConnection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        String connectionId = extension.getConnectionId(mockedConnection);
        verify(mockedConnection, times(1)).queryForObject(anyString(), eq(String.class));
        Assert.assertNotNull(connectionId);
    }
}
