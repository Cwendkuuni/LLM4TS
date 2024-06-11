import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "admin";
        String password = "password";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        String username = "invalid";
        String password = "invalid";
        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        verify(connection).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    private void setFinalField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    public void testGetConnectionProxy() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Connection connection = dataSource.getConnection();
        Lock lock = new ReentrantLock();
        setFinalField(dataSource, "lock", lock);
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotEquals(connection, proxyConnection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLock() throws SQLException {
        dataSource.setLock(new ReentrantLock());
        dataSource.tryLock();
        Connection connection = dataSource.getConnection();
        dataSource.getConnectionProxy(connection);
    }

// Add more tests as needed

}