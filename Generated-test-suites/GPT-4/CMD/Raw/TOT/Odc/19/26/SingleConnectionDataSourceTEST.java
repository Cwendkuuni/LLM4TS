
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import java.sql.SQLException;
import java.sql.Connection;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource dataSource;
    String USERNAME = "admin";
    String PASSWORD = "password123";
    Connection connectionMock;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setAutoCommit(true);
        dataSource.setTimeOutMillis(3000);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(false);

        assertNotNull("verify connection is not null upon creation", dataSource.getConnection());   
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(true);

        dataSource.resetConnection();

        assertNull("After reset, connection should be null", dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testInvalidLoginShouldThrowException() throws SQLException {
        dataSource.getConnection("invaliduser", "invalidpassword");
    }    

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenClosedAndNoAutoReconnect() throws SQLException {
        dataSource.setAutoReconnect(false);
        dataSource.setConnection(connectionMock);
        when(connectionMock.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        // a test could be designed to verify function of the initializer, 
        // but without concrete implementation we cannot test the behavior here
    }

    // similar test cases should be added for all other uncovered methods
}
