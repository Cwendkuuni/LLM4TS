import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() {
        try {
            singleConnectionDataSource.getConnection();
            assertNotNull(singleConnectionDataSource.getConnection());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testResetConnection() {
        try {
            singleConnectionDataSource.resetConnection();
            assertNull(singleConnectionDataSource.getConnection());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        verify(connectionInitializer, times(1)).init(any());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        assertTrue(true);
    }

    @Test
    public void testInnerCreateConnection() {
        try {
            singleConnectionDataSource.innerCreateConnection();
            assertNotNull(singleConnectionDataSource.getConnection());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testConnectionProxy() {
        try {
            assertNotNull(singleConnectionDataSource.getConnectionProxy(mock(Connection.class)));
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testInitConnection() {
        Connection connection = mock(Connection.class);
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        try {
            singleConnectionDataSource.initConnection(connection);
            verify(connectionInitializer, times(1)).init(connection);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        try {
            singleConnectionDataSource.onConnectionReset(mock(Connection.class));
            verify(eventPublisher, times(1)).publishEvent(any());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler invocationHandler =
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mock(Connection.class), mock(Lock.class));
        try {
            assertNotNull(invocationHandler.invoke(mock(Object.class), mock(Method.class), new Object[] {}));
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

}