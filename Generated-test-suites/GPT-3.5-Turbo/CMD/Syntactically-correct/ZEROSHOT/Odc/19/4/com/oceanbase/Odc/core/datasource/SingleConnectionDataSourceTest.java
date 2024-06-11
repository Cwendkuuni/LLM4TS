import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection conn = dataSource.getConnection("username", "password");
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = connection -> {
            // do nothing
        };
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testCloseIgnoredInvocationHandlerClose() throws Throwable {
        Connection connection = dataSource.getConnection();
        dataSource.getCloseIgnoreInvocationHandler().invoke(connection, Connection.class.getMethod("close"), null);
        assertFalse(dataSource.getLock().isLocked());
    }

    @Test
    public void testCloseIgnoredInvocationHandlerIsClosed() throws Throwable {
        Connection connection = dataSource.getConnection();
        boolean isClosed = (boolean) dataSource.getCloseIgnoreInvocationHandler().invoke(connection, Connection.class.getMethod("isClosed"), null);
        assertFalse(isClosed);
    }

    @Test
    public void testCloseIgnoredInvocationHandlerEquals() throws Throwable {
        Connection connection = dataSource.getConnection();
        boolean isEqual = (boolean) dataSource.getCloseIgnoreInvocationHandler().invoke(connection, Connection.class.getMethod("equals", Object.class), new Object[] { null });
        assertTrue(isEqual);
    }

    @Test
    public void testCloseIgnoredInvocationHandlerHashCode() throws Throwable {
        Connection connection = dataSource.getConnection();
        int hashCode = (int) dataSource.getCloseIgnoreInvocationHandler().invoke(connection, Connection.class.getMethod("hashCode"), null);
        assertEquals(System.identityHashCode(connection), hashCode);
    }

}