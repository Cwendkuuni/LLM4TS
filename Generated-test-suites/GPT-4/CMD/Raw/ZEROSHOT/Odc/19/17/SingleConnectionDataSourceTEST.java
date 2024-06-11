
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Mock
    ConnectionInitializer initializer;
    @Mock
    EventPublisher publisher;
    @Spy
    Connection connection;
    @InjectMocks
    SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        dataSource = new SingleConnectionDataSource();
        assertEquals(10000, dataSource.getTimeOutMillis());
        assertFalse(dataSource.getAutoReconnect());
        assertNull(dataSource.getAutoCommit());
    }

    @Test
    public void testConstructorWithAutoReconnect() {
        dataSource = new SingleConnectionDataSource(true);
        assertTrue(dataSource.getAutoReconnect());
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.initializerList.size());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUserPasswordInvalid() throws SQLException {
        dataSource.getConnection("unknown", "password");
    }

    @Test
    public void testGetConnectionAfterClose() throws Exception {
        dataSource.close();
        assertNull(dataSource.connection);
    }

    @Test
    public void testGetConnection() throws Exception {
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.when(connection.isValid(Mockito.anyInt())).thenReturn(true);
        
        Connection conn = dataSource.getConnection();
        assertTrue(Proxy.isProxyClass(conn.getClass()));
    }

    @Test
    public void testGetConnectionWhenClosedAndNoAutoReconnect() throws Exception {
        dataSource.setAutoReconnect(false);
        Mockito.when(connection.isClosed()).thenReturn(true);
        
        try {
            dataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("Connection was closed or not valid", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.setEventPublisher(publisher);
        Connection spyCon = Mockito.spy(Connection.class);
        Mockito.doNothing().when(spyCon).close();
        dataSource.connection = spyCon;

        dataSource.resetConnection();
        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
        Mockito.verify(publisher).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertTrue(dataSource.connection == null);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        Mockito.verify(connection, Mockito.times(1)).setAutoCommit(true);
    }
}
