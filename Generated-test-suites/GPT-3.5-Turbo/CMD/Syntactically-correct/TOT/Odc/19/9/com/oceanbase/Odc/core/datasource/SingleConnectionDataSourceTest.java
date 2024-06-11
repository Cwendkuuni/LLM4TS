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
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setAutoCommit(true);
        dataSource.setTimeOutMillis(5000);
    }

    @Test
    public void testGetConnection_validUsernameAndPassword_shouldReturnConnection() throws SQLException {
        Connection connection = dataSource.getConnection("root", "password");
        assertNotNull(connection);
    }

    @Test
    public void testGetConnection_invalidUsernameOrPassword_shouldThrowSQLException() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "password"));
    }

    @Test
    public void testResetConnection_connectionNotNull_shouldResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNotEquals(connection, dataSource.getConnection());
    }

    @Test
    public void testAddInitializer_validInitializer_shouldAddToInitializerList() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose_connectionNotNull_shouldCloseConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testInnerCreateConnection_connectionIsNull_shouldCreateNewConnection() throws SQLException {
        assertNull(dataSource.getConnection());
    }
}