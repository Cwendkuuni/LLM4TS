import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String DATABASE_URL = "jdbc:test:database";
    private static final int TIME_OUT_MILLIS = 10 * 1000;
    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws Exception {
        Class.forName("TestDriver"); // assuming TestDriver is the JDBC driver class
        connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        dataSource = new SingleConnectionDataSource() {{
            setAutoReconnect(true);
            setEventPublisher(eventPublisherMock = Mockito.mock(EventPublisher.class));
            addInitializer(new DummyConnectionInitializer());
        }};
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection result = dataSource.getConnection();
        Assert.assertNotNull(result);
        Assert.assertEquals(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalidUsername");
        dataSource.setPassword("invalidPassword");
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionTwice() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        Connection secondConnection = dataSource.getConnection();
        Assert.assertEquals(firstConnection, secondConnection);
        dataSource.resetConnection();
        Connection thirdConnection = dataSource.getConnection();
        Assert.assertNotEquals(firstConnection, thirdConnection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection originalConnection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        Assert.assertNotEquals(originalConnection, newConnection);
        Mockito.verify(eventPublisherMock).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Assert.assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test(expected = SQLException.class)
    public void testClose() throws SQLException {
        dataSource.close();
        Connection connection = dataSource.getConnection();
        connection.close();
    }

    @Test(expected = ConflictException.class)
    public void testConnectionTimeout() throws SQLException, InterruptedException {
        dataSource.setTimeOutMillis(0);
        Connection connection = dataSource.getConnection();
        // Do nothing, just hold the connection to make the test fail due to timeout
    }

    private static class DummyConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) throws SQLException {
            // Do nothing
        }
    }
}