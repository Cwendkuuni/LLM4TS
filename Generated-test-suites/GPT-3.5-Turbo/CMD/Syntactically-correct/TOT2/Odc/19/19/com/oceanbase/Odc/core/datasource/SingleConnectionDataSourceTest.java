import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertNotNull(dataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        assertNotNull(dataSource.getInitializerList());
    }

    @Test
    public void testAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testPrepareConnection() {
        assertDoesNotThrow(() -> dataSource.prepareConnection(dataSource.getConnection()));
    }

    @Test
    public void testGetConnectionProxy() {
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(dataSource.getConnection()));
    }

    @Test
    public void testInitConnection() {
        assertDoesNotThrow(() -> dataSource.initConnection(dataSource.getConnection()));
    }

    @Test
    public void testOnConnectionCreated() {
        assertDoesNotThrow(() -> dataSource.onConnectionCreated(dataSource.getConnection()));
    }

    @Test
    public void testInnerCreateConnection() {
        assertDoesNotThrow(() -> dataSource.innerCreateConnection());
    }

}