import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private Connection connection;

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        singleConnectionDataSource.setUsername("test");
        singleConnectionDataSource.setPassword("password");
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test(expected = SQLException.class)
    public void testGetMethodConnectionInvalid() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetMethodDiffCredentials() throws SQLException {
        singleConnectionDataSource.getConnection("foo", "bar");
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionCloseException() throws SQLException {
        singleConnectionDataSource.connection = connection;
        doThrow(new SQLException()).when(connection).close();
        singleConnectionDataSource.resetConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.resetConnection();
        verify(connectionInitializer, times(2)).init(any());
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionException() throws SQLException {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.innerCreateConnection();
    }
}