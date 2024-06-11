import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsername() throws SQLException {
        dataSource.getConnection("invalidUser", "testPassword");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidPassword() throws SQLException {
        dataSource.getConnection("testUser", "invalidPassword");
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
        assertEquals(initializer, dataSource.getInitializerList().get(0));
    }

    @Test
    public void testCloseWithoutConnection() {
        dataSource.close();
        // No exception should be thrown
    }

    @Test
    public void testAutoCommitSetterGetter() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }

    @Test
    public void testAutoReconnectGetter() {
        assertFalse(dataSource.isAutoReconnect());
    }

    @Test
    public void testEventPublisherSetterGetter() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        assertEquals(eventPublisher, dataSource.getEventPublisher());
    }

}