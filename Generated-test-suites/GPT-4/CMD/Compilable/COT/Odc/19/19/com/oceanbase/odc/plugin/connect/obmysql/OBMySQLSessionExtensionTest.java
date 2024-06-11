
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.sql.Connection;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullParameters() {
        obMySQLSessionExtension.killQuery(null, "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryEmptyConnectionId() {
        Connection mockConn = mock(Connection.class);
        obMySQLSessionExtension.killQuery(mockConn, "");
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullParameters() throws SQLException {
        obMySQLSessionExtension.switchSchema(null, "schema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaEmptySchemaName() throws SQLException {
        Connection mockConn = mock(Connection.class);
        obMySQLSessionExtension.switchSchema(mockConn, "");
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaNullParameter() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection mockConn = mock(Connection.class);
        assertEquals("", obMySQLSessionExtension.getCurrentSchema(mockConn));
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdNullParameter() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
    
    @Test
    public void testGetConnectionId() {
        Connection mockConn = mock(Connection.class);
        assertEquals("", obMySQLSessionExtension.getConnectionId(mockConn));
    }
}
