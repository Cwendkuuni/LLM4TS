
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testSingleConnectionDataSourceConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        assertFalse(dataSource.getAutoReconnect());
    }

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource dataSource = spy(new SingleConnectionDataSource());
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        try {
            Connection result = dataSource.getConnection();
            assertEquals(connection, result);
        } catch (SQLException e) {
            fail("Should not throw SQLException");
        }
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        SingleConnectionDataSource dataSource = spy(new SingleConnectionDataSource());
        String username = "testUser";
        String password = "testPassword";
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        try {
            Connection result = dataSource.getConnection(username, password);
            assertEquals(connection, result);
        } catch (SQLException e) {
            fail("Should not throw SQLException");
        }
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = spy(new SingleConnectionDataSource());
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        try {
            dataSource.resetConnection();
            assertNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Should not throw SQLException");
        }
    }
}


// More test methods should be added here to cover all public methods, edge cases, and exception scenarios