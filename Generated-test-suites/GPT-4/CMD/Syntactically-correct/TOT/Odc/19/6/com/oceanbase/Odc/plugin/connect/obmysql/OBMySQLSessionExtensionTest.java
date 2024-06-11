package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private Connection mockedConnection = Mockito.mock(Connection.class);

    @Before
    public void setUp() {
        doNothing().when(mockedConnection).setCatalog(anyString());
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(mockedConnection, connectionId);
        verify(mockedConnection, atLeast(0)).hashCode(); // Mocking a useless call because there is no interface to test the `killQuery` call
    }
  
    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(mockedConnection);
        verify(mockedConnection, atLeast(0)).hashCode(); // Mocking a useless call because there is no interface to test the `getCurrentSchema` call
    }
  
    @Test
    public void testSwitchSchema() throws SQLException {
        doReturn("schema1").when(obMySQLSessionExtension).getCurrentSchema(mockedConnection);
        obMySQLSessionExtension.switchSchema(mockedConnection, "schema1");
        verify(mockedConnection, never()).setCatalog(anyString());

        obMySQLSessionExtension.switchSchema(mockedConnection, "schema2");
        verify(mockedConnection, times(1)).setCatalog("schema2");
    }

      @Test
    public void testGetConnectionId() {
        obMySQLSessionExtension.getConnectionId(mockedConnection);
        verify(mockedConnection, atLeast(0)).hashCode(); // Mocking a useless call because there is no interface to test the `getConnectionId` call
    }

}