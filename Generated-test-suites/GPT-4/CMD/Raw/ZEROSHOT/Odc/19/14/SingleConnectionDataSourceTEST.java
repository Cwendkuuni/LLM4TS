
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource;
    Connection mockConnection;

    @Before
    public void setUp() throws Exception {
        singleConnectionDataSource = new SingleConnectionDataSource();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void getConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;

        assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void getConnectionWithUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.setUsername("testUser");
        singleConnectionDataSource.setPassword("testPassword");

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;

        assertEquals(singleConnectionDataSource.getConnection(),
            singleConnectionDataSource.getConnection("testUser", "testPassword"));
    }

    @Test(expected = SQLException.class)
    public void getConnectionWithInvalidUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.setUsername("testUser");
        singleConnectionDataSource.setPassword("testPassword");
        singleConnectionDataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void addInitializer() throws SQLException {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(mockInitializer);
        verify(mockInitializer, times(1)).init(any(Connection.class));
    }

    @Test
    public void close() {
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAutoReconnect() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        singleConnectionDataSource.connection = mockConnection;

        when(mockConnection.isClosed()).thenReturn(true);
        singleConnectionDataSource.getConnection();

        verify(mockConnection, times(1)).close();
    }

    @After
    public void tearDown() throws Exception {
        singleConnectionDataSource = null;
        mockConnection = null;
    }
}
