import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.mockito.*;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;
    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource.setAutoCommit(true);
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void test_getConnection() {
        assertNotNull(dataSource.getConnection());
        assertTrue(dataSource.getAutoCommit());

        dataSource.setConnection(null);
        assertNull(dataSource.getConnection());
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void test_getConnection_username_password() {        
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_username", "invalid_password"));
    }

    @Test 
    public void test_resetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());

        dataSource.setConnection(null);
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void test_addInitializer() {
        assertNotNull(dataSource.initializerList);
        assertEquals(0, dataSource.initializerList.size());

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection con) {
                // Method implementation
            }
        });

        assertEquals(1, dataSource.initializerList.size());
    }

    @Test
    public void test_close() {
        assertDoesNotThrow(() -> dataSource.close());

        dataSource.setConnection(null);
        assertThrows(Throwable.class, () -> dataSource.close());
    }
}