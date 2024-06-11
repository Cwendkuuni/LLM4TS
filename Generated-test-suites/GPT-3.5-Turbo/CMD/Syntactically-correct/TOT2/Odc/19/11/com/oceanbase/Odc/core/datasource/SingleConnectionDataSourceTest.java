import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("testuser", "password");
        assertNotNull("Connection should not be null", connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.getConnection("invaliduser", "invalidpassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull("Connection should be null after reset", singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        assertNull("Connection should be null after closing", singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals("Initializer should be added", 1, singleConnectionDataSource.getInitializerList().size());
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.initConnection(connection);
        verify(connectionInitializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        singleConnectionDataSource.onConnectionCreated(mock(Connection.class));
        // No exception expected
    }

    @Test
    public void testOnConnectionReset() {
        singleConnectionDataSource.onConnectionReset(mock(Connection.class));
        // No exception expected
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        singleConnectionDataSource.innerCreateConnection();
        assertNotNull("Connection should not be null after creation", singleConnectionDataSource.getConnection());
    }

}