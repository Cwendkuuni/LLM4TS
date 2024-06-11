package com.oceanbase.odc.core.datasource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.junit.Rule;

public class SingleConnectionDataSourceTest {

    @Rule
    public MockitoJUnit mockitoRule = MockitoJUnit.rule();

    @Test
    public void testAutoReconnectConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(true);
        ds.connection = mockConn;
        ds.getConnection();

        Mockito.verify(mockConn, Mockito.times(1)).close();
    }

    @Test
    public void testGetConnectionWithValidUserNameAndPassword() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        String username = "testUser";
        String password = "testPassword";
        
        ds.setUsername(username);
        ds.setPassword(password);
        
        ds.connection = null;
        
        Connection resultConn = ds.getConnection(username, password);
        assertEquals(username, resultConn.getSchema());
        assertEquals(password, resultConn.getCatalog());
    }

    @Test(expected=SQLException.class)
    public void testGetConnectionWithInvalidUserNameAndPassword() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        String username = "testUser";
        String password = "testPassword";
        ds.setUsername(username);
        ds.setPassword(password);
        ds.connection = null;
        ds.getConnection("invalidUser", "invalidPassword");
    }

    @Test(expected=SQLException.class)
    public void testResetConnectionClosedConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        Connection mockConn = Mockito.mock(Connection.class);
        Mockito.when(mockConn.isClosed()).thenReturn(true);
        ds.connection = mockConn;
        ds.resetConnection();
        
        Mockito.verify(mockConn, Mockito.times(1)).close();
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource scds = new SingleConnectionDataSource();
        Connection mockConnection = Mockito.mock(Connection.class);
        scds.connection = mockConnection;
        scds.close();

        Mockito.verify(mockConnection, Mockito.times(1)).close();
    }
    
    @Test
    public void testPrepareConnectionAutoCommit() throws SQLException {
        SingleConnectionDataSource scds = new SingleConnectionDataSource();
        Connection mockConnection = Mockito.mock(Connection.class);
        
        scds.setAutoCommit(true);
        Mockito.when(mockConnection.getAutoCommit()).thenReturn(false);
        scds.prepareConnection(mockConnection);
        Mockito.verify(mockConnection, Mockito.times(1)).setAutoCommit(true);
    }
}