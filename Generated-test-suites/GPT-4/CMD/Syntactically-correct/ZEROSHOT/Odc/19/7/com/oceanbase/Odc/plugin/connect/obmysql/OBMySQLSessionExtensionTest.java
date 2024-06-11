
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    private OBMySQLSessionExtension extensionUnderTest;

    @Before
    public void setUp() {
        extensionUnderTest = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        extensionUnderTest.killQuery(mockConnection, "123");
        verify(mockConnection).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaDifferent() throws SQLException {
        when(extensionUnderTest.getCurrentSchema(mockConnection)).thenReturn("test1");
        doNothing().when(mockConnection).setCatalog(anyString());

        extensionUnderTest.switchSchema(mockConnection, "test2");
        verify(mockConnection, times(1)).setCatalog("test2");
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaSame() throws SQLException {
        when(extensionUnderTest.getCurrentSchema(mockConnection)).thenReturn("test1");
        extensionUnderTest.switchSchema(mockConnection, "test1");
        verify(mockConnection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        extensionUnderTest.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        extensionUnderTest.getConnectionId(mockConnection);
        verify(mockConnection, times(1)).queryForObject("select connection_id()", String.class);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(mockConnection.queryForObject(anyString(), any())).thenThrow(new SQLException());
        extensionUnderTest.getConnectionId(oceanBaseConnection);
        verify(oceanBaseConnection, times(1)).getServerThreadId();
    }

}
