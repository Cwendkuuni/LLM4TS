import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);

        Connection actualConnection = dataSource.getConnection();

        assertNotNull(actualConnection);
        assertEquals(connectionMock, actualConnection);
        verify(dataSource, Mockito.times(1)).getConnectionProxy(connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(false);

        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionSameUsernamePassword() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);

        Connection actualConnection = dataSource.getConnection("username", "password");

        assertNotNull(actualConnection);
        assertEquals(connectionMock, actualConnection);
        verify(dataSource, Mockito.times(1)).getConnectionProxy(connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernamePassword() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);

        dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = Mockito.spy(new SingleConnectionDataSource());
        dataSource.setConnection(connectionMock);

        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        Connection connectionMock = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(connectionMock);

        dataSource.close();

        assertTrue(true); // No exceptions thrown, consider the test passed
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        dataSource.addInitializer(initializerMock);

        assertTrue(dataSource.getInitializerList().contains(initializerMock));
    }

    // Add more test cases for other methods in the SingleConnectionDataSource class

}