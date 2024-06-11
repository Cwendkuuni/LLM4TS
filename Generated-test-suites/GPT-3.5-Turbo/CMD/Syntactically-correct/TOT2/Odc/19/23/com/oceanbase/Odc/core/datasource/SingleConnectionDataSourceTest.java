import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Mock
    private Connection mockConnection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(mockConnection);

        Connection connection = dataSource.getConnection();

        assertEquals(mockConnection, connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenAutoReconnectFalseAndConnectionClosed() throws SQLException {
        dataSource.setAutoReconnect(false);
        dataSource.setConnection(mockConnection);
        when(mockConnection.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    // Add more test cases for other public methods

}