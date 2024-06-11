import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Test
    public void testGetConnectionWithNullConnection() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        assertThrows(NullPointerException.class, () -> singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionClosedOrNotValid() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalid", "credentials"));
    }

    @Test
    public void testInitConnectionFailure() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(mockInitializer);
        assertThrows(SQLException.class, () -> singleConnectionDataSource.initConnection(null));
    }

    @Test
    public void testResetConnectionFailure() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        assertThrows(SQLException.class, () -> singleConnectionDataSource.resetConnection());
    }

}