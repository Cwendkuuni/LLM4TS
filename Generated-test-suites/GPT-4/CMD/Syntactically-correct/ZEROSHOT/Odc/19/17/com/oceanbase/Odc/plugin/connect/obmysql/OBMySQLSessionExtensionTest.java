
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connectionMock;
    
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
        Mockito.verify(connectionMock).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schema = "testDatabase";
        when(obMySQLSessionExtension.getCurrentSchema(connectionMock)).thenReturn(schema);
        doNothing().when(connectionMock).setCatalog(schema);
        obMySQLSessionExtension.switchSchema(connectionMock, schema);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        obMySQLSessionExtension.getCurrentSchema(connectionMock);
        Mockito.verify(connectionMock).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        obMySQLSessionExtension.getConnectionId(connectionMock);
        Mockito.verify(connectionMock).queryForObject(querySql, String.class);
    }
}
