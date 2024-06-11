import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private EventPublisher eventPublisher;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();
        eventPublisher = Mockito.mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                SingleConnectionDataSourceTest.this.connection = connection;
            }
        });
    }

    @Test
    public void testNullUsernameAndPassword() {
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("", ""));
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection returnedConnection = singleConnectionDataSource.getConnection();
        assertNotNull(returnedConnection);
        assertEquals(returnedConnection, singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection initialConnection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        Connection resetConnection = singleConnectionDataSource.getConnection();
        assertNotSame(initialConnection, resetConnection);
        Mockito.verify(eventPublisher).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertEquals(connectionInitializer, initializerList.get(0));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhenAnotherThreadGetConnection() throws SQLException, InterruptedException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        SingleConnectionDataSourceTest anotherThreadSingleConnectionDataSource = new SingleConnectionDataSourceTest();
        List<SingleConnectionDataSourceTest> threads = new ArrayList<>();
        threads.add(this);
        threads.add(anotherThreadSingleConnectionDataSource);
        for (SingleConnectionDataSourceTest thread : threads) {
            thread.singleConnectionDataSource = singleConnectionDataSource;
            new Thread(() -> {
                try {
                    thread.singleConnectionDataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        singleConnectionDataSource.getConnection();
    }
}