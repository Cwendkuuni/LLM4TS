package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private Connection mockedConnection = mock(Connection.class);

    @Test(expected = NullPointerException.class)
    public void testKillQuery_nullConnection() {
        obMySQLSessionExtension.killQuery(null, "123");
    }

    @Test(expected = NullPointerException.class)
    public void testKillQuery_nullConnectionId() {
        obMySQLSessionExtension.killQuery(mockedConnection, null);
    }

    @Test
    public void testSwitchSchema_nullConnection() {
        try {
            obMySQLSessionExtension.switchSchema(null, "testSchema");
        } catch (Exception e) {
            assertTrue(e instanceof SQLException);
        }
    }

    @Test
    public void testGetCurrentSchema_nullConnection() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId_nullConnection() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
    
    @Test
    public void testGetConnectionId_Exception() {
        when(obMySQLSessionExtension.getConnectionId(any())).thenThrow(new RuntimeException("Exception thrown"));

        try {
           obMySQLSessionExtension.getConnectionId(mockedConnection);
           fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Exception thrown"));
        }
    }
}