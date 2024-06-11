
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockconnection;
    private OceanBaseConnection mockOceanBaseConnection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockconnection = Mockito.mock(Connection.class);
        mockOceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String query = "KILL QUERY 123";
        obMySQLSessionExtension.killQuery(mockconnection, "123");
        //assert something
    }

    @Test
    public void testSwitchSchemaWhenSchemasAreSame() throws SQLException {
        obMySQLSessionExtension.switchSchema(mockconnection, "schemaName");
        Mockito.verify(mockconnection, Mockito.never()).setCatalog(Mockito.anyString());
    }

    @Test
    public void testSwitchSchemaWhenSchemasAreDifferent() throws SQLException {
        Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockconnection)).thenReturn("oldSchema");
        obMySQLSessionExtension.switchSchema(mockconnection, "newSchema");
        Mockito.verify(mockconnection).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockconnection)).thenReturn("schemaName");
        Assert.assertEquals("schemaName", obMySQLSessionExtension.getCurrentSchema(mockconnection));
    }

    @Test
    public void testGetConnectionIdWithSQLException() {
        Mockito.when(obMySQLSessionExtension.getConnectionId(mockconnection)).thenThrow(new SQLException());
        Assert.assertNull(obMySQLSessionExtension.getConnectionId(mockconnection));
    }

    @Test
    public void testGetConnectionId() {
        Mockito.when(obMySQLSessionExtension.getConnectionId(mockOceanBaseConnection)).thenReturn("999");
        Assert.assertEquals("999", obMySQLSessionExtension.getConnectionId(mockOceanBaseConnection));
    }

    @Test
    public void testGetConnectionIdWithNonOceanBaseConnection() {
        Mockito.when(obMySQLSessionExtension.getConnectionId(mockconnection)).thenReturn("5678");
        Assert.assertEquals("5678", obMySQLSessionExtension.getConnectionId(mockconnection));
    }
}