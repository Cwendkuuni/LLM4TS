import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    private Connection mockConnection;

    @Before
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
        dataSource = Mockito.spy(SingleConnectionDataSource.class);
        dataSource.setAutoCommit(true);
        dataSource.connection = mockConnection;
    }

    @Test
    public void getConnectionWhenExistsAndOpen() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        
        dataSource.getConnection();
        verify(dataSource, times(1)).getConnectionProxy(any(Connection.class));
    }

    @Test(expected = SQLException.class)
    public void getConnectionWhenExistsAndClosed() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        
        dataSource.getConnection();
    }

    @Test
    public void getConnectionWithUsernameAndPassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        
        dataSource.getConnection("username", "password");
        verify(dataSource, times(1)).getConnectionProxy(any(Connection.class));
    }

    @Test(expected = SQLException.class)
    public void getConnectionWithWrongUsernameAndPassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        
        dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void resetConnectionTest() throws SQLException {
        dataSource.resetConnection();
        verify(mockConnection, times(1)).close();
        verify(dataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void closeConnectionTest() throws SQLException {
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test(expected = SQLException.class)
    public void innerCreateConnectionWhenConnectionNotNull() throws SQLException {
        Connection mockNewConnection = Mockito.mock(Connection.class);

        dataSource.innerCreateConnection();

        verify(mockNewConnection, never()).setAutoCommit(any());
        verify(dataSource, never()).getConnectionProxy(any(Connection.class));
    }

    // Add more tests based on the method's business logic.
}