import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource scd;
    
    @Mock
    ConnectionInitializer mockInitializer;
    @Mock
    EventPublisher mockPublisher;

    @Before
    public void setUp() {
        scd = new SingleConnectionDataSource();
    }

    @Test
    public void testAutoCommit() {
        scd.setAutoCommit(true);
        assertTrue(scd.getAutoCommit());
    }

    @Test
    public void testAutoReconnect() {
        scd = new SingleConnectionDataSource(true);
        assertTrue(scd.getAutoReconnect());
    }

    @Test
    public void testEventPublisher() {
        scd.setEventPublisher(mockPublisher);
        assertEquals(mockPublisher, scd.getEventPublisher());
    }

    @Test
    public void testTimeoutMillis() {
        scd.setTimeOutMillis(2000);
        assertEquals(2000, scd.getTimeOutMillis());
    }

    @Test(expected = SQLException.class)
    public void testGetConnection() throws SQLException {
        scd.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithArgs() throws SQLException {
        scd.getConnection("username", "password");
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        scd.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        scd.addInitializer(mockInitializer);
        assertEquals(1, scd.getInitializerList().size());
    }

    @Test
    public void testClose() {
        scd.close();
        assertNull(scd.getConnection());
    }
}