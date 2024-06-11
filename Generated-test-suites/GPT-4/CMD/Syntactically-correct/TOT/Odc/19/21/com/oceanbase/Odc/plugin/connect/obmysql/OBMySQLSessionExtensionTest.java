package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection, "queryId");
        verify(connection).execute("KILL QUERY queryId");
    }

    @Test
    public void testSwitchSchema_SameSchema() {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("mySchema");
        sessionExtension.switchSchema(connection, "mySchema");
        verify(connection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_DifferentSchema() {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("mySchema");
        sessionExtension.switchSchema(connection, "otherSchema");
        verify(connection).setCatalog("otherSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("mySchema");
        Assert.assertEquals("mySchema", sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("connectionId");
        Assert.assertEquals("connectionId", sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn((long) 1);
        Assert.assertEquals("1", sessionExtension.getConnectionId(oceanBaseConnection));
    }
}