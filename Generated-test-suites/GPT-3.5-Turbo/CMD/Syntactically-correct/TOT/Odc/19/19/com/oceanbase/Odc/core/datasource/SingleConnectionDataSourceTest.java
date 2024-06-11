import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() {
        dataSource = null;
    }

    @Test
    public void testGetConnectionNoConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionClosedConnection() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        doReturn(true).when(mockedConnection).isClosed();
        dataSource.connection = mockedConnection;

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionReconnect() throws SQLException {
        dataSource.setAutoReconnect(true);

        Connection mockedConnection = mock(Connection.class);
        doReturn(true).when(mockedConnection).isClosed();
        dataSource.connection = mockedConnection;

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        dataSource.connection = mockedConnection;

        Lock mockLock = mock(ReentrantLock.class);
        Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        lockField.set(dataSource, mockLock);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionInvalidUsernamePassword() {
        String username = "user";
        String password = "pass";

        try {
            dataSource.getConnection(username, password);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Success
        }
    }

    @Test
    public void testResetConnection() {
        Connection mockedConnection = mock(Connection.class);
        dataSource.connection = mockedConnection;
        dataSource.lock = mock(ReentrantLock.class);

        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(mockInitializer);

        assertEquals(1, dataSource.initializerList.size());
        assertSame(mockInitializer, dataSource.initializerList.get(0));
    }

}